package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.controller.handlers.GlobalExceptionHandler;
import edu.dosw.parcial.core.models.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Debe manejar BusinessException retornando el estado y código esperado")
    void handleBusinessException_ReturnsCustomErrorResponse() {
        // Arrange
        BusinessException ex = new BusinessException("E01", "Franja llena.", HttpStatus.CONFLICT);

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleBusinessException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("E01", response.getBody().getCodigo());
        assertEquals("Franja llena.", response.getBody().getMensaje());
        assertEquals(409, response.getBody().getEstado());
    }

    @Test
    @DisplayName("Debe mapear errores de validación de correo a E08")
    void handleValidationException_Correo_ReturnsE08() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "cliente.correo", "debe ser gmail");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldError()).thenReturn(fieldError);

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleValidationException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("E08", response.getBody().getCodigo());
    }

    @Test
    @DisplayName("Debe manejar BadCredentialsException retornando 401 y E_CREDENCIALES_INVALIDAS")
    void handleBadCredentials_Returns401() {
        // Arrange
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleBadCredentials(ex);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("E_CREDENCIALES_INVALIDAS", response.getBody().getCodigo());
    }

    @Test
    @DisplayName("Debe manejar AuthenticationException retornando 401 y E_NO_AUTORIZADO")
    void handleAuthenticationException_Returns401() {
        // Arrange
        AuthenticationException ex = mock(AuthenticationException.class);
        when(ex.getMessage()).thenReturn("Invalid token");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleAuthenticationException(ex);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("E_NO_AUTORIZADO", response.getBody().getCodigo());
    }

    @Test
    @DisplayName("Debe manejar AccessDeniedException retornando 403 y E_ACCESO_DENEGADO")
    void handleAccessDenied_Returns403() {
        // Arrange
        AccessDeniedException ex = new AccessDeniedException("Forbidden");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleAccessDenied(ex);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("E_ACCESO_DENEGADO", response.getBody().getCodigo());
    }

    @Test
    @DisplayName("Debe manejar excepción genérica no controlada retornando 500")
    void handleGenericException_Returns500() {
        // Arrange
        RuntimeException ex = new RuntimeException("Unexpected error");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("E_ERROR_INTERNO", response.getBody().getCodigo());
        assertEquals("Ocurrió un error interno en el servidor. Por favor intente más tarde.", response.getBody().getMensaje());
    }
}
