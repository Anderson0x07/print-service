package com.impresion.impresion.controller;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.impresion.impresion.dto.CompraImpresionDto;
import com.impresion.impresion.dto.PedidoImpresionDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.print.PrintService;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class Controller {

    @PostMapping("/print")
    public void print(@RequestBody CompraImpresionDto compraDto) throws IOException {

        System.out.println(compraDto.toString());

        PrintService printService = PrinterOutputStream.getPrintServiceByName("GP-L80250 Series");
        PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);

        EscPos escpos = new EscPos(printerOutputStream);

        Style title = new Style()
                .setFontSize(Style.FontSize._2, Style.FontSize._2)
                .setJustification(EscPosConst.Justification.Center)
                .setBold(true);

        Style center = new Style(escpos.getStyle())
                .setJustification(EscPosConst.Justification.Center);

        Style centerBold = new Style(escpos.getStyle())
                .setBold(true)
                .setJustification(EscPosConst.Justification.Center);

        Style derecha = new Style(escpos.getStyle())
                .setJustification(EscPosConst.Justification.Right);

        Style bold = new Style(escpos.getStyle())
                .setBold(true);

        String nombreEmpresa = "CALDERO CHORREANTE";
        String direccion = "AV. 1E #16A-19 CAOBOS";
        String nit = "3046476489";
        String textoSuperior = "Responsable del Impuesto a las Ventas";
        String textoExtra = "Documento Equivalente Sistema POS";
        String numFactura = String.format("NUM-%06d", compraDto.getId_compra());

        LocalTime fechaActual = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String horaFormateada = fechaActual.format(formatter);

        String fecha = compraDto.getFecha_compra().toString() + " " + horaFormateada;
        String vendedor = compraDto.getMesero().getNombre()+" "+compraDto.getMesero().getApellido();
        String mesa = compraDto.getMesa().getNumero();
        String textCliente = "CONSUMIDOR FINAL";

        List<PedidoImpresionDto> pedidos = compraDto.getPedidos();

        escpos.writeLF(title,nombreEmpresa)
                .writeLF(center, direccion)
                .writeLF(center, nit)
                .writeLF(center, textoSuperior)
                .writeLF(center, textoExtra)
                .writeLF(centerBold, numFactura)
                .feed(1)
                .writeLF("Fecha: "+fecha)
                .writeLF("Vendedor: "+vendedor);

        if(mesa.equalsIgnoreCase("DOMICILIO")) {
            escpos.writeLF(mesa);
        } else {
            escpos.writeLF("Mesa: "+mesa);
        }


        if(compraDto.getCliente() != null) {
            escpos.writeLF("Cliente: "+compraDto.getCliente().getDocumento())
                    .writeLF(textCliente)
                    .writeLF(compraDto.getCliente().getDireccion())
                    .writeLF(compraDto.getCliente().getTelefono())
                    .feed(1);
        } else {
            escpos.writeLF("Cliente: Varios");
        }

        escpos.writeLF("------------------------------------------------")
                .writeLF("DESCRIPCION          PRECIO UND    CANT    TOTAL")
                .writeLF("------------------------------------------------");

        String nombreProducto, precioUnitProd, cantidad, precioTotalProd = "";
        DecimalFormat formato = new DecimalFormat("#,###.##");
        for(int i=0; i < pedidos.size(); i++) {

            nombreProducto = pedidos.get(i).getProducto().getNombre();
            precioUnitProd = "$"+formato.format(pedidos.get(i).getProducto().getPrecio());
            cantidad = String.valueOf(pedidos.get(i).getCantidad());
            precioTotalProd = "$"+formato.format(pedidos.get(i).getSubtotal());

            //AGREGAR A LA IMPRESION
            String linea = concatenarCampos(nombreProducto, precioUnitProd, cantidad, precioTotalProd);

            escpos.writeLF(linea);
        }
        escpos.writeLF("------------------------------------------------");

        String subtotal = "$"+formato.format((compraDto.getTotal()-(compraDto.getTotal()*0.08)));
        String impuestos = "$"+formato.format(compraDto.getTotal()*0.08);
        String propina = "$"+formato.format(compraDto.getPropina());
        String total = "$"+formato.format(compraDto.getTotal()+compraDto.getPropina());

        String subtotalConEspacios = concatenarCamposPie("SUBTOTAL:", subtotal);
        String impuestosConEspacios = concatenarCamposPie("IMPUESTOS:", impuestos);
        String propinaConEspacios = concatenarCamposPie("PROPINA SUGERIDA:", propina);
        String totalConEspacios = concatenarCamposPie("TOTAL A PAGAR:", total);

        //DISCRIMINACION DE PAGOS
        escpos.writeLF(subtotalConEspacios)
                .writeLF(impuestosConEspacios)
                .writeLF(propinaConEspacios)
                .writeLF(bold, totalConEspacios);

        //DISCRIMINACION DE IMPUESTOS
        escpos.writeLF("------------------------------------------------")
                .writeLF(centerBold, "SISTEMA DE FACTURACION POS")
                .feed(5)
                .cut(EscPos.CutMode.FULL).close();
    }

    public String ajustarTexto(String texto, int anchoMaximo) {
        StringBuilder sb = new StringBuilder(texto);
        for (int i = texto.length(); i < anchoMaximo; i++) {
            sb.append(" "); // Añadir espacios adicionales si el texto es más corto que el ancho máximo
        }
        return sb.toString();
    }

    public String concatenarCampos(String campo1, String campo2, String campo3, String campo4) {
        // Ajustar la longitud de cada campo
        campo1 = ajustarTexto(campo1, 21);
        campo2 = ajustarTexto(campo2, 15);
        campo3 = ajustarTexto(campo3, 4); // Dejando 2 espacios adicionales para separar el último campo

        int tam = campo1.length() + campo2.length() + campo3.length();
        int espaciosFaltantes = 48 - tam;

        // Ajustar el cuarto campo para que esté alineado a la derecha
        campo4 = String.format("%" + espaciosFaltantes + "s", campo4);
        // Concatenar los campos
        return campo1 + campo2 + campo3 + campo4;
    }

    public String concatenarCamposPie(String campo1, String campo2) {
        // Ajustar la longitud de cada campo
        campo1 = ajustarTexto(campo1, 24);

        int espaciosFaltantes = 48 - campo1.length();

        // Ajustar el cuarto campo para que esté alineado a la derecha
        campo2 = String.format("%" + espaciosFaltantes + "s", campo2);
        // Concatenar los campos
        return campo1 + campo2;
    }
}
