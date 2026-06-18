package com.undec.museobackend.output;

import com.undec.museobackend.model.Role;

public interface RegisterUserPort {
    record Command(String email, String rawPassword, Role role) {}
    record Result(String userId, String email, String role) {}
    Result execute(Command command);
}
