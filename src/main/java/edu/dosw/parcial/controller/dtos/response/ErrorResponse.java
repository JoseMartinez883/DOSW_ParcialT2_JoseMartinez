package edu.dosw.parcial.controller.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura estándar de respuesta de error")
public class ErrorResponse {

    @Schema(description = "Código funcional de error", example = "E01")
    private String codigo;

    @Schema(description = "Mensaje explicativo del error", example = "Franja llena. Cuarta reserva en la misma fecha y hora.")
    private String mensaje;

    @Schema(description = "Código de estado HTTP", example = "409")
    private int estado;
}
