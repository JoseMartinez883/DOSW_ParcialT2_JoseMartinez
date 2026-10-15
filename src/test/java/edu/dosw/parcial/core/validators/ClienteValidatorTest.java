package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.controller.dtos.request.ClienteRequest;
import edu.dosw.parcial.core.models.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClienteValidatorTest {

    private ClienteValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ClienteValidator();
    }

    @Test
    @DisplayName("Debe validar exitosamente cuando el cliente tiene datos válidos")
    void validar_ClienteValido_NoLanzaExcepcion() {
        ClienteRequest cliente = ClienteRequest.builder()
                .nombre("Juan Perez")
                .telefono("3001234567")
                .correo("juan@gmail.com")
                .build();

        assertDoesNotThrow(() -> validator.validar(cliente));
    }

    @Test
    @DisplayName("Debe lanzar E03 si el objeto cliente es nulo")
    void validar_ClienteNulo_ThrowsE03() {
        BusinessException ex = assertThrows(BusinessException.class, () -> validator.validar(null));
        assertEquals("E03", ex.getCodigo());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Debe lanzar E03 si el nombre es nulo o vacío")
    void validar_NombreVacio_ThrowsE03() {
        ClienteRequest cliente = ClienteRequest.builder()
                .nombre("   ")
                .telefono("3001234567")
                .correo("juan@gmail.com")
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> validator.validar(cliente));
        assertEquals("E03", ex.getCodigo());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Debe lanzar E07 si el teléfono es nulo o tiene longitud distinta de 10")
    void validar_TelefonoInvalido_ThrowsE07() {
        ClienteRequest cliente = ClienteRequest.builder()
                .nombre("Juan Perez")
                .telefono("12345")
                .correo("juan@gmail.com")
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> validator.validar(cliente));
        assertEquals("E07", ex.getCodigo());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Debe lanzar E08 si el correo no termina en @gmail.com")
    void validar_CorreoNoGmail_ThrowsE08() {
        ClienteRequest cliente = ClienteRequest.builder()
                .nombre("Juan Perez")
                .telefono("3001234567")
                .correo("juan@hotmail.com")
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> validator.validar(cliente));
        assertEquals("E08", ex.getCodigo());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }
}
