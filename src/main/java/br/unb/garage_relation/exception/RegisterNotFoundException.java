package br.unb.garage_relation.exception;

public class RegisterNotFoundException extends ApplicationException {
    public RegisterNotFoundException() {
        super("Register not found");
    }
}
