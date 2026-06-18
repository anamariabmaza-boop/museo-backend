package com.undec.museobackend.output;

public interface PasswordResetNotifierPort {
    void sendResetToken(String email, String resetToken);
}
