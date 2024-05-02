package com.impresion.impresion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoImpresionDto {
    private int id_pedido;

    private ProductoImpresionDto producto;

    private String nota;

    @Positive
    private int cantidad;

    @Positive
    private double subtotal;
}
