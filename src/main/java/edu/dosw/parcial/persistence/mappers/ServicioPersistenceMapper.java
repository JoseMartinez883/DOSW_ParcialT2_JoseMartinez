package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.ServicioCatalogo;
import edu.dosw.parcial.persistence.entities.ServicioEntity;
import org.springframework.stereotype.Component;

@Component
public class ServicioPersistenceMapper {

    public ServicioCatalogo toDomain(ServicioEntity entity) {
        if (entity == null) {
            return null;
        }
        return ServicioCatalogo.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .precio(entity.getPrecio())
                .especialidadRequerida(entity.getEspecialidadRequerida())
                .build();
    }

    public ServicioEntity toEntity(ServicioCatalogo domain) {
        if (domain == null) {
            return null;
        }
        return ServicioEntity.builder()
                .id(domain.getId())
                .codigo(domain.getCodigo())
                .nombre(domain.getNombre())
                .precio(domain.getPrecio())
                .especialidadRequerida(domain.getEspecialidadRequerida())
                .build();
    }
}
