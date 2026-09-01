package com.example.comprasmu.data.modelos;

import androidx.room.Entity;

@Entity(tableName = "tienda_estatuscliente",primaryKeys = {"une_id", "plantasId"})
public class TiendaEstatusCliente {

    private int une_id;
    private int clientesId;
    private int plantasId;
    private String plantasNombre;
    private int estatus; //2-amarillo, 3-verde
    private String ultimoindice; //para saber el ultimo indice en que se visito

    public int getUne_id() {
        return une_id;
    }

    public void setUne_id(int une_id) {
        this.une_id = une_id;
    }

    public int getClientesId() {
        return clientesId;
    }

    public void setClientesId(int clientesId) {
        this.clientesId = clientesId;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getPlantasId() {
        return plantasId;
    }

    public void setPlantasId(int plantasId) {
        this.plantasId = plantasId;
    }

    public String getPlantasNombre() {
        return plantasNombre;
    }

    public void setPlantasNombre(String plantasNombre) {
        this.plantasNombre = plantasNombre;
    }

    public String getUltimoindice() {
        return ultimoindice;
    }

    public void setUltimoindice(String ultimoindice) {
        this.ultimoindice = ultimoindice;
    }

}
