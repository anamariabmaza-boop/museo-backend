package com.undec.museobackend.output;
import com.undec.museobackend.model.User;
import com.undec.museobackend.valueobjects.Email;
import com.undec.museobackend.valueobjects.UserId;
import java.util.Optional;
public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(Email email);
    Optional<User> findById(UserId id);
    boolean existsByEmail(Email email);
}
