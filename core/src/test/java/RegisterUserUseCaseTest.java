import com.undec.museobackend.exception.UserAlreadyExistsException;
import com.undec.museobackend.model.Role;
import com.undec.museobackend.model.User;
import com.undec.museobackend.output.PasswordHasherPort;
import com.undec.museobackend.output.RegisterUserPort;
import com.undec.museobackend.output.UserRepositoryPort;
import com.undec.museobackend.usecase.RegisterUserUseCase;
import com.undec.museobackend.valueobjects.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;
    @Mock private PasswordHasherPort passwordHasher;

    private Clock fixedClock;
    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(Instant.parse("2024-01-01T10:00:00Z"), ZoneId.of("UTC"));
        useCase = new RegisterUserUseCase(userRepository, passwordHasher, fixedClock);
    }

    @Test
    void debeRegistrarUsuarioExitosamente() {
        // arrange
        RegisterUserPort.Command command = new RegisterUserPort.Command(
                "ana@museo.com", "password123", Role.VISITANTE);

        when(userRepository.existsByEmail(Email.of("ana@museo.com"))).thenReturn(false);
        when(passwordHasher.hash("password123")).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // act
        RegisterUserPort.Result result = useCase.execute(command);

        // assert
        assertThat(result.email()).isEqualTo("ana@museo.com");
        assertThat(result.role()).isEqualTo("VISITANTE");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void debeLanzarExcepcionSiEmailYaExiste() {
        RegisterUserPort.Command command = new RegisterUserPort.Command(
                "ana@museo.com", "password123", Role.VISITANTE);

        when(userRepository.existsByEmail(Email.of("ana@museo.com"))).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("ana@museo.com");

        verify(userRepository, never()).save(any());
    }

    @Test
    void debeUsarFechaDelReloj() {
        RegisterUserPort.Command command = new RegisterUserPort.Command(
                "ana@museo.com", "password123", Role.VISITANTE);

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordHasher.hash(any())).thenReturn("hash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User user = inv.getArgument(0);
            assertThat(user.getCreatedAt()).isEqualTo(Instant.parse("2024-01-01T10:00:00Z"));
            return user;
        });

        useCase.execute(command);
    }
}