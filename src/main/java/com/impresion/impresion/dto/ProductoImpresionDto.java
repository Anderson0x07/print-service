package com.impresion.impresion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoImpresionDto {
    private int id_producto;

    @NotEmpty(message = "Se requiere un nombre para el producto")
    private  String nombre;

    @Positive
    private double precio;

    @Positive
    private Double descuento;
}
