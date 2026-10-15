package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.request.PedirTurnoRequest;
import edu.dosw.parcial.controller.dtos.response.MisReservasResponse;
import edu.dosw.parcial.controller.dtos.response.ReservaResponse;
import edu.dosw.parcial.controller.dtos.response.TodasReservasResponse;

import java.util.List;

public interface ReservaService {
    ReservaResponse pedirTurno(PedirTurnoRequest request);
    List<MisReservasResponse> obtenerMisReservas(String correoCliente);
    List<TodasReservasResponse> obtenerTodasLasReservas();
}
