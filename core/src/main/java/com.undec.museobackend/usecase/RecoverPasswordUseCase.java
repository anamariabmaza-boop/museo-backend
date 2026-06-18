package com.undec.museobackend.usecase;

import com.undec.museobackend.exception.UserNotFoundException;
import com.undec.museobackend.model.User;
import com.undec.museobackend.output.PasswordResetNotifierPort;
import com.undec.museobackend.output.RecoverPasswordPort;
import com.undec.museobackend.output.TokenGeneratorPort;
import com.undec.museobackend.output.UserRepositoryPort;
import com.undec.museobackend.valueobjects.Email;

public class RecoverPasswordUseCase implements RecoverPasswordPort {

    private final UserRepositoryPort userRepository;
    private final TokenGeneratorPort tokenGenerator;
    private final PasswordResetNotifierPort notifier;

    public RecoverPasswordUseCase(UserRepositoryPort userRepository,
                                  TokenGeneratorPort tokenGenerator,
                                  PasswordResetNotifierPort notifier) {
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.notifier = notifier;
    }

    @Override
    public void execute(String email) {
        Email emailVO = Email.of(email);

        User user = userRepository.findByEmail(emailVO)
                .orElseThrow(() -> new UserNotFoundException(email));

        String resetToken = tokenGenerator.generateRefreshToken();
        notifier.sendResetToken(user.getEmail().getValue(), resetToken);
    }
}
