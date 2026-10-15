package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Barbero;
import edu.dosw.parcial.persistence.entities.BarberoEntity;
import org.springframework.stereotype.Component;

@Component
public class BarberoPersistenceMapper {

    public Barbero toDomain(BarberoEntity entity) {
        if (entity == null) {
            return null;
        }
        return Barbero.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .especialidades(entity.getEspecialidades())
                .diasDisponibles(entity.getDiasDisponibles())
                .horaInicio(entity.getHoraInicio())
                .horaFin(entity.getHoraFin())
                .activo(entity.isActivo())
                .build();
    }

    public BarberoEntity toEntity(Barbero domain) {
        if (domain == null) {
            return null;
        }
        return BarberoEntity.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .especialidades(domain.getEspecialidades())
                .diasDisponibles(domain.getDiasDisponibles())
                .horaInicio(domain.getHoraInicio())
                .horaFin(domain.getHoraFin())
                .activo(domain.isActivo())
                .build();
    }
}
