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
@Schema(description = "Datos para la creación de un nuevo turno de barbero")
public class CrearTurnoRequest {

    @NotBlank(message = "El nombre del barbero es obligatorio")
    @Schema(description = "Nombre del barbero", example = "Miguel")
    private String barberoNombre;

    @NotBlank(message = "La fecha es obligatoria")
    @Schema(description = "Fecha del turno en formato YYYY-MM-DD (debe ser futura)", example = "2026-10-25")
    private String fecha;

    @NotBlank(message = "La hora es obligatoria")
    @Schema(description = "Hora del turno en formato HH:mm (dentro del horario del barbero)", example = "10:00")
    private String hora;
}
