package com.impresion.impresion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioImpresionDto {
    private int id_usuario;

    @NotEmpty(message = "Se requiere el nombre del usuario")
    private String nombre;

    @NotEmpty(message = "Se requiere el apellido del usuario")
    private String apellido;

    @NotEmpty(message = "Se requiere el documento del usuario")
    private String documento;
}
