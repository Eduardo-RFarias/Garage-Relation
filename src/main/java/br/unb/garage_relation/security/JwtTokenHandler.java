package br.unb.garage_relation.security;

import br.unb.garage_relation.exception.JwtAuthenticationException;
import br.unb.garage_relation.model.dto.response.TokenResponseDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class JwtTokenHandler {
    @Value("${security.jwt.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token-prefix:Bearer}")
    private String tokenPrefix = "Bearer";

    @Value("${security.jwt.expiration:1 days}")
    private String expiration = "1 days";

    @Value("${security.jwt.refresh-expiration:1 days}")
    private String refreshExpiration = "1 days";

    private final UserDetailsService userDetailsService;

    public JwtTokenHandler(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public TokenResponseDTO createTokenResponse(String username, List<String> roles) {
        var now = new Date();
        var expirationDate = new Date(now.getTime() + this.parseValueToMilliseconds(expiration));
        var accessToken = this.createAccessToken(username, roles, now, expirationDate);
        var refreshToken = this.createRefreshToken(username, roles, now);

        return new TokenResponseDTO(
                username,
                true,
                now,
                expirationDate,
                accessToken,
                refreshToken

        );
    }

    public TokenResponseDTO createRefreshTokenResponse(String refreshToken) throws JwtAuthenticationException {
        var parsedRefreshToken = parseBearerToken(refreshToken);
        var decodedToken = decodeJwt(parsedRefreshToken);

        var username = decodedToken.getSubject();
        var roles = decodedToken.getClaim("roles").asList(String.class);

        return createTokenResponse(
                username,
                roles
        );
    }

    public Authentication getAuthentication(String token) throws JwtAuthenticationException {
        var decodedJwt = decodeJwt(token);
        var userDetails = userDetailsService.loadUserByUsername(decodedJwt.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Optional<String> resolveToken(HttpServletRequest req) {
        var bearerToken = req.getHeader("Authorization");

        try {
            var parsedToken = parseBearerToken(bearerToken);
            return Optional.of(parsedToken);
        } catch (JwtAuthenticationException e) {
            return Optional.empty();
        }
    }

    public boolean validateToken(String token) throws JwtAuthenticationException {
        var decodedJwt = decodeJwt(token);
        var now = new Date();

        return decodedJwt.getExpiresAt().after(now);
    }

    private String parseBearerToken(String token) throws JwtAuthenticationException {
        var prefix = tokenPrefix + " ";

        if (token != null && token.startsWith(prefix)) {
            return token.substring(prefix.length()).trim();
        }

        throw new JwtAuthenticationException("Invalid token");
    }

    private DecodedJWT decodeJwt(String token) throws JwtAuthenticationException {
        var algorithm = Algorithm.HMAC256(secretKey);
        var jwtVerifier = JWT.require(algorithm).build();

        try {
            return jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JwtAuthenticationException(e.getMessage());
        }
    }

    private String createAccessToken(String username, List<String> roles, Date now, Date expirationDate) {
        var issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        var algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .trim();
    }

    private String createRefreshToken(String username, List<String> roles, Date now) {
        var algorithm = Algorithm.HMAC256(secretKey);
        var expirationDate = new Date(now.getTime() + this.parseValueToMilliseconds(refreshExpiration));

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .withSubject(username)
                .sign(algorithm)
                .trim();
    }

    private long parseValueToMilliseconds(String expiration) {
        var expirationArray = expiration.trim().split(" ");

        var unit = expirationArray[0];
        var measure = expirationArray[1];

        try {
            var number = Long.parseLong(unit);

            return switch (measure) {
                case "second", "seconds" -> number * 1000;
                case "minute", "minutes" -> number * 60 * 1000;
                case "hour", "hours" -> number * 60 * 60 * 1000;
                case "day", "days" -> number * 24 * 60 * 60 * 1000;
                case "week", "weeks" -> number * 7 * 24 * 60 * 60 * 1000;
                case "month", "months" -> number * 30 * 24 * 60 * 60 * 1000;
                case "year", "years" -> number * 365 * 24 * 60 * 60 * 1000;
                default -> throw new IllegalArgumentException("Invalid time unit");
            };

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid expiration value");
        }
    }
}
