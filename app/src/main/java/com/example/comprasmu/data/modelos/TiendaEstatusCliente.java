package com.example.comprasmu.data.modelos;

import androidx.room.Entity;

@Entity(tableName = "tienda_estatuscliente",primaryKeys = {"une_id", "clientesId"})
public class TiendaEstatusCliente {

    private int une_id;
    private int clientesId;
    private int estatus;
    private int periodo;

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

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }
}
