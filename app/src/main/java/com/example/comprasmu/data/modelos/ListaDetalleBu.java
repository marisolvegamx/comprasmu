package com.example.comprasmu.data.modelos;

import java.util.List;

public class ListaDetalleBu extends ListaCompraDetalle {
    boolean botonbu;
    boolean botonAgregar;
    boolean completado;
    public ListaDetalleBu(){

    }
    public ListaDetalleBu(ListaCompraDetalle det) {
        this.id= det.id;
        this.listaId = det.listaId;
        this.productosId = det.productosId;
        this.productoNombre = det.productoNombre;
        this.tamanio = det.tamanio;
        this.tamanioId = det.tamanioId;
        this.empaque = det.empaque;
        this.empaquesId = det.empaquesId;
        this.tipoAnalisis = det.tipoAnalisis;
        this.analisisId = det.analisisId;
        this.cantidad = det.cantidad;
        this.codigosNoPermitidos = det.codigosNoPermitidos;
        this.nvoCodigo = det.nvoCodigo;
        this.estatus = det.estatus;
        this.comprados = det.comprados;
        this.tipoMuestra = det.tipoMuestra;
        this.nombreTipoMuestra = det.nombreTipoMuestra;
        this.categoriaid = det.categoriaid;
        this.categoria = det.categoria;
        this.lid_fechapermitida = det.lid_fechapermitida;
        this.lid_fecharestringida = det.lid_fecharestringida;
        this.lid_orden = det.lid_orden;
        this.lid_backup = det.lid_backup;
    }

    private List<InformeCompraDetalle> infcd;

    public List<InformeCompraDetalle> getInfcd() {
        return infcd;
    }

    public void setInfcd(List<InformeCompraDetalle> infcd) {
        this.infcd = infcd;
    }

    public boolean isBotonbu() {
        return botonbu;
    }

    public void setBotonbu(boolean botonbu) {
        this.botonbu = botonbu;
    }

    public boolean isBotonAgregar() {
        return botonAgregar;
    }

    public void setBotonAgregar(boolean botonAgregar) {
        this.botonAgregar = botonAgregar;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }
}
