package com.example.comprasmu.data.modelos;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "coretiquetado_cajadet")
public class CorEtiquetadoCajaDet {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int coretiquetadocId;
    private String numfotoant;
    private String ruta_fotonva;
    private int descripcionId;
    private String descripcion;
    private int numcaja;
    private int estatus;
    private int estatusSync;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoretiquetadocId() {
        return coretiquetadocId;
    }

    public void setCoretiquetadocId(int coretiquetadocId) {
        this.coretiquetadocId = coretiquetadocId;
    }

    public int getDescripcionId() {
        return descripcionId;
    }

    public void setDescripcionId(int descripcionId) {
        this.descripcionId = descripcionId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNumcaja() {
        return numcaja;
    }

    public void setNumcaja(int numcaja) {
        this.numcaja = numcaja;
    }

    public String getNumfotoant() {
        return numfotoant;
    }

    public void setNumfotoant(String numfotoant) {
        this.numfotoant = numfotoant;
    }

    public String getRuta_fotonva() {
        return ruta_fotonva;
    }

    public void setRuta_fotonva(String ruta_fotonva) {
        this.ruta_fotonva = ruta_fotonva;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getEstatusSync() {
        return estatusSync;
    }

    public void setEstatusSync(int estatusSync) {
        this.estatusSync = estatusSync;
    }
}
