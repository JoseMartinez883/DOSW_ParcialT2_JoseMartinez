package edu.dosw.parcial.core.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collection;

public final class DateUtil {

    private DateUtil() {
    }


    public static LocalDate parseFecha(String rawFecha) {
        if (rawFecha == null || rawFecha.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha no puede ser nula ni vacía.");
        }
        return LocalDate.parse(rawFecha.trim());
    }

    public static LocalTime parseHora(String rawHora) {
        if (rawHora == null || rawHora.trim().isEmpty()) {
            throw new IllegalArgumentException("La hora no puede ser nula ni vacía.");
        }
        return LocalTime.parse(rawHora.trim());
    }

    public static boolean esFechaFutura(LocalDate fecha) {
        return fecha != null && fecha.isAfter(LocalDate.now(ZoneId.systemDefault()));
    }


    public static LocalTime calcularFinFranja(LocalTime horaInicio) {
        if (horaInicio == null) {
            throw new IllegalArgumentException("La hora de inicio no puede ser nula.");
        }
        return horaInicio.plusHours(1);
    }


    public static boolean esDiaLaborable(LocalDate fecha, Collection<String> diasDisponibles) {
        if (fecha == null || diasDisponibles == null || diasDisponibles.isEmpty()) {
            return true;
        }
        String dayOfWeek = fecha.getDayOfWeek().name();
        return diasDisponibles.stream()
                .anyMatch(d -> d.equalsIgnoreCase(dayOfWeek) || d.equalsIgnoreCase("ALL"));
    }


    public static boolean franjaDentroDeHorario(LocalTime horaInicio, LocalTime horarioInicio, LocalTime horarioFin) {
        if (horaInicio == null || horarioInicio == null || horarioFin == null) {
            return false;
        }
        LocalTime finFranja = calcularFinFranja(horaInicio);
        return !horaInicio.isBefore(horarioInicio) && !finFranja.isAfter(horarioFin);
    }
}
