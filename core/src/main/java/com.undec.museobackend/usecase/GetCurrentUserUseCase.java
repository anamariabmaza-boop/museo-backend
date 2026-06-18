package com.undec.museobackend.usecase;

import com.undec.museobackend.exception.UserNotFoundException;
import com.undec.museobackend.model.User;
import com.undec.museobackend.output.GetCurrentUserPort;
import com.undec.museobackend.output.UserRepositoryPort;
import com.undec.museobackend.valueobjects.Email;
import com.undec.museobackend.valueobjects.UserId;

import java.util.UUID;

public class GetCurrentUserUseCase implements GetCurrentUserPort {

    private final UserRepositoryPort userRepository;

    public GetCurrentUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Result execute(String email) {
        User user = userRepository.findByEmail(Email.of(email))
                .orElseThrow(() -> new UserNotFoundException(email));

        return new Result(
                user.getId().getValue().toString(),
                user.getEmail().getValue(),
                user.getRole().name(),
                user.getStatus().name()
        );
    }
}
