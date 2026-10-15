package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.request.PedirTurnoRequest;
import edu.dosw.parcial.controller.dtos.response.MisReservasResponse;
import edu.dosw.parcial.controller.dtos.response.ReservaResponse;
import edu.dosw.parcial.controller.dtos.response.TodasReservasResponse;
import edu.dosw.parcial.controller.mappers.ReservaDtoMapper;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.exception.BusinessException;
import edu.dosw.parcial.persistence.entities.BarberoEntity;
import edu.dosw.parcial.persistence.entities.ReservaEntity;
import edu.dosw.parcial.persistence.entities.ServicioEntity;
import edu.dosw.parcial.persistence.entities.TurnoEntity;
import edu.dosw.parcial.persistence.mappers.ReservaPersistenceMapper;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.ReservaRepository;
import edu.dosw.parcial.persistence.repositories.ServicioRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final TurnoRepository turnoRepository;
    private final BarberoRepository barberoRepository;
    private final ServicioRepository servicioRepository;
    private final ReservaPersistenceMapper reservaPersistenceMapper;
    private final ReservaDtoMapper reservaDtoMapper;

    public ReservaServiceImpl(
            ReservaRepository reservaRepository,
            TurnoRepository turnoRepository,
            BarberoRepository barberoRepository,
            ServicioRepository servicioRepository,
            ReservaPersistenceMapper reservaPersistenceMapper,
            ReservaDtoMapper reservaDtoMapper) {
        this.reservaRepository = reservaRepository;
        this.turnoRepository = turnoRepository;
        this.barberoRepository = barberoRepository;
        this.servicioRepository = servicioRepository;
        this.reservaPersistenceMapper = reservaPersistenceMapper;
        this.reservaDtoMapper = reservaDtoMapper;
    }

    @Override
    public ReservaResponse pedirTurno(PedirTurnoRequest request) {
        log.info("Iniciando solicitud de reserva para cliente [{}] en fecha [{}] y hora [{}]",
                request.getCliente().getNombre(), request.getFecha(), request.getHora());

        validateSlotCapacity(request.getFecha(), request.getHora());

        List<TurnoEntity> turnosDisponibles = turnoRepository.findByFechaAndHoraAndEstado(
                request.getFecha().trim(), request.getHora().trim(), "DISPONIBLE");

        if (turnosDisponibles.isEmpty()) {
            log.warn("Rechazo por disponibilidad de franja (E01): no hay turnos disponibles para fecha [{}] y hora [{}]",
                    request.getFecha(), request.getHora());
            throw new BusinessException("E01", "No hay turnos disponibles para la fecha y hora seleccionadas.", HttpStatus.CONFLICT);
        }

        double total = calculateTotalAndValidateCatalog(request.getServicios());

        TurnoEntity turnoSeleccionado = null;
        BarberoEntity barberoSeleccionado = null;
        boolean hayBarberoInactivo = false;

        for (TurnoEntity turno : turnosDisponibles) {
            Optional<BarberoEntity> barberoOpt = barberoRepository.findByNombreIgnoreCase(turno.getBarberoNombre());
            if (barberoOpt.isEmpty()) {
                continue;
            }
            BarberoEntity barbero = barberoOpt.get();
            if (!barbero.isActivo()) {
                hayBarberoInactivo = true;
                continue;
            }
            if (barberoHasSpecialties(barbero, request.getServicios())) {
                turnoSeleccionado = turno;
                barberoSeleccionado = barbero;
                break;
            }
        }

        if (turnoSeleccionado == null) {
            if (hayBarberoInactivo) {
                log.warn("Rechazo por barbero inactivo (E100)");
                throw new BusinessException("E100", "El barbero asignado no se encuentra disponible actualmente.", HttpStatus.CONFLICT);
            }
            log.warn("Rechazo por especialidad incompatible (E02): ningún barbero libre domina los servicios solicitados");
            throw new BusinessException("E02",
                    "El barbero seleccionado está libre, pero no tiene la especialidad necesaria para alguno de los servicios solicitados.",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        turnoSeleccionado.setEstado("OCUPADO");
        turnoRepository.save(turnoSeleccionado);

        ReservaEntity reservaEntity = ReservaEntity.builder()
                .cliente(ReservaEntity.ClienteEmbedded.builder()
                        .nombre(request.getCliente().getNombre().trim())
                        .telefono(request.getCliente().getTelefono().trim())
                        .correo(request.getCliente().getCorreo().trim())
                        .build())
                .fecha(request.getFecha().trim())
                .hora(request.getHora().trim())
                .servicios(request.getServicios())
                .barberoAsignado(barberoSeleccionado.getNombre())
                .total(total)
                .estado("CONFIRMADA")
                .creadoEn(LocalDateTime.now())
                .build();

        ReservaEntity savedEntity = reservaRepository.save(reservaEntity);
        Reserva reservaGuardada = reservaPersistenceMapper.toDomain(savedEntity);

        log.info("Reserva creada y confirmada exitosamente: id=[{}], barbero=[{}], total=[{}]",
                reservaGuardada.getIdReserva(), reservaGuardada.getBarberoAsignado(), reservaGuardada.getTotal());

        return reservaDtoMapper.toResponse(reservaGuardada);
    }

    private void validateSlotCapacity(String fecha, String hora) {
        long reservasEnFranja = reservaRepository.countByFechaAndHoraAndEstado(fecha.trim(), hora.trim(), "CONFIRMADA");
        if (reservasEnFranja >= 3) {
            log.warn("Rechazo por franja llena (E01): [{}] reservas activas en fecha [{}] hora [{}]",
                    reservasEnFranja, fecha, hora);
            throw new BusinessException("E01", "Franja llena. Cuarta reserva en la misma fecha y hora.", HttpStatus.CONFLICT);
        }
    }

    private boolean barberoHasSpecialties(BarberoEntity barbero, Collection<String> codigosServicios) {
        List<String> especialidades = barbero.getEspecialidades() != null ? barbero.getEspecialidades() : List.of();
        for (String codigo : codigosServicios) {
            Optional<ServicioEntity> servicioOpt = servicioRepository.findByCodigo(codigo.trim());
            if (servicioOpt.isEmpty()) {
                continue;
            }
            ServicioEntity servicio = servicioOpt.get();
            boolean matchesSpecialty = especialidades.stream()
                    .anyMatch(esp -> esp.equalsIgnoreCase(servicio.getEspecialidadRequerida())
                            || esp.equalsIgnoreCase(servicio.getCodigo())
                            || esp.equalsIgnoreCase("ALL"));
            if (!matchesSpecialty) {
                return false;
            }
        }
        return true;
    }

    private double calculateTotalAndValidateCatalog(Collection<String> codigosServicios) {
        double total = 0.0;
        for (String codigo : codigosServicios) {
            ServicioEntity servicio = servicioRepository.findByCodigo(codigo.trim())
                    .orElseThrow(() -> {
                        log.warn("Rechazo por servicio no encontrado en catálogo (E09): [{}]", codigo);
                        return new BusinessException("E09", "Se solicita un identificador de servicio que no existe en el catálogo.", HttpStatus.NOT_FOUND);
                    });
            total += servicio.getPrecio();
        }
        return total;
    }

    @Override
    public List<MisReservasResponse> obtenerMisReservas(String correoCliente) {
        log.info("Consultando reservas para cliente autenticado [{}]", correoCliente);
        List<ReservaEntity> entities = reservaRepository.findByClienteCorreo(correoCliente);
        return entities.stream()
                .sorted(Comparator.comparing(ReservaEntity::getFecha, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(ReservaEntity::getHora, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(reservaPersistenceMapper::toDomain)
                .map(reservaDtoMapper::toMisReservasResponse)
                .toList();
    }

    @Override
    public List<TodasReservasResponse> obtenerTodasLasReservas() {
        log.info("Consultando todas las reservas del sistema (rol ADMIN)");
        List<ReservaEntity> entities = reservaRepository.findAllReservas();
        return entities.stream()
                .sorted(Comparator.comparing(ReservaEntity::getFecha, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(ReservaEntity::getHora, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(reservaPersistenceMapper::toDomain)
                .map(reservaDtoMapper::toTodasReservasResponse)
                .toList();
    }
}
