package com.undec.museobackend.usecase;

import com.undec.museobackend.exception.InvalidCredentialsException;
import com.undec.museobackend.model.RefreshToken;
import com.undec.museobackend.model.User;
import com.undec.museobackend.output.*;
import com.undec.museobackend.valueobjects.Email;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class LoginUserUseCase implements LoginUserPort {

    private static final long REFRESH_TOKEN_DAYS = 7;

    private final UserRepositoryPort userRepository;
    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final PasswordHasherPort passwordHasher;
    private final TokenGeneratorPort tokenGenerator;
    private final Clock clock;

    public LoginUserUseCase(UserRepositoryPort userRepository,
                            RefreshTokenRepositoryPort refreshTokenRepository,
                            PasswordHasherPort passwordHasher,
                            TokenGeneratorPort tokenGenerator,
                            Clock clock) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
        this.clock = clock;
    }

    @Override
    public Result execute(Command command) {
        Email email = Email.of(command.email());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException("Error, Credencial no válida"));

        if (!user.isActive()) {
            throw new InvalidCredentialsException("Error, Credencial no válida");
        }

        if (!passwordHasher.matches(command.rawPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Error, Credencial no válida");
        }

        refreshTokenRepository.revokeAllByUserId(user.getId());

        String accessToken = tokenGenerator.generateAccessToken(user);
        String rawRefreshToken = tokenGenerator.generateRefreshToken();

        Instant expiration = Instant.now(clock).plus(REFRESH_TOKEN_DAYS, ChronoUnit.DAYS);
        RefreshToken refreshToken = RefreshToken.create(rawRefreshToken, user.getId(), expiration);
        refreshTokenRepository.save(refreshToken);

        return new Result(
                accessToken,
                rawRefreshToken,
                user.getEmail().getValue(),
                user.getRole().name()
        );

    }
}