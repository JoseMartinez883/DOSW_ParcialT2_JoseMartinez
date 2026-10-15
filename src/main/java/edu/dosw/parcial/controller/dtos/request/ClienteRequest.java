package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de contacto del cliente que solicita la reserva")
public class ClienteRequest {

    @NotBlank(message = "El nombre es nulo, vacío o contiene únicamente espacios.")
    @Schema(description = "Nombre completo del cliente", example = "Juan Perez")
    private String nombre;

    @NotNull(message = "El teléfono no puede ser nulo y debe tener exactamente 10 dígitos.")
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono no puede ser nulo y debe tener exactamente 10 dígitos.")
    @Schema(description = "Teléfono de contacto (exactamente 10 dígitos)", example = "3001234567")
    private String telefono;

    @NotNull(message = "El correo no puede ser nulo, debe tener formato de email y terminar obligatoriamente en @gmail.com.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "El correo no puede ser nulo, debe tener formato de email y terminar obligatoriamente en @gmail.com.")
    @Schema(description = "Correo electrónico (dominio obligatorio @gmail.com)", example = "juan@gmail.com")
    private String correo;
}
