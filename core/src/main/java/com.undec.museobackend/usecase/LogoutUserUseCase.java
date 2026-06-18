package com.undec.museobackend.usecase;

import com.undec.museobackend.output.LogoutUserPort;
import com.undec.museobackend.output.RefreshTokenRepositoryPort;
import com.undec.museobackend.valueobjects.UserId;

import java.util.UUID;

public class LogoutUserUseCase implements LogoutUserPort {

    private final RefreshTokenRepositoryPort refreshTokenRepository;

    public LogoutUserUseCase(RefreshTokenRepositoryPort refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void execute(String userId) {
        UserId id = UserId.of(UUID.fromString(userId));
        refreshTokenRepository.revokeAllByUserId(id);
    }
}
