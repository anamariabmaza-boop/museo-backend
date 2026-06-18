package com.undec.museobackend.controller;

import com.undec.museobackend.dto.request.*;
import com.undec.museobackend.dto.response.AuthResponse;
import com.undec.museobackend.dto.response.MessageResponse;
import com.undec.museobackend.dto.response.UserResponse;
import com.undec.museobackend.model.Role;
import com.undec.museobackend.output.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserPort registerUser;
    private final LoginUserPort loginUser;
    private final LogoutUserPort logoutUser;
    private final RefreshTokenPort refreshToken;
    private final RecoverPasswordPort recoverPassword;
    private final ResetPasswordPort resetPassword;
    private final GetCurrentUserPort getCurrentUser;

    public AuthController(RegisterUserPort registerUser,
                          LoginUserPort loginUser,
                          LogoutUserPort logoutUser,
                          RefreshTokenPort refreshToken,
                          RecoverPasswordPort recoverPassword,
                          ResetPasswordPort resetPassword,
                          GetCurrentUserPort getCurrentUser) {
        this.registerUser = registerUser;
        this.loginUser = loginUser;
        this.logoutUser = logoutUser;
        this.refreshToken = refreshToken;
        this.recoverPassword = recoverPassword;
        this.resetPassword = resetPassword;
        this.getCurrentUser = getCurrentUser;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        registerUser.execute(new RegisterUserPort.Command(
                request.email(), request.password(), Role.VISITANTE));
        return ResponseEntity.ok(new MessageResponse("Usuario registrado correctamente"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginUserPort.Result result = loginUser.execute(
                new LoginUserPort.Command(request.email(), request.password()));
        return ResponseEntity.ok(new AuthResponse(
                result.accessToken(), result.refreshToken(), result.email(), result.role()));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@AuthenticationPrincipal UserDetails userDetails) {
        // En producción extraerías el userId del JWT; aquí usamos el email para buscar
        // Simplificación: el use case recibe userId; ajustar según tu implementación
        logoutUser.execute(userDetails.getUsername()); // adaptar si necesitás UUID
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada correctamente"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestParam String token) {
        RefreshTokenPort.Result result = refreshToken.execute(
                new RefreshTokenPort.Command(token));
        return ResponseEntity.ok(new AuthResponse(result.accessToken(), result.refreshToken(), null, null));
    }


    @PostMapping("/recover-password")
    public ResponseEntity<MessageResponse> recoverPassword(
            @Valid @RequestBody RecoverPasswordRequest request) {
        recoverPassword.execute(request.email());
        return ResponseEntity.ok(new MessageResponse("Si el email existe, recibirás un enlace de recuperación"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        resetPassword.execute(new ResetPasswordPort.Command(request.token(), request.newPassword()));
        return ResponseEntity.ok(new MessageResponse("Contraseña restablecida correctamente"));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal UserDetails userDetails) {
        GetCurrentUserPort.Result result = getCurrentUser.execute(userDetails.getUsername());
        return ResponseEntity.ok(new UserResponse(
                result.userId(), result.email(), result.role(), result.status()));
    }
}