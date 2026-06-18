package com.undec.museobackend.persistence;
import com.undec.museobackend.output.PasswordResetTokenRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepositoryPort {

    private final PasswordResetTokenJpaRepository jpaRepository;

    public PasswordResetTokenRepositoryAdapter(PasswordResetTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(String email, String token) {
        jpaRepository.save(new PasswordResetTokenJpaEntity(token, email, Instant.now()));
    }

    @Override
    public Optional<String> findEmailByToken(String token) {
        return jpaRepository.findById(token).map(PasswordResetTokenJpaEntity::getEmail);
    }

    @Override
    public void deleteByToken(String token) {
        jpaRepository.deleteById(token);
    }
}