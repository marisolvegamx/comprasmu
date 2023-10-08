package com.example.comprasmu.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.comprasmu.data.Converters;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import javax.annotation.Nullable;

@Entity(tableName = "sustitucion",primaryKeys = {"id_sustitucion", "plantasId"})
public class Sustitucion {

    private int id_sustitucion;
    private int clientesId;
    private int su_tipoempaque;
    private int su_producto;
    private int su_tamanio;
    private String nomproducto;
    private String nomtamanio;
    private String nomempaque;
    private int categoriasId;
    private String nomcategoria;
    private int plantasId;


    public int getId_sustitucion() {
        return id_sustitucion;
    }

    public void setId_sustitucion(int id_sustitucion) {
        this.id_sustitucion = id_sustitucion;
    }

    public int getSu_tipoempaque() {
        return su_tipoempaque;
    }

    public void setSu_tipoempaque(int su_tipoempaque) {
        this.su_tipoempaque = su_tipoempaque;
    }

    public int getSu_producto() {
        return su_producto;
    }

    public void setSu_producto(int su_producto) {
        this.su_producto = su_producto;
    }

    public int getSu_tamanio() {
        return su_tamanio;
    }

    public void setSu_tamanio(int su_tamanio) {
        this.su_tamanio = su_tamanio;
    }

    public String getNomproducto() {
        return nomproducto;
    }

    public void setNomproducto(String nomproducto) {
        this.nomproducto = nomproducto;
    }

    public String getNomtamanio() {
        return nomtamanio;
    }

    public void setNomtamanio(String nomtamanio) {
        this.nomtamanio = nomtamanio;
    }

    public String getNomempaque() {
        return nomempaque;
    }

    public void setNomempaque(String nomempaque) {
        this.nomempaque = nomempaque;
    }

    public int getCategoriasId() {
        return categoriasId;
    }

    public void setCategoriasId(int categoriasId) {
        this.categoriasId = categoriasId;
    }

    public String getNomcategoria() {
        return nomcategoria;
    }

    public void setNomcategoria(String nomcategoria) {
        this.nomcategoria = nomcategoria;
    }

    public int getClientesId() {
        return clientesId;
    }

    public void setClientesId(int clientesId) {
        this.clientesId = clientesId;
    }

    public int getPlantasId() {
        return plantasId;
    }

    public void setPlantasId(int plantasId) {
        this.plantasId = plantasId;
    }
}

