package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para el inicio de sesión")
public class LoginRequest {

    @NotBlank(message = "El correo no puede ser nulo o vacío")
    @Schema(description = "Correo del usuario", example = "admin@gmail.com")
    private String correo;

    @NotBlank(message = "La contraseña no puede ser nula o vacía")
    @Schema(description = "Contraseña en texto plano", example = "password123")
    private String password;
}
