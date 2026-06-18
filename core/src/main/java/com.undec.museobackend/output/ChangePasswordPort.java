package com.undec.museobackend.output;

public interface ChangePasswordPort {
    record Command(String userId, String currentPassword, String newPassword) {}
    void execute(Command command);
}
