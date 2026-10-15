package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.LoginRequest;
import edu.dosw.parcial.controller.dtos.response.LoginResponse;
import edu.dosw.parcial.core.services.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Debe procesar la petición de login y retornar 200 con el token")
    void login_Success_Returns200Ok() {
        // Arrange
        LoginRequest request = LoginRequest.builder()
                .correo("admin@gmail.com")
                .password("password123")
                .build();

        LoginResponse mockResponse = LoginResponse.builder()
                .token("mocked.jwt.token")
                .rol("ADMIN")
                .build();

        when(authService.login(request)).thenReturn(mockResponse);

        // Act
        ResponseEntity<LoginResponse> responseEntity = authController.login(request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("mocked.jwt.token", responseEntity.getBody().getToken());
        assertEquals("ADMIN", responseEntity.getBody().getRol());
        verify(authService).login(request);
    }
}
