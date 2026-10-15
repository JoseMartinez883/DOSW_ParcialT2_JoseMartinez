package edu.dosw.parcial.config;

import edu.dosw.parcial.persistence.entities.BarberoEntity;
import edu.dosw.parcial.persistence.entities.ServicioEntity;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.ServicioRepository;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final String CORTE = "CORTE";
    private static final String SER_001 = "SER-001";
    private static final String THURSDAY = "THURSDAY";
    private static final String TUESDAY = "TUESDAY";
    private static final String HORA_10_00 = "10:00";

    private final UsuarioRepository usuarioRepository;
    private final BarberoRepository barberoRepository;
    private final ServicioRepository servicioRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(
            UsuarioRepository usuarioRepository,
            BarberoRepository barberoRepository,
            ServicioRepository servicioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.barberoRepository = barberoRepository;
        this.servicioRepository = servicioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            seedDefaultUsers();
            seedDefaultBarber();
            seedDefaultCatalog();
        } catch (Exception e) {
            log.warn("No se pudo inicializar los datos iniciales (posible arranque sin BD): {}", e.getMessage());
        }
    }

    private void seedDefaultUsers() {
        if (!usuarioRepository.existsByCorreo("admin@gmail.com")) {
            UsuarioEntity admin = UsuarioEntity.builder()
                    .correo("admin@gmail.com")
                    .password(passwordEncoder.encode("password123"))
                    .nombre("Administrador Principal")
                    .telefono("3100000000")
                    .rol("ADMIN")
                    .build();
            usuarioRepository.save(admin);
            log.info("Usuario predeterminado ADMIN creado: admin@gmail.com");
        }

        if (!usuarioRepository.existsByCorreo("juan@gmail.com")) {
            UsuarioEntity cliente = UsuarioEntity.builder()
                    .correo("juan@gmail.com")
                    .password(passwordEncoder.encode("password123"))
                    .nombre("Juan Perez")
                    .telefono("3001234567")
                    .rol("CLIENTE")
                    .build();
            usuarioRepository.save(cliente);
            log.info("Usuario predeterminado CLIENTE creado: juan@gmail.com");
        }
    }

    private void seedDefaultBarber() {
        if (!barberoRepository.existsByNombreIgnoreCase("Miguel")) {
            BarberoEntity miguel = BarberoEntity.builder()
                    .nombre("Miguel")
                    .especialidades(List.of(CORTE, "CORTE CON BARBA", SER_001, "SER-002"))
                    .diasDisponibles(List.of("MONDAY", TUESDAY, "WEDNESDAY", THURSDAY))
                    .horaInicio(HORA_10_00)
                    .horaFin("17:00")
                    .activo(true)
                    .build();
            barberoRepository.save(miguel);
            log.info("Barbero Miguel creado: Lunes a Jueves 10:00 - 17:00 (CORTE, CORTE CON BARBA)");
        }

        if (!barberoRepository.existsByNombreIgnoreCase("Laura")) {
            BarberoEntity laura = BarberoEntity.builder()
                    .nombre("Laura")
                    .especialidades(List.of("CAPILAR", "CEJAS", "SER-003", "SER-005"))
                    .diasDisponibles(List.of("MONDAY", TUESDAY, "WEDNESDAY", THURSDAY, "FRIDAY"))
                    .horaInicio(HORA_10_00)
                    .horaFin("17:00")
                    .activo(true)
                    .build();
            barberoRepository.save(laura);
            log.info("Barbero Laura creada: Lunes a Viernes 10:00 - 17:00 (CAPILAR, CEJAS)");
        }

        if (!barberoRepository.existsByNombreIgnoreCase("Andrés") && !barberoRepository.existsByNombreIgnoreCase("Andres")) {
            BarberoEntity andres = BarberoEntity.builder()
                    .nombre("Andrés")
                    .especialidades(List.of(CORTE, "BARBA", SER_001, "SER-004"))
                    .diasDisponibles(List.of(TUESDAY, THURSDAY, "SATURDAY", "SUNDAY"))
                    .horaInicio(HORA_10_00)
                    .horaFin("14:00")
                    .activo(true)
                    .build();
            barberoRepository.save(andres);
            log.info("Barbero Andrés creado: Martes, Jueves y Fines de Semana 10:00 - 14:00 (CORTE, BARBA)");
        }
    }

    private void seedDefaultCatalog() {
        seedServiceIfAbsent(SER_001, "Corte clásico", 25000.0, CORTE);
        seedServiceIfAbsent("SER-002", "Corte + barba", 38000.0, "CORTE CON BARBA");
        seedServiceIfAbsent("SER-003", "Diseño de cejas", 15000.0, "CEJAS");
        seedServiceIfAbsent("SER-004", "Afeitado con navaja", 20000.0, "BARBA");
        seedServiceIfAbsent("SER-005", "Tratamiento capilar", 45000.0, "CAPILAR");
    }

    private void seedServiceIfAbsent(String codigo, String nombre, double precio, String especialidadRequerida) {
        if (!servicioRepository.existsByCodigo(codigo)) {
            ServicioEntity servicio = ServicioEntity.builder()
                    .codigo(codigo)
                    .nombre(nombre)
                    .precio(precio)
                    .especialidadRequerida(especialidadRequerida)
                    .build();
            servicioRepository.save(servicio);
            log.info("Servicio {} [{}] creado: ${}", codigo, nombre, precio);
        }
    }
}
