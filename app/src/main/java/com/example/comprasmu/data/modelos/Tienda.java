package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tienda")
public class Tienda {

    @PrimaryKey()
    private int une_id;
    private String une_descripcion;
    private String tipoTienda;
    private Integer une_tipotienda;
    private String une_direccion;
    private String ciudad;
    private Integer une_cla_ciudad;
    private String pais;
    private Integer une_cla_pais;
    private String une_puntocardinal;
    private Integer une_estatus;
    private String une_coordenadasxy;
    private Integer une_cadenacomercial;
    private String une_dir_referencia;
    private String color;
    private Integer estpen; //para saber si compre en peñafiel
    private Integer estpep;
    private Integer estele;
    private Integer estjum;
    private String indiceUltimaVisita;

    public int getUne_id() {
        return une_id;
    }

    public void setUne_id(int une_id) {
        this.une_id = une_id;
    }

    public String getUne_descripcion() {
        return une_descripcion;
    }

    public void setUne_descripcion(String une_descripcion) {
        this.une_descripcion = une_descripcion;
    }

    public String getTipoTienda() {
        return tipoTienda;
    }

    public void setTipoTienda(String tipoTienda) {
        this.tipoTienda = tipoTienda;
    }

    public Integer getUne_tipotienda() {
        return une_tipotienda;
    }

    public void setUne_tipotienda(Integer une_tipotienda) {
        this.une_tipotienda = une_tipotienda;
    }

    public String getUne_direccion() {
        return une_direccion;
    }

    public void setUne_direccion(String une_direccion) {
        this.une_direccion = une_direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getUne_cla_ciudad() {
        return une_cla_ciudad;
    }

    public void setUne_cla_ciudad(Integer une_cla_ciudad) {
        this.une_cla_ciudad = une_cla_ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Integer getUne_cla_pais() {
        return une_cla_pais;
    }

    public void setUne_cla_pais(Integer une_cla_pais) {
        this.une_cla_pais = une_cla_pais;
    }

    public String getUne_puntocardinal() {
        return une_puntocardinal;
    }

    public void setUne_puntocardinal(String une_puntocardinal) {
        this.une_puntocardinal = une_puntocardinal;
    }

    public Integer getUne_estatus() {
        return une_estatus;
    }

    public void setUne_estatus(Integer une_estatus) {
        this.une_estatus = une_estatus;
    }

    public String getUne_coordenadasxy() {
        return une_coordenadasxy;
    }

    public void setUne_coordenadasxy(String une_coordenadasxy) {
        this.une_coordenadasxy = une_coordenadasxy;
    }

    public Integer getUne_cadenacomercial() {
        return une_cadenacomercial;
    }

    public void setUne_cadenacomercial(Integer une_cadenacomercial) {
        this.une_cadenacomercial = une_cadenacomercial;
    }

    public String getUne_dir_referencia() {
        return une_dir_referencia;
    }

    public void setUne_dir_referencia(String une_dir_referencia) {
        this.une_dir_referencia = une_dir_referencia;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getEstpen() {
        return estpen;
    }

    public void setEstpen(Integer estpen) {
        this.estpen = estpen;
    }

    public Integer getEstpep() {
        return estpep;
    }

    public void setEstpep(Integer estpep) {
        this.estpep = estpep;
    }

    public Integer getEstele() {
        return estele;
    }

    public void setEstele(Integer estele) {
        this.estele = estele;
    }

    public Integer getEstjum() {
        return estjum;
    }

    public void setEstjum(Integer estjum) {
        this.estjum = estjum;
    }

    public String getIndiceUltimaVisita() {
        return indiceUltimaVisita;
    }

    public void setIndiceUltimaVisita(String indiceUltimaVisita) {
        this.indiceUltimaVisita = indiceUltimaVisita;
    }
}
