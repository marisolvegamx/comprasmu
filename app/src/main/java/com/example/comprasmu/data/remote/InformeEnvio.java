package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;

import java.util.List;

public class InformeEnvio {
    private Visita visita;
    private InformeCompra informeCompra;
    private List<InformeCompraDetalle> informeCompraDetalles;
    private List<ImagenDetalle> imagenDetalles;
    private List<ProductoExhibido> productosEx;

    public Visita getVisita() {
        return visita;
    }

    public void setVisita(Visita visita) {
        this.visita = visita;
    }

    public InformeCompra getInformeCompra() {
        return informeCompra;
    }

    public void setInformeCompra(InformeCompra informeCompra) {
        this.informeCompra = informeCompra;
    }

    public List<InformeCompraDetalle> getInformeCompraDetalles() {
        return informeCompraDetalles;
    }

    public void setInformeCompraDetalles(List<InformeCompraDetalle> informeCompraDetalles) {
        this.informeCompraDetalles = informeCompraDetalles;
    }

    public List<ImagenDetalle> getImagenDetalles() {
        return imagenDetalles;
    }

    public void setImagenDetalles(List<ImagenDetalle> imagenDetalles) {
        this.imagenDetalles = imagenDetalles;
    }

    public List<ProductoExhibido> getProductosEx() {
        return productosEx;
    }

    public void setProductosEx(List<ProductoExhibido> productosEx) {
        this.productosEx = productosEx;
    }
}
