package com.example.comprasmu.data.modelos;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.comprasmu.data.Converters;

import java.util.HashMap;
import java.util.List;

@Entity(tableName = "reactivos")
public class Reactivo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int sigId;
    private String label;
    private String type; /**posibles vienen del creador form**/
    /*@TypeConverters(Converters.class)
    private HashMap lista;*/

    private String nombreCampo;
    private int sigAlt;
    private boolean botonMicro;
    private boolean isCatalogo;
    private String tabla;
    private String cliente;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public int getSigId() {
        return sigId;
    }

    public void setSigId(int sigId) {
        this.sigId = sigId;
    }

    public int getSigAlt() {
        return sigAlt;
    }


    public void setSigAlt(int sigAlt) {
        this.sigAlt = sigAlt;
    }


    public boolean isBotonMicro() {
        return botonMicro;
    }

    public boolean isCatalogo() {
        return isCatalogo;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public void setCatalogo(boolean catalogo) {
        isCatalogo = catalogo;
    }

    public void setBotonMicro(boolean botonMicro) {
        this.botonMicro = botonMicro;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}
