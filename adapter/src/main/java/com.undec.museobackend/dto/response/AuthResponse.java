package com.undec.museobackend.dto.response;
public record AuthResponse(String accessToken, String refreshToken, String email, String role) {}