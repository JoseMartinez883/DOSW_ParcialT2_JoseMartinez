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
                    .especialidades(List.of("SER-001", "SER-002", "CORTE", "BARBA"))
                    .diasDisponibles(List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"))
                    .horaInicio("08:00")
                    .horaFin("20:00")
                    .activo(true)
                    .build();
            barberoRepository.save(miguel);
            log.info("Barbero predeterminado Miguel creado con especialidades y horario completo");
        }
    }

    private void seedDefaultCatalog() {
        if (!servicioRepository.existsByCodigo("SER-001")) {
            ServicioEntity ser1 = ServicioEntity.builder()
                    .codigo("SER-001")
                    .nombre("Corte Clásico")
                    .precio(25000.0)
                    .especialidadRequerida("CORTE")
                    .build();
            servicioRepository.save(ser1);
            log.info("Servicio SER-001 creado");
        }

        if (!servicioRepository.existsByCodigo("SER-002")) {
            ServicioEntity ser2 = ServicioEntity.builder()
                    .codigo("SER-002")
                    .nombre("Corte y Barba")
                    .precio(35000.0)
                    .especialidadRequerida("BARBA")
                    .build();
            servicioRepository.save(ser2);
            log.info("Servicio SER-002 creado");
        }
    }
}
