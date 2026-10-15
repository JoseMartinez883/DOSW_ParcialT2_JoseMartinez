package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de entrada para solicitar una reserva de turno")
public class PedirTurnoRequest {

    @Schema(description = "Información del cliente que solicita")
    private ClienteRequest cliente;

    @NotBlank(message = "La fecha es obligatoria")
    @Schema(description = "Fecha deseada en formato YYYY-MM-DD", example = "2026-10-25")
    private String fecha;

    @NotBlank(message = "La hora es obligatoria")
    @Schema(description = "Hora deseada en formato HH:mm", example = "10:00")
    private String hora;

    @NotEmpty(message = "Debe especificar al menos un código de servicio")
    @Schema(description = "Lista de identificadores de servicios del catálogo", example = "[\"SER-001\"]")
    private List<String> servicios;
}
