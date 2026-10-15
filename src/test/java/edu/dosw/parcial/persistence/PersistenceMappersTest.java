package edu.dosw.parcial.persistence;

import edu.dosw.parcial.core.models.Barbero;
import edu.dosw.parcial.core.models.Cliente;
import edu.dosw.parcial.core.models.EstadoReserva;
import edu.dosw.parcial.core.models.EstadoTurno;
import edu.dosw.parcial.core.models.RolUsuario;
import edu.dosw.parcial.core.models.ServicioCatalogo;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.persistence.entities.BarberoEntity;
import edu.dosw.parcial.persistence.entities.ReservaEntity;
import edu.dosw.parcial.persistence.entities.ServicioEntity;
import edu.dosw.parcial.persistence.entities.TurnoEntity;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.mappers.BarberoPersistenceMapper;
import edu.dosw.parcial.persistence.mappers.ReservaPersistenceMapper;
import edu.dosw.parcial.persistence.mappers.ServicioPersistenceMapper;
import edu.dosw.parcial.persistence.mappers.TurnoPersistenceMapper;
import edu.dosw.parcial.persistence.mappers.UsuarioPersistenceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersistenceMappersTest {

    private final UsuarioPersistenceMapper usuarioMapper = new UsuarioPersistenceMapper();
    private final BarberoPersistenceMapper barberoMapper = new BarberoPersistenceMapper();
    private final ServicioPersistenceMapper servicioMapper = new ServicioPersistenceMapper();
    private final TurnoPersistenceMapper turnoMapper = new TurnoPersistenceMapper();
    private final ReservaPersistenceMapper reservaMapper = new ReservaPersistenceMapper();

    @Test
    @DisplayName("Debe mapear UsuarioEntity a Usuario y viceversa")
    void testUsuarioMapper() {
        assertNull(usuarioMapper.toDomain(null));
        assertNull(usuarioMapper.toEntity(null));

        UsuarioEntity entity = UsuarioEntity.builder()
                .id("u1")
                .correo("admin@gmail.com")
                .password("pwd")
                .nombre("Admin")
                .telefono("1234567890")
                .rol("ADMIN")
                .build();

        Usuario domain = usuarioMapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals("u1", domain.getId());
        assertEquals("admin@gmail.com", domain.getCorreo());
        assertEquals(RolUsuario.ADMIN, domain.getRol());

        UsuarioEntity mappedBack = usuarioMapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals("ADMIN", mappedBack.getRol());
    }

    @Test
    @DisplayName("Debe mapear BarberoEntity a Barbero y viceversa")
    void testBarberoMapper() {
        assertNull(barberoMapper.toDomain(null));
        assertNull(barberoMapper.toEntity(null));

        BarberoEntity entity = BarberoEntity.builder()
                .id("b1")
                .nombre("Miguel")
                .especialidades(List.of("CORTE"))
                .diasDisponibles(List.of("MONDAY"))
                .horaInicio("08:00")
                .horaFin("18:00")
                .activo(true)
                .build();

        Barbero domain = barberoMapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals("Miguel", domain.getNombre());
        assertTrue(domain.isActivo());

        BarberoEntity mappedBack = barberoMapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals("Miguel", mappedBack.getNombre());
    }

    @Test
    @DisplayName("Debe mapear ServicioEntity a ServicioCatalogo y viceversa")
    void testServicioMapper() {
        assertNull(servicioMapper.toDomain(null));
        assertNull(servicioMapper.toEntity(null));

        ServicioEntity entity = ServicioEntity.builder()
                .id("s1")
                .codigo("SER-001")
                .nombre("Corte Clásico")
                .precio(25000.0)
                .especialidadRequerida("CORTE")
                .build();

        ServicioCatalogo domain = servicioMapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals("SER-001", domain.getCodigo());

        ServicioEntity mappedBack = servicioMapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals(25000.0, mappedBack.getPrecio());
    }

    @Test
    @DisplayName("Debe mapear TurnoEntity a Turno y viceversa")
    void testTurnoMapper() {
        assertNull(turnoMapper.toDomain(null));
        assertNull(turnoMapper.toEntity(null));

        TurnoEntity entity = TurnoEntity.builder()
                .id("t1")
                .barberoNombre("Miguel")
                .fecha("2026-10-25")
                .hora("10:00")
                .estado("DISPONIBLE")
                .build();

        Turno domain = turnoMapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals(EstadoTurno.DISPONIBLE, domain.getEstado());

        TurnoEntity mappedBack = turnoMapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals("DISPONIBLE", mappedBack.getEstado());
    }

    @Test
    @DisplayName("Debe mapear ReservaEntity a Reserva y viceversa")
    void testReservaMapper() {
        assertNull(reservaMapper.toDomain(null));
        assertNull(reservaMapper.toEntity(null));

        ReservaEntity.ClienteEmbedded clienteEmbedded = ReservaEntity.ClienteEmbedded.builder()
                .nombre("Juan")
                .telefono("3001234567")
                .correo("juan@gmail.com")
                .build();

        ReservaEntity entity = ReservaEntity.builder()
                .id("r1")
                .cliente(clienteEmbedded)
                .fecha("2026-10-25")
                .hora("10:00")
                .servicios(List.of("SER-001"))
                .barberoAsignado("Miguel")
                .total(25000.0)
                .estado("CONFIRMADA")
                .build();

        edu.dosw.parcial.core.models.Reserva domain = reservaMapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals("r1", domain.getIdReserva());
        assertEquals("Juan", domain.getCliente().getNombre());
        assertEquals(EstadoReserva.CONFIRMADA, domain.getEstado());

        ReservaEntity mappedBack = reservaMapper.toEntity(domain);
        assertNotNull(mappedBack);
        assertEquals("CONFIRMADA", mappedBack.getEstado());
        assertEquals("juan@gmail.com", mappedBack.getCliente().getCorreo());
    }
}
