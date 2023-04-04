package com.example.comprasmu.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.comprasmu.data.Converters;
import java.util.Date;

@Entity(tableName = "informe_etapa")
public class InformeEtapa {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int consecutivo;
    private int plantasId;
    private String plantaNombre;
    private int clientesId;
    private String clienteNombre;
    private String indice;
    private String comentarios;

    private int etapa;
    private int total_cajas;
    private int total_muestras;
    private int estatus;
    private int estatusSync;
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(Converters.class)
    private Date createdAt;
    private Date fechaCancel;
    private String motivoCancel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public int getPlantasId() {
        return plantasId;
    }

    public void setPlantasId(int plantasId) {
        this.plantasId = plantasId;
    }

    public String getPlantaNombre() {
        return plantaNombre;
    }

    public void setPlantaNombre(String plantaNombre) {
        this.plantaNombre = plantaNombre;
    }

    public int getClientesId() {
        return clientesId;
    }

    public void setClientesId(int clientesId) {
        this.clientesId = clientesId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getEtapa() {
        return etapa;
    }

    public void setEtapa(int etapa) {
        this.etapa = etapa;
    }

    public int getTotal_cajas() {
        return total_cajas;
    }

    public void setTotal_cajas(int total_cajas) {
        this.total_cajas = total_cajas;
    }

    public int getTotal_muestras() {
        return total_muestras;
    }

    public void setTotal_muestras(int total_muestras) {
        this.total_muestras = total_muestras;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getFechaCancel() {
        return fechaCancel;
    }

    public void setFechaCancel(Date fechaCancel) {
        this.fechaCancel = fechaCancel;
    }

    public String getMotivoCancel() {
        return motivoCancel;
    }

    public void setMotivoCancel(String motivoCancel) {
        this.motivoCancel = motivoCancel;
    }
}
