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
@Schema(description = "Detalle del turno registrado en el sistema")
public class TurnoResponse {

    @Schema(description = "Identificador único del turno", example = "65b2fa81...")
    private String id;

    @Schema(description = "Nombre del barbero asignado", example = "Miguel")
    private String barberoNombre;

    @Schema(description = "Fecha del turno", example = "2026-10-25")
    private String fecha;

    @Schema(description = "Hora del turno", example = "10:00")
    private String hora;

    @Schema(description = "Estado del turno (DISPONIBLE / OCUPADO)", example = "DISPONIBLE")
    private String estado;
}
