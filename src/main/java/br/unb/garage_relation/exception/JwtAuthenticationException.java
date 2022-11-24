package br.unb.garage_relation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException() {
        super("Jwt authentication failed");
    }

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
