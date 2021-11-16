package com.example.comprasmu.data.modelos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lista_compras_detalle",primaryKeys = {"id", "listaId"})
public class ListaCompraDetalle {


    @NonNull
    private int id;
    @NonNull
    private int listaId;
    private int productosId;
    private String productoNombre;
    private String tamanio;

    private String empaque;
    private int empaquesId;
    private String tipoAnalisis;
    private int analisisId;
    private int cantidad;
    private String codigosNoPermitidos;
    private String nvoCodigo;
    private int estatus;
    private int comprados;
    private int tipoMuestra;
    private String nombreTipoMuestra;
    private int categoriaid;
    private String categoria;
    private String lid_fechapremitida;
    private String lis_fecharestringida;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListaId() {
        return listaId;
    }

    public void setListaId(int listaId) {
        this.listaId = listaId;
    }

    public int getProductosId() {
        return productosId;
    }

    public void setProductosId(int productosId) {
        this.productosId = productosId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public String getEmpaque() {
        return empaque;
    }

    public void setEmpaque(String empaque) {
        this.empaque = empaque;
    }

    public int getEmpaquesId() {
        return empaquesId;
    }

    public void setEmpaquesId(int empaquesId) {
        this.empaquesId = empaquesId;
    }

    public String getTipoAnalisis() {
        return tipoAnalisis;
    }

    public void setTipoAnalisis(String tipoAnalisis) {
        this.tipoAnalisis = tipoAnalisis;
    }

    public int getAnalisisId() {
        return analisisId;
    }

    public void setAnalisisId(int analisisId) {
        this.analisisId = analisisId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigosNoPermitidos() {
        return codigosNoPermitidos;
    }

    public void setCodigosNoPermitidos(String codigosNoPermitidos) {
        this.codigosNoPermitidos = codigosNoPermitidos;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getComprados() {
        return comprados;
    }

    public void setComprados(int comprados) {
        this.comprados = comprados;
    }

    public String getPresentacion(){
        return this.empaque+" "+this.tamanio;
    }

    public int getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(int tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    public String getNombreTipoMuestra() {
        return nombreTipoMuestra;
    }

    public void setNombreTipoMuestra(String nombreTipoMuestra) {
        this.nombreTipoMuestra = nombreTipoMuestra;
    }

    public int getCategoriaid() {
        return categoriaid;
    }

    public void setCategoriaid(int categoriaid) {
        this.categoriaid = categoriaid;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLid_fechapremitida() {
        return lid_fechapremitida;
    }

    public void setLid_fechapremitida(String lid_fechapremitida) {
        this.lid_fechapremitida = lid_fechapremitida;
    }

    public String getLis_fecharestringida() {
        return lis_fecharestringida;
    }

    public void setLis_fecharestringida(String lis_fecharestringida) {
        this.lis_fecharestringida = lis_fecharestringida;
    }

    public String getNvoCodigo() {
        return nvoCodigo;
    }

    public void setNvoCodigo(String nvoCodigo) {
        this.nvoCodigo = nvoCodigo;
    }
}
