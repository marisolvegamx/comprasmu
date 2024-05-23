package com.example.comprasmu.data.modelos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName="lista_compras")
public class ListaCompra {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int plantasId;
    private String plantaNombre;
    private String siglas;
    private int clientesId;
    private String clienteNombre;
    private int ciudadesId;
    private String ciudadNombre;
    private String createdBy;
    private String indice;
    private int estatus;
    private String lis_nota;
    private int lis_etapaactual;
    private int lis_reactivado; //para saber si hubo reactivacion 1-por cancelacion 2- por muestra adcional

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlantasId() {
        return plantasId;
    }

    public void setPlantasId(int plantasId) {
        this.plantasId = plantasId;
    }

    public String getPlantaNombre() {
        return plantaNombre;
    }

    public void setPlantaNombre(String plantaNombre) {
        this.plantaNombre = plantaNombre;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public int getClientesId() {
        return clientesId;
    }

    public void setClientesId(int clientesId) {
        this.clientesId = clientesId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getCiudadesId() {
        return ciudadesId;
    }

    public void setCiudadesId(int ciudadesId) {
        this.ciudadesId = ciudadesId;
    }

    public String getCiudadNombre() {
        return ciudadNombre;
    }

    public void setCiudadNombre(String ciudadNombre) {
        this.ciudadNombre = ciudadNombre;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getLis_nota() {
        return lis_nota;
    }

    public void setLis_nota(String lis_nota) {
        this.lis_nota = lis_nota;
    }

    public int getLis_etapaactual() {
        return lis_etapaactual;
    }

    public void setLis_etapaactual(int lis_etapaactual) {
        this.lis_etapaactual = lis_etapaactual;
    }

    public int getLis_reactivado() {
        return lis_reactivado;
    }

    public void setLis_reactivado(int lis_reactivado) {
        this.lis_reactivado = lis_reactivado;
    }
}
