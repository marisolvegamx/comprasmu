package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.List;

public class InformeGastoEnv {

    private InformeEtapa informeEtapa;
    private List<InformeGastoDet> informeGastoDet;
    private String claveUsuario;
    private String indice;
    private List<ImagenDetalle> imagenDetalles;

    public InformeEtapa getInformeEtapa() {
        return informeEtapa;
    }

    public void setInformeEtapa(InformeEtapa informeEtapa) {
        this.informeEtapa = informeEtapa;
    }

    public List<InformeGastoDet> getInformeGastoDet() {
        return informeGastoDet;
    }

    public void setInformeGastoDet(List<InformeGastoDet> informeGastoDet) {
        this.informeGastoDet = informeGastoDet;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public List<ImagenDetalle> getImagenDetalles() {
        return imagenDetalles;
    }

    public void setImagenDetalles(List<ImagenDetalle> imagenDetalles) {
        this.imagenDetalles = imagenDetalles;
    }

    public String toJson(InformeGastoEnv informe) {
        //  this.inf_visitasIdlocal=informe.getVisitasId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();



        String JSON = gson.toJson(informe);
        return  JSON;

    }
}
