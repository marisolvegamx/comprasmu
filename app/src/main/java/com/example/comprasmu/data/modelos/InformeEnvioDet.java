package com.example.comprasmu.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.comprasmu.data.Converters;

import java.util.Date;

@Entity(tableName = "informe_envio_det")
public class InformeEnvioDet {
    @PrimaryKey
    private int informeEtapaId;

    private Integer fotoSello;

    private String nombreRecibe;
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(Converters.class)
    private Date fechaEnvio;
    private Integer estatus;
    private Integer estatusSync;

    public int getInformeEtapaId() {
        return informeEtapaId;
    }

    public void setInformeEtapaId(int informeEtapaId) {
        this.informeEtapaId = informeEtapaId;
    }

    public Integer getFotoSello() {
        return fotoSello;
    }

    public void setFotoSello(Integer fotoSello) {
        this.fotoSello = fotoSello;
    }

    public String getNombreRecibe() {
        return nombreRecibe;
    }

    public void setNombreRecibe(String nombreRecibe) {
        this.nombreRecibe = nombreRecibe;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public void setEstatusSync(Integer estatusSync) {
        this.estatusSync = estatusSync;
    }

    public Integer getEstatusSync() {
        return estatusSync;
    }

  }
