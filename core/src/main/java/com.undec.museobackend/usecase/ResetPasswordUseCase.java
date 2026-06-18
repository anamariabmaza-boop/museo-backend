package com.undec.museobackend.usecase;

import com.undec.museobackend.exception.InvalidTokenException;
import com.undec.museobackend.exception.UserNotFoundException;
import com.undec.museobackend.model.User;
import com.undec.museobackend.output.PasswordHasherPort;
import com.undec.museobackend.output.PasswordResetTokenRepositoryPort;
import com.undec.museobackend.output.ResetPasswordPort;
import com.undec.museobackend.output.UserRepositoryPort;
import com.undec.museobackend.valueobjects.Email;

import java.time.Clock;
import java.time.Instant;

public class ResetPasswordUseCase implements ResetPasswordPort {

    private final PasswordResetTokenRepositoryPort resetTokenRepository;
    private final UserRepositoryPort userRepository;
    private final PasswordHasherPort passwordHasher;
    private final Clock clock;

    public ResetPasswordUseCase(PasswordResetTokenRepositoryPort resetTokenRepository,
                                UserRepositoryPort userRepository,
                                PasswordHasherPort passwordHasher,
                                Clock clock) {
        this.resetTokenRepository = resetTokenRepository;
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.clock = clock;
    }

    @Override
    public void execute(Command command) {
        String email = resetTokenRepository.findEmailByToken(command.resetToken())
                .orElseThrow(() -> new InvalidTokenException("token de recuperación no válido"));

        User user = userRepository.findByEmail(Email.of(email))
                .orElseThrow(() -> new UserNotFoundException(email));

        String newHash = passwordHasher.hash(command.newPassword());
        user.changePassword(newHash, Instant.now(clock));
        userRepository.save(user);

        resetTokenRepository.deleteByToken(command.resetToken());
    }
}