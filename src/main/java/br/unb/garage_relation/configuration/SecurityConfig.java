package br.unb.garage_relation.configuration;

import br.unb.garage_relation.security.JwtTokenConfigurer;
import br.unb.garage_relation.security.JwtTokenHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.logging.Logger;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger LOGGER = Logger.getLogger(SecurityConfig.class.getName());
    private final JwtTokenHandler jwtTokenHandler;

    public SecurityConfig(JwtTokenHandler jwtTokenHandler) {
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configureFilters(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(STATELESS)
                )
                .authorizeHttpRequests((auth) -> {
                            auth.requestMatchers(
                                    "/auth/signIn",
                                    "/auth/refresh",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**"
                            ).permitAll();
                            auth.requestMatchers("/api/**").authenticated();
                            auth.requestMatchers("/users").denyAll();
                        }
                )
                .cors().and()
                .apply(new JwtTokenConfigurer(jwtTokenHandler));

        return http.build();
    }
}
