package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.CrearTurnoRequest;
import edu.dosw.parcial.controller.dtos.response.TurnoResponse;
import edu.dosw.parcial.core.services.TurnoService;
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
class TurnoControllerTest {

    @Mock
    private TurnoService turnoService;

    @InjectMocks
    private TurnoController turnoController;

    @Test
    @DisplayName("Debe procesar la creación de turno y retornar 201 Created con el payload del turno")
    void crearTurno_Success_Returns201Created() {
        // Arrange
        CrearTurnoRequest request = CrearTurnoRequest.builder()
                .barberoNombre("Miguel")
                .fecha("2026-10-25")
                .hora("10:00")
                .build();

        TurnoResponse mockResponse = TurnoResponse.builder()
                .id("65b2fa81")
                .barberoNombre("Miguel")
                .fecha("2026-10-25")
                .hora("10:00")
                .estado("DISPONIBLE")
                .build();

        when(turnoService.crearTurno(request)).thenReturn(mockResponse);

        // Act
        ResponseEntity<TurnoResponse> responseEntity = turnoController.crearTurno(request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("65b2fa81", responseEntity.getBody().getId());
        assertEquals("Miguel", responseEntity.getBody().getBarberoNombre());
        assertEquals("DISPONIBLE", responseEntity.getBody().getEstado());
        verify(turnoService).crearTurno(request);
    }
}
