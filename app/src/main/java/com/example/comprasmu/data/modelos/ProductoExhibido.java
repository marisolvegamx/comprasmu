package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "producto_exhibido")
public class ProductoExhibido {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int visitasId;
    private int imagenId;
    private int clienteId;
    private String nombreCliente;
    private int estatusSync;
    private int noPermiten;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVisitasId() {
        return visitasId;
    }

    public void setVisitasId(int visitasId) {
        this.visitasId = visitasId;
    }

    public int getImagenId() {
        return imagenId;
    }

    public void setImagenId(int imagenId) {
        this.imagenId = imagenId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getEstatusSync() {
        return estatusSync;
    }

    public void setEstatusSync(int estatusSync) {
        this.estatusSync = estatusSync;
    }

    public int getNoPermiten() {
        return noPermiten;
    }

    public void setNoPermiten(int noPermiten) {
        this.noPermiten = noPermiten;
    }
}
