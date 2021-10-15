package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.comprasmu.data.Converters;

import java.util.HashMap;

@Entity(tableName = "reactivos")
public class Reactivo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int parentId;
    private String descripcion;
    private String tipoReactivo; /**posibles Abierto Lista Catalogo, Boolean, Imagen**/
    @TypeConverters(Converters.class)
    private HashMap lista;
    private int estatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoReactivo() {
        return tipoReactivo;
    }

    public void setTipoReactivo(String tipoReactivo) {
        this.tipoReactivo = tipoReactivo;
    }

    public HashMap getLista() {
        return lista;
    }

    public void setLista(HashMap lista) {
        this.lista = lista;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
