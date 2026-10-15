package edu.dosw.parcial.controller.handlers;

import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.core.models.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("Excepción de negocio [{}]: {}", ex.getCodigo(), ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .codigo(ex.getCodigo())
                .mensaje(ex.getMessage())
                .estado(ex.getStatus().value())
                .build();
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String field = fieldError != null ? fieldError.getField() : "desconocido";
        String defaultMsg = fieldError != null ? fieldError.getDefaultMessage() : "Datos de entrada inválidos";

        String codigo = "E_VALIDACION_ENTRADA";
        String mensaje = defaultMsg;

        if (field.contains("nombre")) {
            codigo = "E03";
            mensaje = "El nombre es nulo, vacío o contiene únicamente espacios.";
        } else if (field.contains("telefono")) {
            codigo = "E07";
            mensaje = "El teléfono no puede ser nulo y debe tener exactamente 10 dígitos.";
        } else if (field.contains("correo")) {
            codigo = "E08";
            mensaje = "El correo no puede ser nulo, debe tener formato de email y terminar obligatoriamente en @gmail.com.";
        } else if (field.contains("fecha")) {
            codigo = "E_FORMATO_FECHA";
            mensaje = "La fecha debe tener un formato válido y ser futura.";
        } else if (field.contains("hora")) {
            codigo = "E_FORMATO_HORA";
            mensaje = "La hora debe tener un formato válido (HH:mm).";
        }

        log.warn("Fallo de validación de entrada en campo [{}]: {} ({})", field, mensaje, codigo);

        ErrorResponse error = ErrorResponse.builder()
                .codigo(codigo)
                .mensaje(mensaje)
                .estado(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Fallo de autenticación: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .codigo("E_CREDENCIALES_INVALIDAS")
                .mensaje("Correo o contraseña incorrectos.")
                .estado(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Error de autenticación: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .codigo("E_NO_AUTORIZADO")
                .mensaje("Token de sesión ausente, inválido o expirado.")
                .estado(HttpStatus.UNAUTHORIZED.value())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Acceso denegado: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .codigo("E_ACCESO_DENEGADO")
                .mensaje("Rol de usuario no tiene permisos para esta acción.")
                .estado(HttpStatus.FORBIDDEN.value())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(org.springframework.dao.DuplicateKeyException ex) {
        log.warn("Conflicto de clave duplicada en base de datos: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .codigo("E_CONFLICTO_DUPLICADO")
                .mensaje("El recurso ya existe en el sistema o genera un conflicto de clave única.")
                .estado(HttpStatus.CONFLICT.value())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error no controlado en el servidor: ", ex);
        String detalle = ex.getClass().getName() + ": " + ex.getMessage();
        ErrorResponse error = ErrorResponse.builder()
                .codigo("E_ERROR_INTERNO")
                .mensaje(detalle)
                .estado(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
