package br.unb.garage_relation.service;

import br.unb.garage_relation.exception.JwtAuthenticationException;
import br.unb.garage_relation.model.dto.request.LoginDTO;
import br.unb.garage_relation.model.dto.response.TokenResponseDTO;
import br.unb.garage_relation.repository.UserRepository;
import br.unb.garage_relation.security.JwtTokenHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtTokenHandler jwtTokenHandler;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthService(JwtTokenHandler jwtTokenHandler, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.jwtTokenHandler = jwtTokenHandler;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public TokenResponseDTO signIn(LoginDTO credentials) {
        var username = credentials.username();
        var password = credentials.password();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        return jwtTokenHandler.createTokenResponse(username, user.getRoles());
    }

    public TokenResponseDTO refreshToken(String refreshToken) throws JwtAuthenticationException {
        return jwtTokenHandler.createRefreshTokenResponse(refreshToken);
    }
}
