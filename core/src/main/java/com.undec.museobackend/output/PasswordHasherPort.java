package com.undec.museobackend.output;

public interface PasswordHasherPort {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}
