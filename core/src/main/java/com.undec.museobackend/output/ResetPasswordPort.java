package com.undec.museobackend.output;

public interface ResetPasswordPort {
    record Command(String resetToken, String newPassword) {}
    void execute(Command command);
}
