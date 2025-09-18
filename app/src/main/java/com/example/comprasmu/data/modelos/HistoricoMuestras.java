package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "historico_muestras")
public class HistoricoMuestras {
    @PrimaryKey(autoGenerate = true)
    private int hisId;
    private String inf_indice;
    private int plantaId;
    private int clienteId;
    private int ind_informes_id;
    private int ind_id;
    private int productoId;
    private String producto ;
    private int tamanioId;
    private String presentacion;
    private int empaquesId;
    private String empaque;
    private Date caducidad;
    private int tipoAnalisis;
    private String nombreAnalisis;
    private int categoriaId;
    private String categoriaNombre;

    public int getHisId() {
        return hisId;
    }

    public void setHisId(int hisId) {
        this.hisId = hisId;
    }

    public String getInf_indice() {
        return inf_indice;
    }

    public void setInf_indice(String inf_indice) {
        this.inf_indice = inf_indice;
    }

    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }

    public int getPlantaId() {
        return plantaId;
    }

    public void setPlantaId(int plantaId) {
        this.plantaId = plantaId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getInd_informes_id() {
        return ind_informes_id;
    }

    public void setInd_informes_id(int ind_informes_id) {
        this.ind_informes_id = ind_informes_id;
    }

    public int getInd_id() {
        return ind_id;
    }

    public void setInd_id(int ind_id) {
        this.ind_id = ind_id;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getTamanioId() {
        return tamanioId;
    }

    public void setTamanioId(int tamanioId) {
        this.tamanioId = tamanioId;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public int getEmpaquesId() {
        return empaquesId;
    }

    public void setEmpaquesId(int empaquesId) {
        this.empaquesId = empaquesId;
    }

    public String getEmpaque() {
        return empaque;
    }

    public void setEmpaque(String empaque) {
        this.empaque = empaque;
    }

    public Date getCaducidad() {
        return caducidad;
    }

    public int getTipoAnalisis() {
        return tipoAnalisis;
    }

    public void setTipoAnalisis(int tipoAnalisis) {
        this.tipoAnalisis = tipoAnalisis;
    }

    public String getNombreAnalisis() {
        return nombreAnalisis;
    }

    public void setNombreAnalisis(String nombreAnalisis) {
        this.nombreAnalisis = nombreAnalisis;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }
}
