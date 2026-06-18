package com.undec.museobackend.persistence;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetTokenJpaEntity {

    @Id
    @Column(length = 36)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Instant createdAt;

    public PasswordResetTokenJpaEntity() {}

    public PasswordResetTokenJpaEntity(String token, String email, Instant createdAt) {
        this.token = token;
        this.email = email;
        this.createdAt = createdAt;
    }

    public String getToken() { return token; }
    public String getEmail() { return email; }
    public Instant getCreatedAt() { return createdAt; }
}
