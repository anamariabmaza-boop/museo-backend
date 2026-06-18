package com.undec.museobackend.persistence;


import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenJpaRepository
        extends JpaRepository<PasswordResetTokenJpaEntity, String> {
}
