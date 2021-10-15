package com.example.comprasmu.data.modelos;

import java.util.List;

public class InformeCancelar {
    private InformeCompra informe_compra;
    private List<InformeCompraDetalle> detalle_compra;
    private List<ImagenDetalle> imagen_detalle;

    public InformeCompra getInforme_compra() {
        return informe_compra;
    }

    public void setInforme_compra(InformeCompra informe_compra) {
        this.informe_compra = informe_compra;
    }

    public List<InformeCompraDetalle> getDetalle_compra() {
        return detalle_compra;
    }

    public void setDetalle_compra(List<InformeCompraDetalle> detalle_compra) {
        this.detalle_compra = detalle_compra;
    }

    public List<ImagenDetalle> getImagen_detalle() {
        return imagen_detalle;
    }

    public void setImagen_detalle(List<ImagenDetalle> imagen_detalle) {
        this.imagen_detalle = imagen_detalle;
    }
}
