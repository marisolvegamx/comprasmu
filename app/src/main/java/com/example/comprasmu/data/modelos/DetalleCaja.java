package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "detalle_caja")
public class DetalleCaja {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int informeEtapaId;
    private int num_caja;
    private String dimensiones;
    private int estatusSync;

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

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public int getEstatusSync() {
        return estatusSync;
    }

    public void setEstatusSync(int estatusSync) {
        this.estatusSync = estatusSync;
    }
}
