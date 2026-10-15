package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.controller.dtos.request.ClienteRequest;
import edu.dosw.parcial.core.models.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Slf4j
@Component
public class ClienteValidator {

    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern CORREO_GMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@gmail\\.com$");

    public void validar(ClienteRequest cliente) {
        if (cliente == null || cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            log.warn("Rechazo por nombre de cliente inválido (E03)");
            throw new BusinessException("E03", "El nombre es nulo, vacío o contiene únicamente espacios.", HttpStatus.BAD_REQUEST);
        }

        if (cliente.getTelefono() == null || !TELEFONO_PATTERN.matcher(cliente.getTelefono().trim()).matches()) {
            log.warn("Rechazo por teléfono de cliente inválido (E07)");
            throw new BusinessException("E07", "El teléfono no puede ser nulo y debe tener exactamente 10 dígitos.", HttpStatus.BAD_REQUEST);
        }

        if (cliente.getCorreo() == null || !CORREO_GMAIL_PATTERN.matcher(cliente.getCorreo().trim()).matches()) {
            log.warn("Rechazo por correo de cliente inválido (E08)");
            throw new BusinessException("E08", "El correo no puede ser nulo, debe tener formato de email y terminar obligatoriamente en @gmail.com.", HttpStatus.BAD_REQUEST);
        }
    }
}
