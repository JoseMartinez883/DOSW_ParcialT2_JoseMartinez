package edu.dosw.parcial.config;

import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final edu.dosw.parcial.persistence.repositories.BarberoRepository barberoRepository;
    private final edu.dosw.parcial.persistence.repositories.ServicioRepository servicioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            edu.dosw.parcial.persistence.repositories.BarberoRepository barberoRepository,
            edu.dosw.parcial.persistence.repositories.ServicioRepository servicioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.barberoRepository = barberoRepository;
        this.servicioRepository = servicioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
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

            if (!barberoRepository.existsByNombreIgnoreCase("Miguel")) {
                edu.dosw.parcial.persistence.entities.BarberoEntity miguel = edu.dosw.parcial.persistence.entities.BarberoEntity.builder()
                        .nombre("Miguel")
                        .especialidades(java.util.List.of("SER-001", "SER-002", "CORTE", "BARBA"))
                        .diasDisponibles(java.util.List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"))
                        .horaInicio("08:00")
                        .horaFin("20:00")
                        .activo(true)
                        .build();
                barberoRepository.save(miguel);
                log.info("Barbero predeterminado Miguel creado con especialidades y horario completo");
            }

            if (!servicioRepository.existsByCodigo("SER-001")) {
                edu.dosw.parcial.persistence.entities.ServicioEntity ser1 = edu.dosw.parcial.persistence.entities.ServicioEntity.builder()
                        .codigo("SER-001")
                        .nombre("Corte Clásico")
                        .precio(25000.0)
                        .especialidadRequerida("CORTE")
                        .build();
                servicioRepository.save(ser1);
                log.info("Servicio SER-001 creado");
            }

            if (!servicioRepository.existsByCodigo("SER-002")) {
                edu.dosw.parcial.persistence.entities.ServicioEntity ser2 = edu.dosw.parcial.persistence.entities.ServicioEntity.builder()
                        .codigo("SER-002")
                        .nombre("Corte y Barba")
                        .precio(35000.0)
                        .especialidadRequerida("BARBA")
                        .build();
                servicioRepository.save(ser2);
                log.info("Servicio SER-002 creado");
            }
        } catch (Exception e) {
            log.warn("No se pudo inicializar los datos iniciales (posible arranque sin BD): {}", e.getMessage());
        }
    }
}
