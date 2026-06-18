package com.undec.museobackend.security;

import com.undec.museobackend.output.PasswordResetNotifierPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//reemplazá con JavaMailSender cuando esté listo
@Component
public class PasswordResetNotifierAdapter implements PasswordResetNotifierPort {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetNotifierAdapter.class);

    @Override
    public void sendResetToken(String email, String resetToken) {
        // TODO: reemplazar con envío real de email
        log.info("[STUB] Token de recuperación para {}: {}", email, resetToken);
    }
}