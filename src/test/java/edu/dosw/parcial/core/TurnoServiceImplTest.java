package edu.dosw.parcial.core;

import edu.dosw.parcial.controller.dtos.request.CrearTurnoRequest;
import edu.dosw.parcial.controller.dtos.response.TurnoResponse;
import edu.dosw.parcial.controller.mappers.TurnoDtoMapper;
import edu.dosw.parcial.core.models.EstadoTurno;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.core.models.exception.BusinessException;
import edu.dosw.parcial.core.services.TurnoServiceImpl;
import edu.dosw.parcial.persistence.entities.BarberoEntity;
import edu.dosw.parcial.persistence.entities.TurnoEntity;
import edu.dosw.parcial.persistence.mappers.TurnoPersistenceMapper;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnoServiceImplTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private BarberoRepository barberoRepository;

    @Spy
    private TurnoPersistenceMapper turnoPersistenceMapper = new TurnoPersistenceMapper();

    @Spy
    private TurnoDtoMapper turnoDtoMapper = new TurnoDtoMapper();

    @InjectMocks
    private TurnoServiceImpl turnoService;

    private String futureDate;
    private BarberoEntity barberoMiguel;

    @BeforeEach
    void setUp() {
        futureDate = LocalDate.now().plusDays(5).toString();
        barberoMiguel = BarberoEntity.builder()
                .id("b1")
                .nombre("Miguel")
                .diasDisponibles(List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"))
                .horaInicio("08:00")
                .horaFin("20:00")
                .activo(true)
                .build();
    }

    @Test
    @DisplayName("Debe crear un turno exitosamente con estado DISPONIBLE")
    void crearTurno_Success_ReturnsCreatedTurnoResponse() {
        // Arrange
        CrearTurnoRequest request = CrearTurnoRequest.builder()
                .barberoNombre("Miguel")
                .fecha(futureDate)
                .hora("10:00")
                .build();

        TurnoEntity savedEntity = TurnoEntity.builder()
                .id("t123")
                .barberoNombre("Miguel")
                .fecha(futureDate)
                .hora("10:00")
                .estado("DISPONIBLE")
                .build();

        when(barberoRepository.findByNombreIgnoreCase("Miguel")).thenReturn(Optional.of(barberoMiguel));
        when(turnoRepository.existsByBarberoNombreIgnoreCaseAndFechaAndHora("Miguel", futureDate, "10:00")).thenReturn(false);
        when(turnoRepository.save(any(TurnoEntity.class))).thenReturn(savedEntity);

        // Act
        TurnoResponse response = turnoService.crearTurno(request);

        // Assert
        assertNotNull(response);
        assertEquals("t123", response.getId());
        assertEquals("Miguel", response.getBarberoNombre());
        assertEquals(futureDate, response.getFecha());
        assertEquals("10:00", response.getHora());
        assertEquals("DISPONIBLE", response.getEstado());
        verify(turnoRepository).save(any(TurnoEntity.class));
    }

    @Test
    @DisplayName("Debe rechazar con 400 si el formato de fecha no es válido")
    void crearTurno_InvalidDateFormat_ThrowsBadRequest() {
        // Arrange
        CrearTurnoRequest request = CrearTurnoRequest.builder()
                .barberoNombre("Miguel")
                .fecha("25-10-2026")
                .hora("10:00")
                .build();

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> turnoService.crearTurno(request));
        assertEquals("E_FORMATO_FECHA", ex.getCodigo());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Debe rechazar con 400 si la fecha no es futura")
    void crearTurno_PastDate_ThrowsBadRequest() {
        // Arrange
        String pastDate = LocalDate.now().minusDays(1).toString();
        CrearTurnoRequest request = CrearTurnoRequest.builder()
                .barberoNombre("Miguel")
                .fecha(pastDate)
                .hora("10:00")
                .build();

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> turnoService.crearTurno(request));
        assertEquals("E_FORMATO_FECHA", ex.getCodigo());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Debe rechazar con 400 si el formato de la hora es inválido")
    void crearTurno_InvalidTimeFormat_ThrowsBadRequest() {
        // Arrange
        CrearTurnoRequest request = CrearTurnoRequest.builder()
                .barberoNombre("Miguel")
                .fecha(futureDate)
                .hora("hora-invalida")
                .build();

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> turnoService.crearTurno(request));
        assertEquals("E_FORMATO_HORA", ex.getCodigo());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    @DisplayName("Debe rechazar con 404 si el barbero no existe")
    void crearTurno_BarberoNotFound_ThrowsNotFound() {
        // Arrange
        CrearTurnoRequest request = CrearTurnoRequest.builder()
                .barberoNombre("Inexistente")
                .fecha(futureDate)
                .hora("10:00")
                .build();

        when(barberoRepository.findByNombreIgnoreCase("Inexistente")).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> turnoService.crearTurno(request));
        assertEquals("E_BARBERO_NO_ENCONTRADO", ex.getCodigo());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    @DisplayName("Debe rechazar con 422 si la hora está fuera del horario del barbero")
    void crearTurno_TimeOutsideSchedule_ThrowsUnprocessableEntity() {
        // Arrange
        CrearTurnoRequest request = CrearTurnoRequest.builder()
                .barberoNombre("Miguel")
                .fecha(futureDate)
                .hora("06:00")
                .build();

        when(barberoRepository.findByNombreIgnoreCase("Miguel")).thenReturn(Optional.of(barberoMiguel));

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> turnoService.crearTurno(request));
        assertEquals("E_FUERA_DE_HORARIO", ex.getCodigo());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatus());
    }

    @Test
    @DisplayName("Debe rechazar con 409 si el turno ya existe para el barbero en fecha y hora")
    void crearTurno_DuplicateTurno_ThrowsConflict() {
        // Arrange
        CrearTurnoRequest request = CrearTurnoRequest.builder()
                .barberoNombre("Miguel")
                .fecha(futureDate)
                .hora("10:00")
                .build();

        when(barberoRepository.findByNombreIgnoreCase("Miguel")).thenReturn(Optional.of(barberoMiguel));
        when(turnoRepository.existsByBarberoNombreIgnoreCaseAndFechaAndHora("Miguel", futureDate, "10:00")).thenReturn(true);

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> turnoService.crearTurno(request));
        assertEquals("E_TURNO_DUPLICADO", ex.getCodigo());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }
}
