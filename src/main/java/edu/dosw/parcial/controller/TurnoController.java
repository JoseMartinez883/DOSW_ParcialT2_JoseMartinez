package edu.dosw.parcial.controller;

import edu.dosw.parcial.config.OpenApiConfig;
import edu.dosw.parcial.controller.dtos.request.CrearTurnoRequest;
import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.controller.dtos.response.TurnoResponse;
import edu.dosw.parcial.core.services.TurnoService;
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
@RequestMapping("/api/v1/turnos")
@Tag(name = "Turnos", description = "Endpoints para la gestión de turnos de la barbería")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear turno", description = "Crea un nuevo turno para un barbero en una fecha y hora específicas. Requiere rol ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Turno creado exitosamente",
                    content = @Content(schema = @Schema(implementation = TurnoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Formato de fecha u hora inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token ausente o inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Barbero no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Turno duplicado para el barbero",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Fecha u hora fuera de la disponibilidad del barbero",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<TurnoResponse> crearTurno(@Valid @RequestBody CrearTurnoRequest request) {
        log.info("Petición recibida en POST /api/v1/turnos para barbero [{}]", request.getBarberoNombre());
        TurnoResponse response = turnoService.crearTurno(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
