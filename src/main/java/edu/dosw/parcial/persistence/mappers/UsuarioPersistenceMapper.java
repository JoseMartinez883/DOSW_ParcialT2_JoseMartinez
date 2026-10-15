package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.RolUsuario;
import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceMapper {

    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        return Usuario.builder()
                .id(entity.getId())
                .correo(entity.getCorreo())
                .password(entity.getPassword())
                .nombre(entity.getNombre())
                .telefono(entity.getTelefono())
                .rol(entity.getRol() != null ? RolUsuario.valueOf(entity.getRol()) : null)
                .build();
    }

    public UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) {
            return null;
        }
        return UsuarioEntity.builder()
                .id(domain.getId())
                .correo(domain.getCorreo())
                .password(domain.getPassword())
                .nombre(domain.getNombre())
                .telefono(domain.getTelefono())
                .rol(domain.getRol() != null ? domain.getRol().name() : null)
                .build();
    }
}
