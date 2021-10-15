package com.example.comprasmu.data.modelos;

public class DescripcionGenerica {
    public int id;

    public String nombre;

    public String descripcion;

    public DescripcionGenerica() {

    }

    public DescripcionGenerica(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
