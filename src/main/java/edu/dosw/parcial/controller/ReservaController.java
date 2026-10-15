package edu.dosw.parcial.controller;

import edu.dosw.parcial.config.OpenApiConfig;
import edu.dosw.parcial.controller.dtos.request.PedirTurnoRequest;
import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.controller.dtos.response.ReservaResponse;
import edu.dosw.parcial.core.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/reservas")
@Tag(name = "Reservas", description = "Endpoints para agendamiento y consulta de reservas")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Pedir un turno (Reservar)", description = "Permite a un cliente solicitar una reserva en una franja disponible evaluando la cadena de reglas de negocio en orden estricto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva confirmada exitosamente",
                    content = @Content(schema = @Schema(implementation = ReservaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validación de entrada fallida (E03 nombre, E07 teléfono, E08 correo)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token ausente o expirado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol CLIENTE",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado en el catálogo (E09)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Franja llena (E01) o turno no disponible",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Barbero sin especialidad para el servicio (E02)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ReservaResponse> pedirTurno(@Valid @RequestBody PedirTurnoRequest request) {
        log.info("Petición recibida en POST /api/v1/reservas para cliente [{}]", request.getCliente().getNombre());
        ReservaResponse response = reservaService.pedirTurno(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @org.springframework.web.bind.annotation.GetMapping("/me")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Consultar mis reservas", description = "Consulta únicamente las reservas del cliente autenticado, ordenadas por fecha y hora de manera descendente. Requiere rol CLIENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reservas del cliente autenticado",
                    content = @Content(array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @Schema(implementation = edu.dosw.parcial.controller.dtos.response.MisReservasResponse.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token ausente o inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol CLIENTE",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<java.util.List<edu.dosw.parcial.controller.dtos.response.MisReservasResponse>> consultarMisReservas(java.security.Principal principal) {
        String correo = principal != null ? principal.getName() : "";
        log.info("Petición recibida en GET /api/v1/reservas/me para usuario [{}]", correo);
        java.util.List<edu.dosw.parcial.controller.dtos.response.MisReservasResponse> reservas = reservaService.obtenerMisReservas(correo);
        return ResponseEntity.ok(reservas);
    }

    @org.springframework.web.bind.annotation.GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Consultar todas las reservas", description = "Consulta las reservas de todos los clientes, pasadas y futuras, ordenadas por fecha y hora de manera descendente. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista global de reservas de la barbería",
                    content = @Content(array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @Schema(implementation = edu.dosw.parcial.controller.dtos.response.TodasReservasResponse.class)))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token ausente o inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<java.util.List<edu.dosw.parcial.controller.dtos.response.TodasReservasResponse>> consultarTodasLasReservas() {
        log.info("Petición recibida en GET /api/v1/reservas para consulta administrativa");
        java.util.List<edu.dosw.parcial.controller.dtos.response.TodasReservasResponse> reservas = reservaService.obtenerTodasLasReservas();
        return ResponseEntity.ok(reservas);
    }
}
