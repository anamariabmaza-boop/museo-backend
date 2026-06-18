package com.undec.museobackend.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenJpaEntity {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    @org.hibernate.annotations.JdbcTypeCode(java.sql.Types.VARCHAR)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private Instant expirationDate;

    @Column(nullable = false)
    private boolean revoked;

    public RefreshTokenJpaEntity() {}

    public RefreshTokenJpaEntity(UUID id, String token, UUID userId,
                                 Instant expirationDate, boolean revoked) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expirationDate = expirationDate;
        this.revoked = revoked;
    }

    public UUID getId() { return id; }
    public String getToken() { return token; }
    public UUID getUserId() { return userId; }
    public Instant getExpirationDate() { return expirationDate; }
    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }
}