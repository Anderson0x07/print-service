package com.impresion.impresion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraImpresionDto {
    private int id_compra;

    private UsuarioImpresionDto mesero;

    private MesaImpresionDto mesa;

    private LocalDate fecha_compra;

    @Positive
    private double total;

    private double propina;

    private List<PedidoImpresionDto> pedidos = new ArrayList<>();

    private ClienteImpresionDto cliente;
}
