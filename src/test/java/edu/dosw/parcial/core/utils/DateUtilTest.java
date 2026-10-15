package edu.dosw.parcial.core.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateUtilTest {

    @Test
    @DisplayName("parseFecha debe parsear correctamente fecha válida y lanzar excepción ante nulo o formato inválido")
    void parseFecha_ValidAndInvalid() {
        LocalDate fecha = DateUtil.parseFecha("2026-10-25");
        assertEquals(2026, fecha.getYear());
        assertEquals(10, fecha.getMonthValue());
        assertEquals(25, fecha.getDayOfMonth());

        assertThrows(IllegalArgumentException.class, () -> DateUtil.parseFecha(null));
        assertThrows(IllegalArgumentException.class, () -> DateUtil.parseFecha("  "));
        assertThrows(DateTimeParseException.class, () -> DateUtil.parseFecha("25-10-2026"));
    }

    @Test
    @DisplayName("parseHora debe parsear correctamente hora válida y lanzar excepción ante nulo o formato inválido")
    void parseHora_ValidAndInvalid() {
        LocalTime hora = DateUtil.parseHora("10:30");
        assertEquals(10, hora.getHour());
        assertEquals(30, hora.getMinute());

        assertThrows(IllegalArgumentException.class, () -> DateUtil.parseHora(null));
        assertThrows(IllegalArgumentException.class, () -> DateUtil.parseHora(""));
        assertThrows(DateTimeParseException.class, () -> DateUtil.parseHora("hora-invalida"));
    }

    @Test
    @DisplayName("esFechaFutura debe retornar true solo si la fecha es posterior a hoy")
    void esFechaFutura_EvaluatesCorrectly() {
        assertTrue(DateUtil.esFechaFutura(LocalDate.now().plusDays(1)));
        assertFalse(DateUtil.esFechaFutura(LocalDate.now()));
        assertFalse(DateUtil.esFechaFutura(LocalDate.now().minusDays(1)));
        assertFalse(DateUtil.esFechaFutura(null));
    }

    @Test
    @DisplayName("calcularFinFranja debe sumar exactamente 1 hora a la hora inicial")
    void calcularFinFranja_AddsOneHour() {
        LocalTime inicio = LocalTime.of(10, 0);
        LocalTime fin = DateUtil.calcularFinFranja(inicio);
        assertEquals(LocalTime.of(11, 0), fin);

        assertThrows(IllegalArgumentException.class, () -> DateUtil.calcularFinFranja(null));
    }

    @Test
    @DisplayName("esDiaLaborable debe validar correctamente según la lista de días")
    void esDiaLaborable_EvaluatesCorrectly() {
        LocalDate lunes = LocalDate.of(2026, 10, 19);

        assertTrue(DateUtil.esDiaLaborable(lunes, List.of("MONDAY", "TUESDAY")));
        assertFalse(DateUtil.esDiaLaborable(lunes, List.of("FRIDAY", "SATURDAY")));
        assertTrue(DateUtil.esDiaLaborable(lunes, List.of("ALL")));
        assertTrue(DateUtil.esDiaLaborable(lunes, null));
        assertTrue(DateUtil.esDiaLaborable(lunes, List.of()));
        assertTrue(DateUtil.esDiaLaborable(null, List.of("MONDAY")));
    }

    @Test
    @DisplayName("franjaDentroDeHorario debe verificar que el turno de 1 hora no empiece antes ni termine después")
    void franjaDentroDeHorario_EvaluatesCorrectly() {
        LocalTime apertura = LocalTime.of(10, 0);
        LocalTime cierre = LocalTime.of(17, 0);

        assertTrue(DateUtil.franjaDentroDeHorario(LocalTime.of(10, 0), apertura, cierre));

        assertTrue(DateUtil.franjaDentroDeHorario(LocalTime.of(16, 0), apertura, cierre));

        assertFalse(DateUtil.franjaDentroDeHorario(LocalTime.of(9, 0), apertura, cierre));

        assertFalse(DateUtil.franjaDentroDeHorario(LocalTime.of(17, 0), apertura, cierre));

        assertFalse(DateUtil.franjaDentroDeHorario(LocalTime.of(16, 30), apertura, cierre));

        assertFalse(DateUtil.franjaDentroDeHorario(null, apertura, cierre));
        assertFalse(DateUtil.franjaDentroDeHorario(LocalTime.of(10, 0), null, cierre));
        assertFalse(DateUtil.franjaDentroDeHorario(LocalTime.of(10, 0), apertura, null));
    }
}
