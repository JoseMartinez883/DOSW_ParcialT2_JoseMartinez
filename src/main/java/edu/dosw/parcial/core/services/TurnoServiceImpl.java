package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.request.CrearTurnoRequest;
import edu.dosw.parcial.controller.dtos.response.TurnoResponse;
import edu.dosw.parcial.controller.mappers.TurnoDtoMapper;
import edu.dosw.parcial.core.models.EstadoTurno;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.core.models.exception.BusinessException;
import edu.dosw.parcial.persistence.entities.BarberoEntity;
import edu.dosw.parcial.persistence.entities.TurnoEntity;
import edu.dosw.parcial.persistence.mappers.TurnoPersistenceMapper;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@Service
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;
    private final BarberoRepository barberoRepository;
    private final TurnoPersistenceMapper turnoPersistenceMapper;
    private final TurnoDtoMapper turnoDtoMapper;

    public TurnoServiceImpl(
            TurnoRepository turnoRepository,
            BarberoRepository barberoRepository,
            TurnoPersistenceMapper turnoPersistenceMapper,
            TurnoDtoMapper turnoDtoMapper) {
        this.turnoRepository = turnoRepository;
        this.barberoRepository = barberoRepository;
        this.turnoPersistenceMapper = turnoPersistenceMapper;
        this.turnoDtoMapper = turnoDtoMapper;
    }

    @Override
    public TurnoResponse crearTurno(CrearTurnoRequest request) {
        log.info("Iniciando creación de turno para barbero [{}] en fecha [{}] y hora [{}]",
                request.getBarberoNombre(), request.getFecha(), request.getHora());

        // 1. Validación de formato de fecha y ser futura
        LocalDate date;
        try {
            date = LocalDate.parse(request.getFecha().trim());
        } catch (DateTimeParseException e) {
            log.warn("Formato de fecha inválido: [{}]", request.getFecha());
            throw new BusinessException("E_FORMATO_FECHA", "La fecha debe tener un formato válido y ser futura.", HttpStatus.BAD_REQUEST);
        }

        if (!date.isAfter(LocalDate.now())) {
            log.warn("La fecha [{}] no es futura con respecto a hoy [{}]", date, LocalDate.now());
            throw new BusinessException("E_FORMATO_FECHA", "La fecha debe tener un formato válido y ser futura.", HttpStatus.BAD_REQUEST);
        }

        // 2. Validación de formato de hora
        LocalTime time;
        try {
            time = LocalTime.parse(request.getHora().trim());
        } catch (DateTimeParseException e) {
            log.warn("Formato de hora inválido: [{}]", request.getHora());
            throw new BusinessException("E_FORMATO_HORA", "La hora debe tener un formato válido (HH:mm).", HttpStatus.BAD_REQUEST);
        }

        // 3. Validación de existencia del barbero
        BarberoEntity barbero = barberoRepository.findByNombreIgnoreCase(request.getBarberoNombre().trim())
                .orElseThrow(() -> {
                    log.warn("Barbero [{}] no existe en la base de datos", request.getBarberoNombre());
                    return new BusinessException("E_BARBERO_NO_ENCONTRADO", "El barbero especificado no existe.", HttpStatus.NOT_FOUND);
                });

        // 4. Validación de día y horario de atención del barbero
        String dayOfWeek = date.getDayOfWeek().name();
        List<String> diasDisponibles = barbero.getDiasDisponibles();
        if (diasDisponibles != null && !diasDisponibles.isEmpty()) {
            boolean diaValido = diasDisponibles.stream()
                    .anyMatch(d -> d.equalsIgnoreCase(dayOfWeek) || d.equalsIgnoreCase("ALL"));
            if (!diaValido) {
                log.warn("El barbero [{}] no labora el día [{}]", barbero.getNombre(), dayOfWeek);
                throw new BusinessException("E_FUERA_DE_HORARIO", "La fecha u hora no se encuentra dentro de la disponibilidad del barbero.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        if (barbero.getHoraInicio() != null && barbero.getHoraFin() != null) {
            LocalTime inicio = LocalTime.parse(barbero.getHoraInicio());
            LocalTime fin = LocalTime.parse(barbero.getHoraFin());
            if (time.isBefore(inicio) || time.isAfter(fin)) {
                log.warn("La hora [{}] está fuera del horario del barbero [{} - {}]", time, inicio, fin);
                throw new BusinessException("E_FUERA_DE_HORARIO", "La fecha u hora no se encuentra dentro de la disponibilidad del barbero.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        // 5. Validación de turno no duplicado (barbero + fecha + hora)
        if (turnoRepository.existsByBarberoNombreIgnoreCaseAndFechaAndHora(barbero.getNombre(), request.getFecha().trim(), request.getHora().trim())) {
            log.warn("Turno duplicado para barbero [{}] en fecha [{}] y hora [{}]", barbero.getNombre(), request.getFecha(), request.getHora());
            throw new BusinessException("E_TURNO_DUPLICADO", "No se duplica barbero + fecha + hora.", HttpStatus.CONFLICT);
        }

        // 6. Creación y persistencia del turno (nace DISPONIBLE)
        Turno nuevoTurno = Turno.builder()
                .barberoNombre(barbero.getNombre())
                .fecha(request.getFecha().trim())
                .hora(request.getHora().trim())
                .estado(EstadoTurno.DISPONIBLE)
                .build();

        TurnoEntity entityToSave = turnoPersistenceMapper.toEntity(nuevoTurno);
        TurnoEntity savedEntity = turnoRepository.save(entityToSave);
        Turno turnoGuardado = turnoPersistenceMapper.toDomain(savedEntity);

        log.info("Turno creado satisfactoriamente: id=[{}], barbero=[{}], fecha=[{}], hora=[{}], estado=[{}]",
                turnoGuardado.getId(), turnoGuardado.getBarberoNombre(), turnoGuardado.getFecha(), turnoGuardado.getHora(), turnoGuardado.getEstado());

        return turnoDtoMapper.toResponse(turnoGuardado);
    }
}
