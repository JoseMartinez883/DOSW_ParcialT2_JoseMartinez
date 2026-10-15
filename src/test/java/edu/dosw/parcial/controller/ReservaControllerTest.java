package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.ClienteRequest;
import edu.dosw.parcial.controller.dtos.request.PedirTurnoRequest;
import edu.dosw.parcial.controller.dtos.response.ReservaResponse;
import edu.dosw.parcial.core.services.ReservaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaControllerTest {

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;

    @Test
    @DisplayName("Debe procesar la petición de reserva y retornar 201 Created con el detalle")
    void pedirTurno_Success_Returns201Created() {
        // Arrange
        ClienteRequest cliente = ClienteRequest.builder()
                .nombre("Juan Perez")
                .telefono("3001234567")
                .correo("juan@gmail.com")
                .build();

        PedirTurnoRequest request = PedirTurnoRequest.builder()
                .cliente(cliente)
                .fecha("2026-10-25")
                .hora("10:00")
                .servicios(List.of("SER-001"))
                .build();

        ReservaResponse mockResponse = ReservaResponse.builder()
                .idReserva("98a1b2c3")
                .barberoAsignado("Miguel")
                .total(25000.0)
                .estado("CONFIRMADA")
                .build();

        when(reservaService.pedirTurno(request)).thenReturn(mockResponse);

        // Act
        ResponseEntity<ReservaResponse> responseEntity = reservaController.pedirTurno(request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("98a1b2c3", responseEntity.getBody().getIdReserva());
        assertEquals("Miguel", responseEntity.getBody().getBarberoAsignado());
        assertEquals(25000.0, responseEntity.getBody().getTotal());
        assertEquals("CONFIRMADA", responseEntity.getBody().getEstado());
        verify(reservaService).pedirTurno(request);
    }

    @Test
    @DisplayName("Debe retornar 200 OK con la lista de reservas del cliente autenticado")
    void consultarMisReservas_Success_Returns200Ok() {
        // Arrange
        java.security.Principal principal = () -> "juan@gmail.com";
        edu.dosw.parcial.controller.dtos.response.MisReservasResponse r1 =
                edu.dosw.parcial.controller.dtos.response.MisReservasResponse.builder()
                        .id("r1")
                        .fecha("2026-10-25")
                        .hora("10:00")
                        .estado("CONFIRMADA")
                        .build();

        when(reservaService.obtenerMisReservas("juan@gmail.com")).thenReturn(List.of(r1));

        // Act
        ResponseEntity<List<edu.dosw.parcial.controller.dtos.response.MisReservasResponse>> responseEntity =
                reservaController.consultarMisReservas(principal);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals("r1", responseEntity.getBody().get(0).getId());
        verify(reservaService).obtenerMisReservas("juan@gmail.com");
    }

    @Test
    @DisplayName("Debe retornar 200 OK con la lista global de reservas para ADMIN")
    void consultarTodasLasReservas_Success_Returns200Ok() {
        // Arrange
        edu.dosw.parcial.controller.dtos.response.TodasReservasResponse r1 =
                edu.dosw.parcial.controller.dtos.response.TodasReservasResponse.builder()
                        .id("r1")
                        .cliente("Juan")
                        .fecha("2026-10-25")
                        .hora("10:00")
                        .build();

        when(reservaService.obtenerTodasLasReservas()).thenReturn(List.of(r1));

        // Act
        ResponseEntity<List<edu.dosw.parcial.controller.dtos.response.TodasReservasResponse>> responseEntity =
                reservaController.consultarTodasLasReservas();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals("r1", responseEntity.getBody().get(0).getId());
        assertEquals("Juan", responseEntity.getBody().get(0).getCliente());
        verify(reservaService).obtenerTodasLasReservas();
    }
}
