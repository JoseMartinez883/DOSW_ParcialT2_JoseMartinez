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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        // 1. Franja con max. 3 reservas/disponible (E01)
        long reservasEnFranja = reservaRepository.countByFechaAndHoraAndEstado(
                request.getFecha().trim(), request.getHora().trim(), "CONFIRMADA");
        if (reservasEnFranja >= 3) {
            log.warn("Rechazo por franja llena (E01): [{}] reservas activas en fecha [{}] hora [{}]",
                    reservasEnFranja, request.getFecha(), request.getHora());
            throw new BusinessException("E01", "Franja llena. Cuarta reserva en la misma fecha y hora.", HttpStatus.CONFLICT);
        }

        // 2. Barbero disponible con especialidad (E02 / E03)
        List<TurnoEntity> turnosDisponibles = turnoRepository.findByFechaAndHoraAndEstado(
                request.getFecha().trim(), request.getHora().trim(), "DISPONIBLE");
        if (turnosDisponibles.isEmpty()) {
            log.warn("Rechazo: no hay turnos disponibles para fecha [{}] y hora [{}]", request.getFecha(), request.getHora());
            throw new BusinessException("E03", "No hay turnos disponibles para la fecha y hora seleccionadas.", HttpStatus.CONFLICT);
        }

        TurnoEntity turnoSeleccionado = turnosDisponibles.get(0);
        BarberoEntity barbero = barberoRepository.findByNombreIgnoreCase(turnoSeleccionado.getBarberoNombre())
                .orElseThrow(() -> {
                    log.warn("Barbero [{}] no encontrado", turnoSeleccionado.getBarberoNombre());
                    return new BusinessException("E_BARBERO_NO_ENCONTRADO", "El barbero especificado no existe.", HttpStatus.NOT_FOUND);
                });

        if (!barbero.isActivo()) {
            log.warn("Rechazo por barbero inactivo (E03): barbero [{}]", barbero.getNombre());
            throw new BusinessException("E03", "El barbero asignado no se encuentra disponible actualmente.", HttpStatus.CONFLICT);
        }

        List<String> barberEspecialidades = barbero.getEspecialidades() != null ? barbero.getEspecialidades() : List.of();
        for (String codServicio : request.getServicios()) {
            Optional<ServicioEntity> servOpt = servicioRepository.findByCodigo(codServicio.trim());
            if (servOpt.isPresent()) {
                ServicioEntity serv = servOpt.get();
                boolean tieneEspecialidad = barberEspecialidades.stream()
                        .anyMatch(esp -> esp.equalsIgnoreCase(serv.getEspecialidadRequerida())
                                || esp.equalsIgnoreCase(serv.getCodigo())
                                || esp.equalsIgnoreCase("ALL"));
                if (!tieneEspecialidad) {
                    log.warn("Rechazo por especialidad incompatible (E02): barbero [{}] no domina [{}]",
                            barbero.getNombre(), serv.getEspecialidadRequerida());
                    throw new BusinessException("E02",
                            "El barbero seleccionado está libre, pero no tiene la especialidad necesaria para alguno de los servicios solicitados.",
                            HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }

        // 3. Servicios existen en catálogo (E09) y cálculo de total
        double total = 0.0;
        for (String codServicio : request.getServicios()) {
            ServicioEntity serv = servicioRepository.findByCodigo(codServicio.trim())
                    .orElseThrow(() -> {
                        log.warn("Rechazo por servicio no encontrado en catálogo (E09): [{}]", codServicio);
                        return new BusinessException("E09", "Se solicita un identificador de servicio que no existe en el catálogo.", HttpStatus.NOT_FOUND);
                    });
            total += serv.getPrecio();
        }

        // 4. Modificación de turno y creación de reserva (Operación atómica)
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
                .barberoAsignado(barbero.getNombre())
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

    @Override
    public List<MisReservasResponse> obtenerMisReservas(String correoCliente) {
        log.info("Consultando reservas para cliente autenticado [{}]", correoCliente);
        List<ReservaEntity> entities = reservaRepository.findByClienteCorreo(correoCliente);
        return entities.stream()
                .sorted(java.util.Comparator.comparing(ReservaEntity::getFecha, java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder()))
                        .thenComparing(ReservaEntity::getHora, java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder())))
                .map(reservaPersistenceMapper::toDomain)
                .map(reservaDtoMapper::toMisReservasResponse)
                .toList();
    }

    @Override
    public List<TodasReservasResponse> obtenerTodasLasReservas() {
        log.info("Consultando todas las reservas del sistema (rol ADMIN)");
        List<ReservaEntity> entities = reservaRepository.findAllReservas();
        return entities.stream()
                .sorted(java.util.Comparator.comparing(ReservaEntity::getFecha, java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder()))
                        .thenComparing(ReservaEntity::getHora, java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder())))
                .map(reservaPersistenceMapper::toDomain)
                .map(reservaDtoMapper::toTodasReservasResponse)
                .toList();
    }
}
