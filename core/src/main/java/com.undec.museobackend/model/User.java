package com.undec.museobackend.model;

import com.undec.museobackend.valueobjects.Email;
import com.undec.museobackend.valueobjects.UserId;

import java.time.Instant;

public class User {

    private final UserId id;
    private final Email email;
    private String passwordHash;
    private Role role;
    private UserStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private User(UserId id, Email email, String passwordHash,
                 Role role, UserStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory Method principal
    public static User create(Email email, String passwordHash, Role role, Instant now) {
        return new User(
                UserId.generate(),
                email,
                passwordHash,
                role,
                UserStatus.ACTIVE,
                now,
                now
        );
    }

    // Factory Method para reconstruir desde persistencia
    public static User reconstitute(UserId id, Email email, String passwordHash,
                                    Role role, UserStatus status,
                                    Instant createdAt, Instant updatedAt) {
        return new User(id, email, passwordHash, role, status, createdAt, updatedAt);
    }

    public void changePassword(String newPasswordHash, Instant now) {
        this.passwordHash = newPasswordHash;
        this.updatedAt = now;
    }

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    public UserId getId() { return id; }
    public Email getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public UserStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}