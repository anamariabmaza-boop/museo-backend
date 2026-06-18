package com.undec.museobackend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String identifier) {
        super("Usuario no encontrado: " + identifier);
    }
}
