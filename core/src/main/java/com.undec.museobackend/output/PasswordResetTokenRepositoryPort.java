package com.undec.museobackend.output;

import java.util.Optional;

public interface PasswordResetTokenRepositoryPort {
    void save(String email, String token);
    Optional<String> findEmailByToken(String token);
    void deleteByToken(String token);
}
