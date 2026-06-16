package com.undec.museobackend.output;

import com.undec.museobackend.model.User;

public interface TokenGeneratorPort {
    String generateAccessToken(User user);
    String generateRefreshToken();
    String extractEmail(String token);
    boolean isTokenValid(String token, String email);
}
