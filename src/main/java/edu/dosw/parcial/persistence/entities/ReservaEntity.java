package edu.dosw.parcial.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios")
public class ReservaEntity {

    @Id
    private String id;

    private ClienteEmbedded cliente;
    private String fecha;
    private String hora;
    private List<String> servicios;
    private String barberoAsignado;
    private double total;
    private String estado;
    private LocalDateTime creadoEn;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClienteEmbedded {
        private String nombre;
        private String telefono;
        private String correo;
    }
}
