package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.Atributo;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Sustitucion;

import java.util.ArrayList;

public class CatalogosResponse {



    /* ArrayList<CatalogoDetalle> clientes;
    ArrayList<CatalogoDetalle> plantas;
    ArrayList<CatalogoDetalle> tipo_tienda;
    ArrayList<CatalogoDetalle> cadena_comercial;*/
    ArrayList<CatalogoDetalle> catalogos;
    ArrayList<CatalogoDetalle> causas;
    ArrayList<Atributo> atributos;
    ArrayList<Sustitucion> sustitucion;

  /*  public ArrayList<CatalogoDetalle> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<CatalogoDetalle> clientes) {
        this.clientes = clientes;
    }

    public ArrayList<CatalogoDetalle> getPlantas() {
        return plantas;
    }

    public void setPlantas(ArrayList<CatalogoDetalle> plantas) {
        this.plantas = plantas;
    }

    public ArrayList<CatalogoDetalle> getTipo_tienda() {
        return tipo_tienda;
    }

    public void setTipo_tienda(ArrayList<CatalogoDetalle> tipo_tienda) {
        this.tipo_tienda = tipo_tienda;
    }

    public ArrayList<CatalogoDetalle> getCadena_comercial() {
        return cadena_comercial;
    }

    public void setCadena_comercial(ArrayList<CatalogoDetalle> cadena_comercial) {
        this.cadena_comercial = cadena_comercial;
    }

    public ArrayList<CatalogoDetalle> getTomado_de() {
        return tomado_de;
    }

    public void setTomado_de(ArrayList<CatalogoDetalle> tomado_de) {
        this.tomado_de = tomado_de;
    }*/

    public ArrayList<Sustitucion> getSustitucion() {
        return sustitucion;
    }

    public void setSustitucion(ArrayList<Sustitucion> sustitucion) {
        this.sustitucion = sustitucion;
    }

    public ArrayList<CatalogoDetalle> getCausas() {
        return causas;
    }

    public void setCausas(ArrayList<CatalogoDetalle> causas) {
        this.causas = causas;
    }

    public ArrayList<Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(ArrayList<Atributo> atributos) {
        this.atributos = atributos;
    }

    public ArrayList<CatalogoDetalle> getCatalogos() {
        return catalogos;
    }

    public void setCatalogos(ArrayList<CatalogoDetalle> catalogos) {
        this.catalogos = catalogos;
    }
}
