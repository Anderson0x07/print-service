package com.impresion.impresion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteImpresionDto {
    private String documento;
    private String direccion;
    private String telefono;
}
