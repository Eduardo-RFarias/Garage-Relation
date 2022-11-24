package br.unb.garage_relation.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class RegisterNotFoundException extends RuntimeException {
    public RegisterNotFoundException() {
        super("Register not found");
    }

    public RegisterNotFoundException(String message) {
        super(message);
    }
}
