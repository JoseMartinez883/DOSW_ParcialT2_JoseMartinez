package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.response.TurnoResponse;
import edu.dosw.parcial.core.models.Turno;
import org.springframework.stereotype.Component;

@Component
public class TurnoDtoMapper {

    public TurnoResponse toResponse(Turno turno) {
        if (turno == null) {
            return null;
        }
        return TurnoResponse.builder()
                .id(turno.getId())
                .barberoNombre(turno.getBarberoNombre())
                .fecha(turno.getFecha())
                .hora(turno.getHora())
                .estado(turno.getEstado() != null ? turno.getEstado().name() : null)
                .build();
    }
}
