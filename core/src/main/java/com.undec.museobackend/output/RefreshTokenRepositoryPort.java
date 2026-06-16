package com.undec.museobackend.output;

import com.undec.museobackend.model.RefreshToken;
import com.undec.museobackend.valueobjects.UserId;

import java.util.Optional;

public interface RefreshTokenRepositoryPort {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    void revokeAllByUserId(UserId userId);
}
