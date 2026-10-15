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
@Schema(description = "Respuesta de confirmación al crear una reserva")
public class ReservaResponse {

    @Schema(description = "Identificador único de la reserva creada", example = "98a1b2c3...")
    private String idReserva;

    @Schema(description = "Nombre del barbero asignado a la reserva", example = "Miguel")
    private String barberoAsignado;

    @Schema(description = "Monto total de los servicios solicitados", example = "25000")
    private double total;

    @Schema(description = "Estado de la reserva", example = "CONFIRMADA")
    private String estado;
}
