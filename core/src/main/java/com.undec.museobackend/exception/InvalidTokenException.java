package com.undec.museobackend.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String reason) {
        super("Token inválido: " + reason);
    }
}
