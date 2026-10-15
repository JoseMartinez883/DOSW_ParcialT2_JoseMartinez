package edu.dosw.parcial.core;

import edu.dosw.parcial.controller.dtos.request.ClienteRequest;
import edu.dosw.parcial.controller.dtos.request.PedirTurnoRequest;
import edu.dosw.parcial.controller.dtos.response.ReservaResponse;
import edu.dosw.parcial.controller.mappers.ReservaDtoMapper;
import edu.dosw.parcial.core.models.exception.BusinessException;
import edu.dosw.parcial.core.services.ReservaServiceImpl;
import edu.dosw.parcial.core.validators.ClienteValidator;
import edu.dosw.parcial.persistence.entities.BarberoEntity;
import edu.dosw.parcial.persistence.entities.ReservaEntity;
import edu.dosw.parcial.persistence.entities.ServicioEntity;
import edu.dosw.parcial.persistence.entities.TurnoEntity;
import edu.dosw.parcial.persistence.mappers.ReservaPersistenceMapper;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.ReservaRepository;
import edu.dosw.parcial.persistence.repositories.ServicioRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private BarberoRepository barberoRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @Spy
    private ReservaPersistenceMapper reservaPersistenceMapper = new ReservaPersistenceMapper();

    @Spy
    private ReservaDtoMapper reservaDtoMapper = new ReservaDtoMapper();

    @Spy
    private ClienteValidator clienteValidator = new ClienteValidator();

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private PedirTurnoRequest validRequest;
    private TurnoEntity availableTurno;
    private BarberoEntity barberoMiguel;
    private ServicioEntity servicioCorte;

    @BeforeEach
    void setUp() {
        ClienteRequest cliente = ClienteRequest.builder()
                .nombre("Juan Perez")
                .telefono("3001234567")
                .correo("juan@gmail.com")
                .build();

        validRequest = PedirTurnoRequest.builder()
                .cliente(cliente)
                .fecha("2026-10-25")
                .hora("10:00")
                .servicios(List.of("SER-001"))
                .build();

        availableTurno = TurnoEntity.builder()
                .id("t1")
                .barberoNombre("Miguel")
                .fecha("2026-10-25")
                .hora("10:00")
                .estado("DISPONIBLE")
                .build();

        barberoMiguel = BarberoEntity.builder()
                .id("b1")
                .nombre("Miguel")
                .especialidades(List.of("CORTE", "SER-001"))
                .activo(true)
                .build();

        servicioCorte = ServicioEntity.builder()
                .id("s1")
                .codigo("SER-001")
                .nombre("Corte Clásico")
                .precio(25000.0)
                .especialidadRequerida("CORTE")
                .build();
    }

    @Test
    @DisplayName("Debe agendar exitosamente, marcando el turno como OCUPADO y confirmando la reserva")
    void pedirTurno_Success() {
        // Arrange
        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(0L);
        when(turnoRepository.findByFechaAndHoraAndEstado("2026-10-25", "10:00", "DISPONIBLE")).thenReturn(List.of(availableTurno));
        when(barberoRepository.findByNombreIgnoreCase("Miguel")).thenReturn(Optional.of(barberoMiguel));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servicioCorte));

        ReservaEntity savedReserva = ReservaEntity.builder()
                .id("r1")
                .barberoAsignado("Miguel")
                .total(25000.0)
                .estado("CONFIRMADA")
                .build();
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(savedReserva);

        // Act
        ReservaResponse response = reservaService.pedirTurno(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("r1", response.getIdReserva());
        assertEquals("Miguel", response.getBarberoAsignado());
        assertEquals(25000.0, response.getTotal());
        assertEquals("CONFIRMADA", response.getEstado());
        assertEquals("OCUPADO", availableTurno.getEstado());
        verify(turnoRepository).save(availableTurno);
        verify(reservaRepository).save(any(ReservaEntity.class));
    }

    @Test
    @DisplayName("Paso 1: Debe rechazar con E01 (409) si la franja ya tiene 3 reservas, sin tocar turno ni BD")
    void pedirTurno_FranjaLlena_ThrowsE01() {
        // Arrange
        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(3L);

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> reservaService.pedirTurno(validRequest));

        assertEquals("E01", ex.getCodigo());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        verify(turnoRepository, never()).save(any());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Paso 1: Debe rechazar con E01 (409) si no hay turnos disponibles en esa franja")
    void pedirTurno_SinTurnosDisponibles_ThrowsE01() {
        // Arrange
        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(0L);
        when(turnoRepository.findByFechaAndHoraAndEstado("2026-10-25", "10:00", "DISPONIBLE")).thenReturn(Collections.emptyList());

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> reservaService.pedirTurno(validRequest));

        assertEquals("E01", ex.getCodigo());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        verify(turnoRepository, never()).save(any());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Paso 2: Debe rechazar con E100 (409) si el barbero asignado está inactivo")
    void pedirTurno_BarberoInactivo_ThrowsE100() {
        // Arrange
        barberoMiguel.setActivo(false);
        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(0L);
        when(turnoRepository.findByFechaAndHoraAndEstado("2026-10-25", "10:00", "DISPONIBLE")).thenReturn(List.of(availableTurno));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servicioCorte));
        when(barberoRepository.findByNombreIgnoreCase("Miguel")).thenReturn(Optional.of(barberoMiguel));

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> reservaService.pedirTurno(validRequest));

        assertEquals("E100", ex.getCodigo());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        verify(turnoRepository, never()).save(any());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Paso 2: Debe rechazar con E02 (422) si el barbero no domina la especialidad del servicio pedido")
    void pedirTurno_BarberoSinEspecialidad_ThrowsE02() {
        // Arrange
        barberoMiguel.setEspecialidades(List.of("OTRA_COSA"));
        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(0L);
        when(turnoRepository.findByFechaAndHoraAndEstado("2026-10-25", "10:00", "DISPONIBLE")).thenReturn(List.of(availableTurno));
        when(barberoRepository.findByNombreIgnoreCase("Miguel")).thenReturn(Optional.of(barberoMiguel));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servicioCorte));

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> reservaService.pedirTurno(validRequest));

        assertEquals("E02", ex.getCodigo());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatus());
        assertEquals("DISPONIBLE", availableTurno.getEstado());
        verify(turnoRepository, never()).save(any());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Paso 3: Debe rechazar con E09 (404) si el servicio solicitado no existe en el catálogo")
    void pedirTurno_ServicioInexistente_ThrowsE09() {
        // Arrange
        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(0L);
        when(turnoRepository.findByFechaAndHoraAndEstado("2026-10-25", "10:00", "DISPONIBLE")).thenReturn(List.of(availableTurno));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> reservaService.pedirTurno(validRequest));

        assertEquals("E09", ex.getCodigo());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        verify(turnoRepository, never()).save(any());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe consultar únicamente las reservas del cliente autenticado ordenadas por fecha y hora descendente")
    void obtenerMisReservas_ReturnsListOfClientReservations() {
        // Arrange
        String correo = "juan@gmail.com";
        ReservaEntity r1 = ReservaEntity.builder()
                .id("r1")
                .cliente(ReservaEntity.ClienteEmbedded.builder().nombre("Juan").correo(correo).telefono("3001234567").build())
                .fecha("2026-10-25")
                .hora("10:00")
                .estado("CONFIRMADA")
                .build();

        when(reservaRepository.findByClienteCorreo(correo)).thenReturn(List.of(r1));

        // Act
        List<edu.dosw.parcial.controller.dtos.response.MisReservasResponse> resultado = reservaService.obtenerMisReservas(correo);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("r1", resultado.get(0).getId());
        assertEquals("2026-10-25", resultado.get(0).getFecha());
        assertEquals("10:00", resultado.get(0).getHora());
        assertEquals("CONFIRMADA", resultado.get(0).getEstado());
        verify(reservaRepository).findByClienteCorreo(correo);
    }

    @Test
    @DisplayName("Debe consultar todas las reservas de la barbería ordenadas por fecha y hora descendente")
    void obtenerTodasLasReservas_ReturnsListOfAllReservations() {
        // Arrange
        ReservaEntity r1 = ReservaEntity.builder()
                .id("r1")
                .cliente(ReservaEntity.ClienteEmbedded.builder().nombre("Juan").correo("juan@gmail.com").telefono("3001234567").build())
                .fecha("2026-10-25")
                .hora("10:00")
                .estado("CONFIRMADA")
                .build();

        ReservaEntity r2 = ReservaEntity.builder()
                .id("r2")
                .cliente(ReservaEntity.ClienteEmbedded.builder().nombre("Pedro").correo("pedro@gmail.com").telefono("3111234567").build())
                .fecha("2026-10-24")
                .hora("11:00")
                .estado("CONFIRMADA")
                .build();

        when(reservaRepository.findAllReservas()).thenReturn(List.of(r1, r2));

        // Act
        List<edu.dosw.parcial.controller.dtos.response.TodasReservasResponse> resultado = reservaService.obtenerTodasLasReservas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("r1", resultado.get(0).getId());
        assertEquals("Juan", resultado.get(0).getCliente());
        assertEquals("r2", resultado.get(1).getId());
        assertEquals("Pedro", resultado.get(1).getCliente());
        verify(reservaRepository).findAllReservas();
    }

    @Test
    @DisplayName("Debe asignar dinámicamente el barbero con la especialidad requerida si hay varios turnos disponibles en la franja")
    void pedirTurno_MultiplesTurnos_AsignaBarberoConEspecialidad() {
        // Arrange: 2 turnos disponibles en la misma franja (Laura sin CORTE y Miguel con CORTE)
        TurnoEntity turnoLaura = TurnoEntity.builder()
                .id("t-laura")
                .barberoNombre("Laura")
                .fecha("2026-10-25")
                .hora("10:00")
                .estado("DISPONIBLE")
                .build();

        TurnoEntity turnoMiguel = TurnoEntity.builder()
                .id("t-miguel")
                .barberoNombre("Miguel")
                .fecha("2026-10-25")
                .hora("10:00")
                .estado("DISPONIBLE")
                .build();

        BarberoEntity barberoLaura = BarberoEntity.builder()
                .id("b-laura")
                .nombre("Laura")
                .especialidades(List.of("CEJAS", "CAPILAR"))
                .activo(true)
                .build();

        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(0L);
        when(turnoRepository.findByFechaAndHoraAndEstado("2026-10-25", "10:00", "DISPONIBLE")).thenReturn(List.of(turnoLaura, turnoMiguel));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servicioCorte));
        when(barberoRepository.findByNombreIgnoreCase("Laura")).thenReturn(Optional.of(barberoLaura));
        when(barberoRepository.findByNombreIgnoreCase("Miguel")).thenReturn(Optional.of(barberoMiguel));

        ReservaEntity savedReserva = ReservaEntity.builder()
                .id("r-dinamica")
                .barberoAsignado("Miguel")
                .total(25000.0)
                .estado("CONFIRMADA")
                .build();
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(savedReserva);

        // Act
        ReservaResponse response = reservaService.pedirTurno(validRequest);

        // Assert: Verifica que asignó a Miguel (que tiene CORTE) y no a Laura
        assertNotNull(response);
        assertEquals("Miguel", response.getBarberoAsignado());
        assertEquals("OCUPADO", turnoMiguel.getEstado());
        assertEquals("DISPONIBLE", turnoLaura.getEstado());
        verify(turnoRepository).save(turnoMiguel);
        verify(turnoRepository, never()).save(turnoLaura);
    }

    @Test
    @DisplayName("Debe priorizar E01 (franja llena) sobre E03 cuando la franja está al límite y el nombre del cliente viene vacío")
    void pedirTurno_FranjaLlenaYNombreVacio_PriorizaE01SobreE03() {
        // Arrange
        validRequest.getCliente().setNombre("   "); // Nombre inválido
        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(3L);

        // Act y Assert: Debe fallar en la Validación 1 (E01) y no en la Validación 3 (E03)
        BusinessException ex = assertThrows(BusinessException.class, () -> reservaService.pedirTurno(validRequest));
        assertEquals("E01", ex.getCodigo());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        verify(reservaRepository, never()).save(any());
        verify(turnoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe priorizar E02 (especialidad incompatible) sobre E07 cuando el barbero no domina el servicio y el teléfono es inválido")
    void pedirTurno_BarberoSinEspecialidadYTelefonoInvalido_PriorizaE02SobreE07() {
        // Arrange: Teléfono con 5 dígitos (inválido), barbero no tiene CORTE
        validRequest.getCliente().setTelefono("12345");
        BarberoEntity barberoSinCorte = BarberoEntity.builder()
                .id("b2")
                .nombre("Laura")
                .especialidades(List.of("CEJAS", "CAPILAR"))
                .activo(true)
                .build();
        TurnoEntity turno = TurnoEntity.builder()
                .id("t2")
                .barberoNombre("Laura")
                .fecha("2026-10-25")
                .hora("10:00")
                .estado("DISPONIBLE")
                .build();

        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(0L);
        when(turnoRepository.findByFechaAndHoraAndEstado("2026-10-25", "10:00", "DISPONIBLE")).thenReturn(List.of(turno));
        when(barberoRepository.findByNombreIgnoreCase("Laura")).thenReturn(Optional.of(barberoSinCorte));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servicioCorte));

        // Act y Assert: Debe fallar en Validación 2 (E02) antes de evaluar datos del cliente (E07)
        BusinessException ex = assertThrows(BusinessException.class, () -> reservaService.pedirTurno(validRequest));
        assertEquals("E02", ex.getCodigo());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatus());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar con E03 cuando la franja y barbero son válidos pero el nombre es vacío o espacios")
    void pedirTurno_ValidacionesPasanPeroNombreVacio_ThrowsE03() {
        // Arrange
        validRequest.getCliente().setNombre("   ");
        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(0L);
        when(turnoRepository.findByFechaAndHoraAndEstado("2026-10-25", "10:00", "DISPONIBLE")).thenReturn(List.of(availableTurno));
        when(barberoRepository.findByNombreIgnoreCase("Miguel")).thenReturn(Optional.of(barberoMiguel));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servicioCorte));

        // Act y Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> reservaService.pedirTurno(validRequest));
        assertEquals("E03", ex.getCodigo());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        verify(reservaRepository, never()).save(any());
        verify(turnoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar con E07 cuando la franja y barbero son válidos pero el teléfono no tiene 10 dígitos")
    void pedirTurno_ValidacionesPasanPeroTelefonoInvalido_ThrowsE07() {
        // Arrange
        validRequest.getCliente().setTelefono("300123");
        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(0L);
        when(turnoRepository.findByFechaAndHoraAndEstado("2026-10-25", "10:00", "DISPONIBLE")).thenReturn(List.of(availableTurno));
        when(barberoRepository.findByNombreIgnoreCase("Miguel")).thenReturn(Optional.of(barberoMiguel));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servicioCorte));

        // Act y Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> reservaService.pedirTurno(validRequest));
        assertEquals("E07", ex.getCodigo());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        verify(reservaRepository, never()).save(any());
        verify(turnoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar con E08 cuando la franja y barbero son válidos pero el correo no es @gmail.com")
    void pedirTurno_ValidacionesPasanPeroCorreoInvalido_ThrowsE08() {
        // Arrange
        validRequest.getCliente().setCorreo("juan@hotmail.com");
        when(reservaRepository.countByFechaAndHoraAndEstado("2026-10-25", "10:00", "CONFIRMADA")).thenReturn(0L);
        when(turnoRepository.findByFechaAndHoraAndEstado("2026-10-25", "10:00", "DISPONIBLE")).thenReturn(List.of(availableTurno));
        when(barberoRepository.findByNombreIgnoreCase("Miguel")).thenReturn(Optional.of(barberoMiguel));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servicioCorte));

        // Act y Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> reservaService.pedirTurno(validRequest));
        assertEquals("E08", ex.getCodigo());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        verify(reservaRepository, never()).save(any());
        verify(turnoRepository, never()).save(any());
    }
}
