package com.undec.museobackend.persistence;

import com.undec.museobackend.model.User;
import com.undec.museobackend.output.UserRepositoryPort;
import com.undec.museobackend.valueobjects.Email;
import com.undec.museobackend.valueobjects.UserId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = toEntity(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue()).map(this::toDomain);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.getValue()).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    private UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId().getValue(),
                user.getEmail().getValue(),
                user.getPasswordHash(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private User toDomain(UserJpaEntity entity) {
        return User.reconstitute(
                UserId.of(entity.getId()),
                Email.of(entity.getEmail()),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}