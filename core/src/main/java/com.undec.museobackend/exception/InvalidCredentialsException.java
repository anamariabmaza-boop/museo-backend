package com.undec.museobackend.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super("Credenciales inválidas");
    }
}
