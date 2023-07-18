package com.example.comprasmu.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.comprasmu.data.Converters;

import java.util.Date;

@Entity(tableName = "correccion")
public class Correccion {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int solicitudId;
    private String ruta_foto1;
    private String ruta_foto2;
    private String ruta_foto3;
    private int estatus;
    private int estatusSync;
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(Converters.class)
    private Date createdAt;

     private String indice;
    @ColumnInfo(defaultValue = "0")
    private int numfoto;
    private String dato1; //para datos extras en la correccion
    private String dato2;
    private String dato3;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(int solicitudId) {
        this.solicitudId = solicitudId;
    }

    public String getRuta_foto1() {
        return ruta_foto1;
    }

    public void setRuta_foto1(String ruta_foto1) {
        this.ruta_foto1 = ruta_foto1;
    }

    public String getRuta_foto2() {
        return ruta_foto2;
    }

    public void setRuta_foto2(String ruta_foto2) {
        this.ruta_foto2 = ruta_foto2;
    }

    public String getRuta_foto3() {
        return ruta_foto3;
    }

    public void setRuta_foto3(String ruta_foto3) {
        this.ruta_foto3 = ruta_foto3;
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

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public int getNumfoto() {
        return numfoto;
    }

    public void setNumfoto(int numfoto) {
        this.numfoto = numfoto;
    }

    public String getDato1() {
        return dato1;
    }

    public void setDato1(String dato1) {
        this.dato1 = dato1;
    }

    public String getDato2() {
        return dato2;
    }

    public void setDato2(String dato2) {
        this.dato2 = dato2;
    }

    public String getDato3() {
        return dato3;
    }

    public void setDato3(String dato3) {
        this.dato3 = dato3;
    }
}
