package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;

import java.util.List;

public class RespInfEtapaResponse { //y correcciones

    private List<Correccion> correcciones;
    private List<InformeEtapa> informesEtapa;
    private List<InformeEtapaDet> informesDetalles;
    private List<DetalleCaja> detalleCajas;

    public List<Correccion> getCorrecciones() {
        return correcciones;
    }

    public void setCorrecciones(List<Correccion> correcciones) {
        this.correcciones = correcciones;
    }

    public List<InformeEtapa> getInformesEtapa() {
        return informesEtapa;
    }

    public void setInformesEtapa(List<InformeEtapa> informesEtapa) {
        this.informesEtapa = informesEtapa;
    }

    public List<InformeEtapaDet> getInformesDetalles() {
        return informesDetalles;
    }

    public void setInformesDetalles(List<InformeEtapaDet> informesDetalles) {
        this.informesDetalles = informesDetalles;
    }

    public List<DetalleCaja> getDetalleCajas() {
        return detalleCajas;
    }

    public void setDetalleCajas(List<DetalleCaja> detalleCajas) {
        this.detalleCajas = detalleCajas;
    }
}