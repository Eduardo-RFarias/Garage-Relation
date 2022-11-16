package br.unb.garage_relation.exception;

public abstract class ApplicationException extends Exception {
    public ApplicationException(String message) {
        super(message);
    }
}
