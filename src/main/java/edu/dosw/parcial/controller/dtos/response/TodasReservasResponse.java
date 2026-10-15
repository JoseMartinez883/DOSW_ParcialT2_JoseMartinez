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
@Schema(description = "Detalle de reserva para consulta administrativa")
public class TodasReservasResponse {

    @Schema(description = "Identificador de la reserva", example = "98a1b2c3...")
    private String id;

    @Schema(description = "Nombre del cliente", example = "Juan")
    private String cliente;

    @Schema(description = "Fecha de la reserva", example = "2026-10-25")
    private String fecha;

    @Schema(description = "Hora de la reserva", example = "10:00")
    private String hora;
}
