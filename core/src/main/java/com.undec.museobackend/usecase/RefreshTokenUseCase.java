package com.undec.museobackend.usecase;

import com.undec.museobackend.exception.InvalidTokenException;
import com.undec.museobackend.exception.UserNotFoundException;
import com.undec.museobackend.model.RefreshToken;
import com.undec.museobackend.model.User;
import com.undec.museobackend.output.RefreshTokenPort;
import com.undec.museobackend.output.RefreshTokenRepositoryPort;
import com.undec.museobackend.output.TokenGeneratorPort;
import com.undec.museobackend.output.UserRepositoryPort;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RefreshTokenUseCase implements RefreshTokenPort {

    private static final long REFRESH_TOKEN_DAYS = 7;

    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final UserRepositoryPort userRepository;
    private final TokenGeneratorPort tokenGenerator;
    private final Clock clock;

    public RefreshTokenUseCase(RefreshTokenRepositoryPort refreshTokenRepository,
                               UserRepositoryPort userRepository,
                               TokenGeneratorPort tokenGenerator,
                               Clock clock) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.clock = clock;
    }

    @Override
    public Result execute(Command command) {
        Instant now = Instant.now(clock);

        RefreshToken storedToken = refreshTokenRepository.findByToken(command.refreshToken())
                .orElseThrow(() -> new InvalidTokenException("token no encontrado"));

        if (!storedToken.isValid(now)) {
            throw new InvalidTokenException("token expirado o revocado");
        }

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException(storedToken.getUserId().toString()));

        storedToken.revoke();
        refreshTokenRepository.save(storedToken);

        String newAccessToken = tokenGenerator.generateAccessToken(user);
        String newRawRefreshToken = tokenGenerator.generateRefreshToken();
        Instant newExpiration = now.plus(REFRESH_TOKEN_DAYS, ChronoUnit.DAYS);

        RefreshToken newRefreshToken = RefreshToken.create(newRawRefreshToken, user.getId(), newExpiration);
        refreshTokenRepository.save(newRefreshToken);

        return new Result(newAccessToken, newRawRefreshToken);
    }
}
