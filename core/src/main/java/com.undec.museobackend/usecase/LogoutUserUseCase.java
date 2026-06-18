package com.undec.museobackend.usecase;

import com.undec.museobackend.output.LogoutUserPort;
import com.undec.museobackend.output.RefreshTokenRepositoryPort;
import com.undec.museobackend.output.UserRepositoryPort;
import com.undec.museobackend.valueobjects.Email;


public class LogoutUserUseCase implements LogoutUserPort {

    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final UserRepositoryPort userRepository;

    public LogoutUserUseCase(RefreshTokenRepositoryPort refreshTokenRepository,
                             UserRepositoryPort userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(String email) {
        userRepository.findByEmail(Email.of(email))
                .ifPresent(user -> refreshTokenRepository.revokeAllByUserId(user.getId()));
    }
}