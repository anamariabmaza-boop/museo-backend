package com.undec.museobackend.output;

public interface LoginUserPort {
    record Command(String email, String rawPassword) {}
    record Result(String accessToken, String refreshToken, String email, String role) {}
    Result execute(Command command);
}
