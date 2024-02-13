package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "detalle_caja")
public class DetalleCaja {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int informeEtapaId;
    private int num_caja;
    private String alto;
    private String ancho;
    private String largo;
    private String peso;
    private int estatusSync;
    private Integer estatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInformeEtapaId() {
        return informeEtapaId;
    }

    public void setInformeEtapaId(int informeEtapaId) {
        this.informeEtapaId = informeEtapaId;
    }

    public int getNum_caja() {
        return num_caja;
    }

    public void setNum_caja(int num_caja) {
        this.num_caja = num_caja;
    }

    public String getAlto() {
        return alto;
    }

    public void setAlto(String alto) {
        this.alto = alto;
    }

    public String getAncho() {
        return ancho;
    }

    public void setAncho(String ancho) {
        this.ancho = ancho;
    }

    public String getLargo() {
        return largo;
    }

    public void setLargo(String largo) {
        this.largo = largo;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public int getEstatusSync() {
        return estatusSync;
    }

    public void setEstatusSync(int estatusSync) {
        this.estatusSync = estatusSync;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
