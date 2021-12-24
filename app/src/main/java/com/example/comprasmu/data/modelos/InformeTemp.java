package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "informe_temp")
public class InformeTemp {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int visitasId;
    private int iddetalle;

    private int informesId;
    private String nombre_campo;
    private int consecutivo;
    private String valor;
    private String tabla;
    private boolean isPregunta; //false para saber si solo es para guardar info


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_campo() {
        return nombre_campo;
    }

    public void setNombre_campo(String nombre_campo) {
        this.nombre_campo = nombre_campo;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public boolean isPregunta() {
        return isPregunta;
    }

    public void setPregunta(boolean pregunta) {
        isPregunta = pregunta;
    }

    public int getVisitasId() {
        return visitasId;
    }

    public void setVisitasId(int visitasId) {
        this.visitasId = visitasId;
    }

    public int getIddetalle() {
        return iddetalle;
    }

    public void setIddetalle(int iddetalle) {
        this.iddetalle = iddetalle;
    }

    public int getInformesId() {
        return informesId;
    }

    public void setInformesId(int informesId) {
        this.informesId = informesId;
    }
}
