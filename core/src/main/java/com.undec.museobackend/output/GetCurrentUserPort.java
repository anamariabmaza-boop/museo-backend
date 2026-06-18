package com.undec.museobackend.output;

public interface GetCurrentUserPort {
    record Result(String userId, String email, String role, String status) {}
    Result execute(String userId);
}
