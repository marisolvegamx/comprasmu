package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;

import java.util.List;

public class RespInformesResponse {

    private List<Visita> visita;
    private List<InformeCompra> informeCompra;
    private List<InformeCompraDetalle> informeCompraDetalles;
    private List<ImagenDetalle> imagenDetalles;
    private List<ProductoExhibido> productosEx;

    public List<Visita> getVisita() {
        return visita;
    }

    public void setVisita(List<Visita> visita) {
        this.visita = visita;
    }

    public List<InformeCompra> getInformeCompra() {
        return informeCompra;
    }

    public void setInformeCompra(List<InformeCompra> informeCompra) {
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