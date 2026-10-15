package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.request.CrearTurnoRequest;
import edu.dosw.parcial.controller.dtos.response.TurnoResponse;

public interface TurnoService {
    TurnoResponse crearTurno(CrearTurnoRequest request);
}
