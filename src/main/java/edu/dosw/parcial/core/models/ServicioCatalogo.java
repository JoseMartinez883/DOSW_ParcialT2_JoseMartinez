package edu.dosw.parcial.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioCatalogo {
    private String id;
    private String codigo;
    private String nombre;
    private double precio;
    private String especialidadRequerida;
}
