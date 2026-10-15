package edu.dosw.parcial.core;

import edu.dosw.parcial.controller.dtos.request.LoginRequest;
import edu.dosw.parcial.controller.dtos.response.LoginResponse;
import edu.dosw.parcial.core.models.exception.BusinessException;
import edu.dosw.parcial.core.security.JwtTokenProvider;
import edu.dosw.parcial.core.services.AuthServiceImpl;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest validRequest;
    private UsuarioEntity sampleUser;

    @BeforeEach
    void setUp() {
        validRequest = LoginRequest.builder()
                .correo("admin@gmail.com")
                .password("password123")
                .build();

        sampleUser = UsuarioEntity.builder()
                .id("u1")
                .correo("admin@gmail.com")
                .password("hashed_pwd")
                .nombre("Admin")
                .rol("ADMIN")
                .build();
    }

    @Test
    @DisplayName("Debe autenticar correctamente y retornar token JWT con el rol")
    void login_Success_ReturnsTokenAndRole() {
        when(usuarioRepository.findByCorreo("admin@gmail.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("password123", "hashed_pwd")).thenReturn(true);
        when(jwtTokenProvider.generateToken("admin@gmail.com", "ADMIN")).thenReturn("mocked.jwt.token");

        LoginResponse response = authService.login(validRequest);

        assertNotNull(response);
        assertEquals("mocked.jwt.token", response.getToken());
        assertEquals("ADMIN", response.getRol());
        verify(usuarioRepository).findByCorreo("admin@gmail.com");
        verify(passwordEncoder).matches("password123", "hashed_pwd");
        verify(jwtTokenProvider).generateToken("admin@gmail.com", "ADMIN");
    }

    @Test
    @DisplayName("Debe lanzar excepción 401 si el usuario no existe en la base de datos")
    void login_UserNotFound_ThrowsBusinessException() {
        when(usuarioRepository.findByCorreo("admin@gmail.com")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(validRequest));

        assertEquals("E_CREDENCIALES_INVALIDAS", exception.getCodigo());
        assertEquals("Correo o contraseña incorrectos.", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString());
    }

    @Test
    @DisplayName("Debe lanzar excepción 401 si la contraseña es incorrecta")
    void login_InvalidPassword_ThrowsBusinessException() {
        when(usuarioRepository.findByCorreo("admin@gmail.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("password123", "hashed_pwd")).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(validRequest));

        assertEquals("E_CREDENCIALES_INVALIDAS", exception.getCodigo());
        assertEquals("Correo o contraseña incorrectos.", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString());
    }
}
