package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Cliente;
import edu.dosw.parcial.core.models.EstadoReserva;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.persistence.entities.ReservaEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservaPersistenceMapper {

    public Reserva toDomain(ReservaEntity entity) {
        if (entity == null) {
            return null;
        }
        Cliente cliente = null;
        if (entity.getCliente() != null) {
            cliente = Cliente.builder()
                    .nombre(entity.getCliente().getNombre())
                    .telefono(entity.getCliente().getTelefono())
                    .correo(entity.getCliente().getCorreo())
                    .build();
        }

        return Reserva.builder()
                .idReserva(entity.getId())
                .cliente(cliente)
                .fecha(entity.getFecha())
                .hora(entity.getHora())
                .servicios(entity.getServicios())
                .barberoAsignado(entity.getBarberoAsignado())
                .total(entity.getTotal())
                .estado(entity.getEstado() != null ? EstadoReserva.valueOf(entity.getEstado()) : null)
                .creadoEn(entity.getCreadoEn())
                .build();
    }

    public ReservaEntity toEntity(Reserva domain) {
        if (domain == null) {
            return null;
        }
        ReservaEntity.ClienteEmbedded cliente = null;
        if (domain.getCliente() != null) {
            cliente = ReservaEntity.ClienteEmbedded.builder()
                    .nombre(domain.getCliente().getNombre())
                    .telefono(domain.getCliente().getTelefono())
                    .correo(domain.getCliente().getCorreo())
                    .build();
        }

        return ReservaEntity.builder()
                .id(domain.getIdReserva())
                .cliente(cliente)
                .fecha(domain.getFecha())
                .hora(domain.getHora())
                .servicios(domain.getServicios())
                .barberoAsignado(domain.getBarberoAsignado())
                .total(domain.getTotal())
                .estado(domain.getEstado() != null ? domain.getEstado().name() : null)
                .creadoEn(domain.getCreadoEn())
                .build();
    }
}
