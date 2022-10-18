package com.example.comprasmu.data.modelos;

public class DescripcionGenerica {
    public int id;

    public String nombre;

    public String descripcion;
    public String descripcion2;

    public DescripcionGenerica() {

    }

    public DescripcionGenerica(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public DescripcionGenerica(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public DescripcionGenerica(int id, String nombre, String descripcion, String descripcion2) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descripcion2 = descripcion2;
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

    public String getDescripcion2() {
        return descripcion2;
    }

    public void setDescripcion2(String descripcion2) {
        this.descripcion2 = descripcion2;
    }
}
