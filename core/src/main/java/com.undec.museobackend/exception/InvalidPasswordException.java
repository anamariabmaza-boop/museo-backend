package com.undec.museobackend.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String reason) {
        super("Contraseña inválida: " + reason);
    }
}
