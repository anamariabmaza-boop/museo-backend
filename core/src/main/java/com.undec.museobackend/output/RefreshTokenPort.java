package com.undec.museobackend.output;

public interface RefreshTokenPort {
    record Command(String refreshToken) {}
    record Result(String accessToken, String refreshToken) {}
    Result execute(Command command);
}
