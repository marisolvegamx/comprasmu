package com.example.comprasmu.data.modelos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lista_compras_detalle",primaryKeys = {"id", "listaId"})
public class ListaCompraDetalle {



    private int id;

    private int listaId;
    private int productosId;
    private String productoNombre;
    private String tamanio;
    private int tamanioId;

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
    private String lid_fechapermitida;
    private String lid_fecharestringida;
    private int lid_orden;

    @ColumnInfo(name="lid_backup", defaultValue = "-1")
    private int lid_backup;



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

    public String getLid_fechapermitida() {
        return lid_fechapermitida;
    }

    public void setLid_fechapermitida(String lid_fechapermitida) {
        this.lid_fechapermitida = lid_fechapermitida;
    }

    public String getLid_fecharestringida() {
        return lid_fecharestringida;
    }

    public void setLid_fecharestringida(String lid_fecharestringida) {
        this.lid_fecharestringida = lid_fecharestringida;
    }

    public String getNvoCodigo() {
        return nvoCodigo;
    }

    public void setNvoCodigo(String nvoCodigo) {
        this.nvoCodigo = nvoCodigo;
    }

    public int getTamanioId() {
        return tamanioId;
    }

    public void setTamanioId(int tamanioId) {
        this.tamanioId = tamanioId;
    }

    public int getLid_orden() {
        return lid_orden;
    }

    public void setLid_orden(int lid_orden) {
        this.lid_orden = lid_orden;
    }

    public int getLid_backup() {
        return lid_backup;
    }

    public void setLid_backup(int lid_backup) {
        this.lid_backup = lid_backup;
    }
}
