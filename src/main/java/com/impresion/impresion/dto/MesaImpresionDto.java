package com.impresion.impresion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MesaImpresionDto {
    private int id_mesa;

    @NotEmpty(message = "Se requiere el numero de la mesa")
    private String numero;
}
