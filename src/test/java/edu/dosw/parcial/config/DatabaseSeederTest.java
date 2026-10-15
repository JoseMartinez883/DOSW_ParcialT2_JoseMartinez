package edu.dosw.parcial.config;

import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.ServicioRepository;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseSeederTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BarberoRepository barberoRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DatabaseSeeder databaseSeeder;

    @Test
    @DisplayName("Debe inicializar usuarios, barbero y servicios si no existen en BD")
    void run_InitializesData() {
        when(usuarioRepository.existsByCorreo("admin@gmail.com")).thenReturn(false);
        when(usuarioRepository.existsByCorreo("juan@gmail.com")).thenReturn(false);
        when(barberoRepository.existsByNombreIgnoreCase("Miguel")).thenReturn(false);
        when(barberoRepository.existsByNombreIgnoreCase("Laura")).thenReturn(false);
        when(barberoRepository.existsByNombreIgnoreCase("Andrés")).thenReturn(false);
        when(servicioRepository.existsByCodigo("SER-001")).thenReturn(false);
        when(servicioRepository.existsByCodigo("SER-002")).thenReturn(false);
        when(servicioRepository.existsByCodigo("SER-003")).thenReturn(false);
        when(servicioRepository.existsByCodigo("SER-004")).thenReturn(false);
        when(servicioRepository.existsByCodigo("SER-005")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_pwd");

        databaseSeeder.run();

        verify(usuarioRepository, times(2)).save(any());
        verify(barberoRepository, times(3)).save(any());
        verify(servicioRepository, times(5)).save(any());
    }
}
