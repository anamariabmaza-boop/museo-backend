package com.undec.museobackend.security;

import com.undec.museobackend.model.User;
import com.undec.museobackend.output.TokenGeneratorPort;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider implements TokenGeneratorPort {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    @Override
    public String generateAccessToken(User user) {
        //.claim("userId", user.getId().getValue().toString())
        //existe para cuando implementemos el endpoint de logout y necesitemos extraer
        // el userId del token en el controller.
        return Jwts.builder()
                .subject(user.getEmail().getValue())
                .claim("role", user.getRole().name())
                .claim("userId", user.getId().getValue().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    @Override
    public boolean isTokenValid(String token, String email) {
        try {
            Claims claims = parseClaims(token);
            return claims.getSubject().equals(email)
                    && claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}