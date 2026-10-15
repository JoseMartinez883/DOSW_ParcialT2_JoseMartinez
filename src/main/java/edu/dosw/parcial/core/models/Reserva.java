package edu.dosw.parcial.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    private String idReserva;
    private Cliente cliente;
    private String fecha;
    private String hora;
    private List<String> servicios;
    private String barberoAsignado;
    private double total;
    private EstadoReserva estado;
    private LocalDateTime creadoEn;
}
