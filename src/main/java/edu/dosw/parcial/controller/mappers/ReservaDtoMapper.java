package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.response.MisReservasResponse;
import edu.dosw.parcial.controller.dtos.response.ReservaResponse;
import edu.dosw.parcial.controller.dtos.response.TodasReservasResponse;
import edu.dosw.parcial.core.models.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaDtoMapper {

    public ReservaResponse toResponse(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        return ReservaResponse.builder()
                .idReserva(reserva.getIdReserva())
                .barberoAsignado(reserva.getBarberoAsignado())
                .total(reserva.getTotal())
                .estado(reserva.getEstado() != null ? reserva.getEstado().name() : null)
                .build();
    }

    public MisReservasResponse toMisReservasResponse(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        return MisReservasResponse.builder()
                .id(reserva.getIdReserva())
                .fecha(reserva.getFecha())
                .hora(reserva.getHora())
                .estado(reserva.getEstado() != null ? reserva.getEstado().name() : null)
                .build();
    }

    public TodasReservasResponse toTodasReservasResponse(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        String nombreCliente = reserva.getCliente() != null ? reserva.getCliente().getNombre() : null;
        return TodasReservasResponse.builder()
                .id(reserva.getIdReserva())
                .cliente(nombreCliente)
                .fecha(reserva.getFecha())
                .hora(reserva.getHora())
                .build();
    }
}
