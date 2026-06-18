package com.undec.museobackend.usecase;

import com.undec.museobackend.exception.UserAlreadyExistsException;
import com.undec.museobackend.model.User;
import com.undec.museobackend.output.PasswordHasherPort;
import com.undec.museobackend.output.RegisterUserPort;
import com.undec.museobackend.output.UserRepositoryPort;
import com.undec.museobackend.valueobjects.Email;

import javax.xml.transform.Result;
import java.time.Clock;
import java.time.Instant;

public class RegisterUserUseCase implements RegisterUserPort {

    private final UserRepositoryPort userRepository;
    private final PasswordHasherPort passwordHasher;
    private final Clock clock;

    public RegisterUserUseCase(UserRepositoryPort userRepository,
                               PasswordHasherPort passwordHasher,
                               Clock clock) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.clock = clock;
    }

    @Override
    public Result execute(Command command) {
        Email email = Email.of(command.email());

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email.getValue());
        }

        String passwordHash = passwordHasher.hash(command.rawPassword());
        Instant now = Instant.now(clock);

        User user = User.create(email, passwordHash, command.role(), now);
        User saved = userRepository.save(user);

        return new Result(
                saved.getId().getValue().toString(),
                saved.getEmail().getValue(),
                saved.getRole().name()
        );
    }
}