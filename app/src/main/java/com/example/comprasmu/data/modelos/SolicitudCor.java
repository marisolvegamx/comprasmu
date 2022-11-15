package com.example.comprasmu.data.modelos;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.comprasmu.data.Converters;

import java.util.Date;

@Entity(tableName = "solicitud_cor")
public class SolicitudCor {
    @PrimaryKey(autoGenerate = true)
    private int id; //val_id
    private int informesId;
    private int plantasId;
    private String plantaNombre;
    private int clientesId;
    private String clienteNombre;
    private String indice;
    private String nombreTienda;
    private String descripcionFoto;
    private int descripcionId;
    private String descMostrar; //como la ven
    private int numFoto; //para inf etapa será el id del detalle, para compra el id de imagendet

    private int numFoto2;

   private int numfoto3;
    private String motivo;
    private int total_fotos;
    private int etapa;
    private int estatus;
    private int estatusSync;

    private int contador;
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(Converters.class)
    private Date createdAt;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public int getEtapa() {
        return etapa;
    }

    public int getInformesId() {
        return informesId;
    }

    public void setInformesId(int informesId) {
        this.informesId = informesId;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getDescripcionFoto() {
        return descripcionFoto;
    }

    public void setDescripcionFoto(String descripcionFoto) {
        this.descripcionFoto = descripcionFoto;
    }

    public int getNumFoto() {
        return numFoto;
    }

    public void setNumFoto(int numFoto) {
        this.numFoto = numFoto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getTotal_fotos() {
        return total_fotos;
    }

    public void setTotal_fotos(int total_fotos) {
        this.total_fotos = total_fotos;
    }

    public int getDescripcionId() {
        return descripcionId;
    }

    public void setDescripcionId(int descripcionId) {
        this.descripcionId = descripcionId;
    }

    public void setEtapa(int etapa) {
        this.etapa = etapa;
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

    public String getDescMostrar() {
        return descMostrar;
    }

    public void setDescMostrar(String descMostrar) {
        this.descMostrar = descMostrar;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public int getNumFoto2() {
        return numFoto2;
    }

    public void setNumFoto2(int numFoto2) {
        this.numFoto2 = numFoto2;
    }

    public int getNumfoto3() {
        return numfoto3;
    }

    public void setNumfoto3(int numfoto3) {
        this.numfoto3 = numfoto3;
    }
}
