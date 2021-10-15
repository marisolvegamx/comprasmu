package com.example.comprasmu.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.comprasmu.data.Converters;

import java.util.Date;

@Entity(tableName = "informe_compras")
public class InformeCompra {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int visitasId;
    private int consecutivo;
    private boolean primeraMuestra;
    private boolean segundaMuestra;
    private boolean terceraMuestra;
    private int plantasId;
    private int producto_exhibido;
    private String plantaNombre;
    private int clientesId;
    private String clienteNombre;
    private String comentarios;
    private int ticket_compra;
    private int condiciones_traslado;
    private int estatus;
     private int estatusSync;

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

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

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public boolean isPrimeraMuestra() {
        return primeraMuestra;
    }

    public void setPrimeraMuestra(boolean primeraMuestra) {
        this.primeraMuestra = primeraMuestra;
    }

    public boolean isSegundaMuestra() {
        return segundaMuestra;
    }

    public void setSegundaMuestra(boolean segundaMuestra) {
        this.segundaMuestra = segundaMuestra;
    }

    public boolean isTerceraMuestra() {
        return terceraMuestra;
    }

    public void setTerceraMuestra(boolean terceraMuestra) {
        this.terceraMuestra = terceraMuestra;
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

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getTicket_compra() {
        return ticket_compra;
    }

    public void setTicket_compra(int ticket_compra) {
        this.ticket_compra = ticket_compra;
    }

    public int getCondiciones_traslado() {
        return condiciones_traslado;
    }

    public void setCondiciones_traslado(int condiciones_traslado) {
        this.condiciones_traslado = condiciones_traslado;
    }

    public int getProducto_exhibido() {
        return producto_exhibido;
    }

    public void setProducto_exhibido(int producto_exhibido) {
        this.producto_exhibido = producto_exhibido;
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
