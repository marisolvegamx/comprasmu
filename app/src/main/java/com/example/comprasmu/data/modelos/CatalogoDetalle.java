package com.example.comprasmu.data.modelos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="catalogos_detalle")
public class CatalogoDetalle {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int cad_idcatalogo;
    private String cad_nombreCatalogo;
    private int cad_idopcion;
    private String cad_descripcionesp;
    private String cad_descripcioning;
    private String cad_otro;

    public int getCad_idcatalogo() {
        return cad_idcatalogo;
    }

    public void setCad_idcatalogo(int cad_idcatalogo) {
        this.cad_idcatalogo = cad_idcatalogo;
    }

    public String getCad_nombreCatalogo() {
        return cad_nombreCatalogo;
    }

    public void setCad_nombreCatalogo(String cad_nombreCatalogo) {
        this.cad_nombreCatalogo = cad_nombreCatalogo;
    }

    public int getCad_idopcion() {
        return cad_idopcion;
    }

    public void setCad_idopcion(int cad_idopcion) {
        this.cad_idopcion = cad_idopcion;
    }

    public String getCad_descripcionesp() {
        return cad_descripcionesp;
    }

    public void setCad_descripcionesp(String cad_descripcionesp) {
        this.cad_descripcionesp = cad_descripcionesp;
    }

    public String getCad_descripcioning() {
        return cad_descripcioning;
    }

    public void setCad_descripcioning(String cad_descripcioning) {
        this.cad_descripcioning = cad_descripcioning;
    }

    public String getCad_otro() {
        return cad_otro;
    }

    public void setCad_otro(String cad_otro) {
        this.cad_otro = cad_otro;
    }
}
