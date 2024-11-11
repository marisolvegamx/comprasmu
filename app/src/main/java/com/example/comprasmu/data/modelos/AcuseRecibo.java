package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "acuse_recibo")
public class AcuseRecibo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String indice;
    private int aceptado; //0- no aceptado 1-aceptado
    private String ciudad;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public int getAceptado() {
        return aceptado;
    }

    public void setAceptado(int aceptado) {
        this.aceptado = aceptado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
