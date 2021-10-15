package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.TablaVersiones;

import java.util.ArrayList;
import java.util.List;

public class ListaCompraResponse {

    private List<TablaVersiones> versiones;

    private String actualizacion;
    private List<ListaCompra> compras;
    private List<ListaCompraDetalle> detalles;

    public List<TablaVersiones> getVersiones() {
        return versiones;
    }

    public void setVersiones(ArrayList<TablaVersiones> versiones) {
        this.versiones = versiones;
    }

    public String getActualizacion() {
        return actualizacion;
    }

    public void setActualizacion(String actualizacion) {
        this.actualizacion = actualizacion;
    }

    public List<ListaCompra> getCompras() {
        return compras;
    }

    public void setCompras(List<ListaCompra> compras) {
        this.compras = compras;
    }

    public List<ListaCompraDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<ListaCompraDetalle> detalles) {
        this.detalles = detalles;
    }
}
