package com.undec.museobackend.config;

import com.undec.museobackend.output.*;
import com.undec.museobackend.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class BeanConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(
            UserRepositoryPort userRepository,
            PasswordHasherPort passwordHasher,
            Clock clock) {
        return new RegisterUserUseCase(userRepository, passwordHasher, clock);
    }

    @Bean
    public LoginUserUseCase loginUserUseCase(
            UserRepositoryPort userRepository,
            RefreshTokenRepositoryPort refreshTokenRepository,
            PasswordHasherPort passwordHasher,
            TokenGeneratorPort tokenGenerator,
            Clock clock) {
        return new LoginUserUseCase(userRepository, refreshTokenRepository,
                passwordHasher, tokenGenerator, clock);
    }

    @Bean
    public LogoutUserUseCase logoutUserUseCase(RefreshTokenRepositoryPort refreshTokenRepository) {
        return new LogoutUserUseCase(refreshTokenRepository);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(
            RefreshTokenRepositoryPort refreshTokenRepository,
            UserRepositoryPort userRepository,
            TokenGeneratorPort tokenGenerator,
            Clock clock) {
        return new RefreshTokenUseCase(refreshTokenRepository, userRepository, tokenGenerator, clock);
    }

    @Bean
    public RecoverPasswordUseCase recoverPasswordUseCase(
            UserRepositoryPort userRepository,
            TokenGeneratorPort tokenGenerator,
            PasswordResetNotifierPort notifier) {
        return new RecoverPasswordUseCase(userRepository, tokenGenerator, notifier);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(
            PasswordResetTokenRepositoryPort resetTokenRepository,
            UserRepositoryPort userRepository,
            PasswordHasherPort passwordHasher,
            Clock clock) {
        return new ResetPasswordUseCase(resetTokenRepository, userRepository, passwordHasher, clock);
    }

    @Bean
    public GetCurrentUserUseCase getCurrentUserUseCase(UserRepositoryPort userRepository) {
        return new GetCurrentUserUseCase(userRepository);
    }
}
