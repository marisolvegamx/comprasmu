package com.example.comprasmu.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.comprasmu.data.Converters;

import java.util.Date;

@Entity(tableName = "visitas")
public class Visita {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String indice;
    private String geolocalizacion;
    private int tiendaId; //si no tengo id es nueva
    private String tiendaNombre;
    private String tipoTienda;
    private int tipoId;
    private String direccion;
    private String cadenaComercial;
    private String complementodireccion;
    private String puntoCardinal;
    private String ciudad;
    private int ciudadId;
    private String pais;
    private int paisId;
  /*  private int producto_exhibido;
    private int producto_exhibido;
    private int producto_exhibido;*/
  private int fotoFachada;
    private int estatus;
    private String claveUsuario;
    private int estatusSync;
    private int estatusPepsi; //para saber si puedo comprar pepsi 0 no 1 si
    private int estatusPen;
    private int estatusElec;
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(Converters.class)
    private Date createdAt;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(Converters.class)
    private Date updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getGeolocalizacion() {
        return geolocalizacion;
    }

    public void setGeolocalizacion(String geolocalizacion) {
        this.geolocalizacion = geolocalizacion;
    }

    public int getTiendaId() {
        return tiendaId;
    }

    public void setTiendaId(int tiendaId) {
        this.tiendaId = tiendaId;
    }

    public String getTiendaNombre() {
        return tiendaNombre;
    }

    public void setTiendaNombre(String tiendaNombre) {
        this.tiendaNombre = tiendaNombre;
    }

    public String getTipoTienda() {
        return tipoTienda;
    }

    public void setTipoTienda(String tipoTienda) {
        this.tipoTienda = tipoTienda;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCadenaComercial() {
        return cadenaComercial;
    }

    public void setCadenaComercial(String cadenaComercial) {
        this.cadenaComercial = cadenaComercial;
    }

    public String getComplementodireccion() {
        return complementodireccion;
    }

    public void setComplementodireccion(String complementodireccion) {
        this.complementodireccion = complementodireccion;
    }

    public String getPuntoCardinal() {
        return puntoCardinal;
    }

    public void setPuntoCardinal(String puntoCardinal) {
        this.puntoCardinal = puntoCardinal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getCiudadId() {
        return ciudadId;
    }

    public void setCiudadId(int ciudadId) {
        this.ciudadId = ciudadId;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getPaisId() {
        return paisId;
    }

    public void setPaisId(int paisId) {
        this.paisId = paisId;
    }

    public int getFotoFachada() {
        return fotoFachada;
    }

    public void setFotoFachada(int fotoFachada) {
        this.fotoFachada = fotoFachada;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getEstatusPepsi() {
        return estatusPepsi;
    }

    public void setEstatusPepsi(int estatusPepsi) {
        this.estatusPepsi = estatusPepsi;
    }

    public int getEstatusPen() {
        return estatusPen;
    }

    public void setEstatusPen(int estatusPen) {
        this.estatusPen = estatusPen;
    }

    public int getEstatusElec() {
        return estatusElec;
    }

    public void setEstatusElec(int estatusElec) {
        this.estatusElec = estatusElec;
    }
}
