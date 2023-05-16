package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="siglas")
public class Sigla {
    @PrimaryKey
    int id;
    String siglas;
    String planta;
    int  plantasId;
    int  clientesId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public int getPlantasId() {
        return plantasId;
    }

    public void setPlantasId(int plantasId) {
        this.plantasId = plantasId;
    }

    public int getClientesId() {
        return clientesId;
    }

    public void setClientesId(int clientesId) {
        this.clientesId = clientesId;
    }
}
