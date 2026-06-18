import com.undec.museobackend.exception.InvalidCredentialsException;
import com.undec.museobackend.model.Role;
import com.undec.museobackend.model.User;
import com.undec.museobackend.model.UserStatus;
import com.undec.museobackend.output.*;
import com.undec.museobackend.usecase.LoginUserUseCase;
import com.undec.museobackend.valueobjects.Email;
import com.undec.museobackend.valueobjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;
    @Mock private RefreshTokenRepositoryPort refreshTokenRepository;
    @Mock private PasswordHasherPort passwordHasher;
    @Mock private TokenGeneratorPort tokenGenerator;

    private Clock fixedClock;
    private LoginUserUseCase useCase;
    private User activeUser;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2024-01-01T10:00:00Z"), ZoneId.of("UTC"));
        useCase = new LoginUserUseCase(userRepository, refreshTokenRepository,
                passwordHasher, tokenGenerator, fixedClock);

        activeUser = User.reconstitute(
                UserId.generate(),
                Email.of("ana@museo.com"),
                "hashed_password",
                Role.VISITANTE,
                UserStatus.ACTIVE,
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    void debeRetornarTokensEnLoginExitoso() {
        when(userRepository.findByEmail(Email.of("ana@museo.com")))
                .thenReturn(Optional.of(activeUser));
        when(passwordHasher.matches("password123", "hashed_password")).thenReturn(true);
        when(tokenGenerator.generateAccessToken(activeUser)).thenReturn("access_token");
        when(tokenGenerator.generateRefreshToken()).thenReturn("refresh_token");
        when(refreshTokenRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        LoginUserPort.Result result = useCase.execute(
                new LoginUserPort.Command("ana@museo.com", "password123"));

        assertThat(result.accessToken()).isEqualTo("access_token");
        assertThat(result.refreshToken()).isEqualTo("refresh_token");
        assertThat(result.email()).isEqualTo("ana@museo.com");
    }

    @Test
    void debeLanzarExcepcionSiUsuarioNoExiste() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(
                new LoginUserPort.Command("noexiste@museo.com", "pass")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void debeLanzarExcepcionSiPasswordIncorrecta() {
        when(userRepository.findByEmail(Email.of("ana@museo.com")))
                .thenReturn(Optional.of(activeUser));
        when(passwordHasher.matches("wrongpass", "hashed_password")).thenReturn(false);

        assertThatThrownBy(() -> useCase.execute(
                new LoginUserPort.Command("ana@museo.com", "wrongpass")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void debeLanzarExcepcionSiUsuarioInactivo() {
        User inactiveUser = User.reconstitute(
                UserId.generate(), Email.of("ana@museo.com"), "hash",
                Role.VISITANTE, UserStatus.INACTIVE, Instant.now(), Instant.now());

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(inactiveUser));

        assertThatThrownBy(() -> useCase.execute(
                new LoginUserPort.Command("ana@museo.com", "password")))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}