package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Nombre completo del cliente", example = "Juan Perez")
    private String nombre;

    @Schema(description = "Teléfono de contacto (exactamente 10 dígitos numéricos)", example = "3001234567")
    private String telefono;

    @Schema(description = "Correo electrónico (dominio obligatorio @gmail.com)", example = "juan@gmail.com")
    private String correo;
}
