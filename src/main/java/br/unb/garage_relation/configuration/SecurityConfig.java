package br.unb.garage_relation.configuration;

import br.unb.garage_relation.security.CookieAndHeaderBearerTokenResolver;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${security.jwt.public-key}")
    private RSAPublicKey publicKey;

    @Value("${security.jwt.private-key}")
    private RSAPrivateKey privateKey;

    @PostConstruct
    protected void init() {
        if (publicKey == null || privateKey == null) {
            throw new RuntimeException("Public and private keys must be set");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }

    @Bean
    public SecurityFilterChain configureFilters(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> {
                            auth.requestMatchers(
                                    "/auth/**",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**"
                            ).permitAll();
                            auth.requestMatchers("/api/**").authenticated();
                            auth.requestMatchers("/users").denyAll();
                        }
                )
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .oauth2ResourceServer((oauth2) -> {
                    oauth2.jwt();
                    oauth2.bearerTokenResolver(new CookieAndHeaderBearerTokenResolver());
                })
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(STATELESS)
                )
                .exceptionHandling((exceptions) -> {
                    exceptions.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                    exceptions.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
                });

        return http.build();
    }
}
