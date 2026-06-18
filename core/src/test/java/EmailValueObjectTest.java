import com.undec.museobackend.exception.InvalidEmailException;
import com.undec.museobackend.valueobjects.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class EmailValueObjectTest {

    @Test
    void debeCrearEmailValido() {
        Email email = Email.of("Ana@Museo.COM");
        assertThat(email.getValue()).isEqualTo("ana@museo.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "noesunmail", "sin@dominio", "@sinusuario.com"})
    void debeLanzarExcepcionParaEmailsInvalidos(String invalid) {
        assertThatThrownBy(() -> Email.of(invalid))
                .isInstanceOf(InvalidEmailException.class);
    }

    @Test
    void debeLanzarExcepcionParaEmailNulo() {
        assertThatThrownBy(() -> Email.of(null))
                .isInstanceOf(InvalidEmailException.class);
    }

    @Test
    void dosEmailsIgualesDebenSerIguales() {
        Email e1 = Email.of("ana@museo.com");
        Email e2 = Email.of("ANA@MUSEO.COM");
        assertThat(e1).isEqualTo(e2);
    }
}