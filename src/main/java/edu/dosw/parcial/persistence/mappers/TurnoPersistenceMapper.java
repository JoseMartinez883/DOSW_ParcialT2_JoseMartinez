package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.EstadoTurno;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.persistence.entities.TurnoEntity;
import org.springframework.stereotype.Component;

@Component
public class TurnoPersistenceMapper {

    public Turno toDomain(TurnoEntity entity) {
        if (entity == null) {
            return null;
        }
        return Turno.builder()
                .id(entity.getId())
                .barberoNombre(entity.getBarberoNombre())
                .fecha(entity.getFecha())
                .hora(entity.getHora())
                .estado(entity.getEstado() != null ? EstadoTurno.valueOf(entity.getEstado()) : null)
                .build();
    }

    public TurnoEntity toEntity(Turno domain) {
        if (domain == null) {
            return null;
        }
        return TurnoEntity.builder()
                .id(domain.getId())
                .barberoNombre(domain.getBarberoNombre())
                .fecha(domain.getFecha())
                .hora(domain.getHora())
                .estado(domain.getEstado() != null ? domain.getEstado().name() : null)
                .build();
    }
}
