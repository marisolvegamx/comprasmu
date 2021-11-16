package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="atributos")
public class Atributo {
        @PrimaryKey(autoGenerate = true)
        private int id_atributo;
        private String at_nombre;
        private int id_tipoempaque;
        private String nombre_empaqueesp;
        private String nombre_empaque_ing;
       /* private int at_idcliente;
        private int at_idclasificaciondano;
        private int at_idponderaciondano;*/

    public int getId_atributo() {
        return id_atributo;
    }

    public void setId_atributo(int id_atributo) {
        this.id_atributo = id_atributo;
    }

    public String getAt_nombre() {
        return at_nombre;
    }

    public void setAt_nombre(String at_nombre) {
        this.at_nombre = at_nombre;
    }

    public int getId_tipoempaque() {
        return id_tipoempaque;
    }

    public void setId_tipoempaque(int id_tipoempaque) {
        this.id_tipoempaque = id_tipoempaque;
    }

    public String getNombre_empaqueesp() {
        return nombre_empaqueesp;
    }

    public void setNombre_empaqueesp(String nombre_empaqueesp) {
        this.nombre_empaqueesp = nombre_empaqueesp;
    }

    public String getNombre_empaque_ing() {
        return nombre_empaque_ing;
    }

    public void setNombre_empaque_ing(String nombre_empaque_ing) {
        this.nombre_empaque_ing = nombre_empaque_ing;
    }
}
