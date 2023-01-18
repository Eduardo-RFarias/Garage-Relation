package br.unb.garage_relation.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@Service
public class JwtService {
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    @Value("${security.jwt.validity.access:1 day}")
    private String accessExpiration = "1 day";

    @Value("${security.jwt.validity.refresh:1 day}")
    private String refreshExpiration = "1 day";

    public JwtService(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public Jwt createAccessToken(String username, List<String> roles) {
        var now = Instant.now();
        var expiresAt = now.plusMillis(parseValueToMilliseconds(accessExpiration));

        return createToken(username, roles, now, expiresAt);
    }

    public Jwt createRefreshToken(String username, List<String> roles) {
        var now = Instant.now();
        var expiresAt = now.plusMillis(parseValueToMilliseconds(refreshExpiration));

        return createToken(username, roles, now, expiresAt);
    }

    public Jwt decode(String token) {
        return decoder.decode(token);
    }

    private Jwt createToken(String username, List<String> roles, Instant now, Instant expiresAt) {
        var issuer = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(username)
                .claim("roles", roles)
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims));
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
