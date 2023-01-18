package br.unb.garage_relation.service;

import br.unb.garage_relation.model.User;
import br.unb.garage_relation.model.dto.request.LoginDTO;
import br.unb.garage_relation.model.dto.request.RegisterDTO;
import br.unb.garage_relation.model.dto.response.TokenResponseDTO;
import br.unb.garage_relation.model.dto.response.UserResponseDTO;
import br.unb.garage_relation.repository.UserRepository;
import br.unb.garage_relation.security.JwtService;
import br.unb.garage_relation.service.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

import static br.unb.garage_relation.Constants.ACCESS_TOKEN_COOKIE_NAME;
import static br.unb.garage_relation.Constants.REFRESH_TOKEN_COOKIE_NAME;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    public TokenResponseDTO login(LoginDTO credentials, HttpServletResponse response) {
        var user = userRepository.findByUsername(credentials.username()).orElseThrow(
                () -> new BadCredentialsException("Invalid username or password")
        );

        if (!passwordEncoder.matches(credentials.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password supplied");
        }

        var accessJwt = jwtService.createAccessToken(
                user.getUsername(),
                user.getRoles()
        );

        var refreshJwt = jwtService.createRefreshToken(
                user.getUsername(),
                user.getRoles()
        );

        response.addCookie(
                createCookie(
                        ACCESS_TOKEN_COOKIE_NAME,
                        accessJwt.getTokenValue(),
                        Objects.requireNonNull(accessJwt.getExpiresAt())
                )
        );

        response.addCookie(
                createCookie(
                        REFRESH_TOKEN_COOKIE_NAME,
                        refreshJwt.getTokenValue(),
                        Objects.requireNonNull(refreshJwt.getExpiresAt())
                )
        );

        return new TokenResponseDTO(
                accessJwt.getTokenValue(),
                refreshJwt.getTokenValue()
        );
    }

    public TokenResponseDTO refresh(String headerRefreshToken, String cookieRefreshToken, HttpServletResponse response) {
        String token;

        if (headerRefreshToken != null && headerRefreshToken.startsWith("Bearer ")) {
            token = headerRefreshToken.substring("Bearer ".length()).trim();
        } else if (cookieRefreshToken != null) {
            token = cookieRefreshToken;
        } else {
            throw new BadCredentialsException("Invalid refresh token");
        }

        var decodedToken = jwtService.decode(token);

        if (decodedToken.getExpiresAt() == null || decodedToken.getExpiresAt().isBefore(Instant.now())) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        var accessJwt = jwtService.createAccessToken(
                decodedToken.getSubject(),
                decodedToken.getClaim("roles")
        );

        var refreshJwt = jwtService.createRefreshToken(
                decodedToken.getSubject(),
                decodedToken.getClaim("roles")
        );

        response.addCookie(
                createCookie(
                        ACCESS_TOKEN_COOKIE_NAME,
                        accessJwt.getTokenValue(),
                        Objects.requireNonNull(accessJwt.getExpiresAt())
                )
        );

        response.addCookie(
                createCookie(
                        REFRESH_TOKEN_COOKIE_NAME,
                        refreshJwt.getTokenValue(),
                        Objects.requireNonNull(refreshJwt.getExpiresAt())
                )
        );

        return new TokenResponseDTO(
                accessJwt.getTokenValue(),
                refreshJwt.getTokenValue()
        );
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        var cookies = request.getCookies();

        if (cookies == null) {
            return;
        }

        for (var cookie : cookies) {
            var name = cookie.getName();

            if (name.equals(ACCESS_TOKEN_COOKIE_NAME) || name.equals(REFRESH_TOKEN_COOKIE_NAME)) {
                response.addCookie(invalidateCookie(name));
            }
        }
    }

    public EntityModel<UserResponseDTO> signup(RegisterDTO registerDTO) {
        var hashedPassword = passwordEncoder.encode(registerDTO.password());

        var user = new User(
                registerDTO.username(),
                hashedPassword,
                registerDTO.email(),
                registerDTO.fullName()
        );

        userRepository.save(user);

        return userMapper.toModel(user);
    }

    public User getAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        var principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            return userRepository.findByUsername(jwt.getSubject()).orElseThrow(
                    () -> new BadCredentialsException("Invalid username or password")
            );
        }

        throw new BadCredentialsException("Invalid username or password");
    }

    private Cookie createCookie(String name, String value, Instant maxAge) {
        var expiresAt = maxAge.getEpochSecond() - Instant.now().getEpochSecond();

        var cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) expiresAt);
        cookie.setPath("/");
        return cookie;
    }

    private Cookie invalidateCookie(String name) {
        var cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
