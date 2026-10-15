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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BarberoRepository barberoRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    @DisplayName("Debe inicializar usuarios, barbero y servicios si no existen en BD")
    void run_InitializesData() {
        // Arrange
        when(usuarioRepository.existsByCorreo("admin@gmail.com")).thenReturn(false);
        when(usuarioRepository.existsByCorreo("juan@gmail.com")).thenReturn(false);
        when(barberoRepository.existsByNombreIgnoreCase("Miguel")).thenReturn(false);
        when(servicioRepository.existsByCodigo("SER-001")).thenReturn(false);
        when(servicioRepository.existsByCodigo("SER-002")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_pwd");

        // Act
        dataInitializer.run();

        // Assert
        org.mockito.Mockito.verify(usuarioRepository, org.mockito.Mockito.times(2)).save(any());
        org.mockito.Mockito.verify(barberoRepository, org.mockito.Mockito.times(1)).save(any());
        org.mockito.Mockito.verify(servicioRepository, org.mockito.Mockito.times(2)).save(any());
    }
}
