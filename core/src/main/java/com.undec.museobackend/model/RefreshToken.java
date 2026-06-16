package com.undec.museobackend.model;

import com.undec.museobackend.valueobjects.UserId;

import java.time.Instant;
import java.util.UUID;

public class RefreshToken {

    private final UUID id;
    private final String token;
    private final UserId userId;
    private final Instant expirationDate;
    private boolean revoked;

    private RefreshToken(UUID id, String token, UserId userId,
                         Instant expirationDate, boolean revoked) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expirationDate = expirationDate;
        this.revoked = revoked;
    }

    public static RefreshToken create(String token, UserId userId, Instant expirationDate) {
        return new RefreshToken(UUID.randomUUID(), token, userId, expirationDate, false);
    }

    public static RefreshToken reconstitute(UUID id, String token, UserId userId,
                                            Instant expirationDate, boolean revoked) {
        return new RefreshToken(id, token, userId, expirationDate, revoked);
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(this.expirationDate);
    }

    public boolean isValid(Instant now) {
        return !revoked && !isExpired(now);
    }

    public void revoke() {
        this.revoked = true;
    }

    public UUID getId() { return id; }
    public String getToken() { return token; }
    public UserId getUserId() { return userId; }
    public Instant getExpirationDate() { return expirationDate; }
    public boolean isRevoked() { return revoked; }
}
