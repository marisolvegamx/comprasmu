package com.example.comprasmu.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.comprasmu.data.Converters;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "informe_etapa_det")
public class InformeEtapaDet {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int informeEtapaId;
    private int etapa;
    private int estatusSync;
    private String ruta_foto;
    private String qr;
    private int num_muestra;
    private int descripcionId;
    private String descripcion;
    private int num_caja;

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

    public int getEtapa() {
        return etapa;
    }

    public void setEtapa(int etapa) {
        this.etapa = etapa;
    }

    public int getEstatusSync() {
        return estatusSync;
    }

    public void setEstatusSync(int estatusSync) {
        this.estatusSync = estatusSync;
    }

    public String getRuta_foto() {
        return ruta_foto;
    }

    public void setRuta_foto(String ruta_foto) {
        this.ruta_foto = ruta_foto;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public int getNum_muestra() {
        return num_muestra;
    }

    public void setNum_muestra(int num_muestra) {
        this.num_muestra = num_muestra;
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

    public int getNum_caja() {
        return num_caja;
    }

    public void setNum_caja(int num_caja) {
        this.num_caja = num_caja;
    }
}

