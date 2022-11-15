package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;

import java.util.List;

public class RespInfEtapaResponse { //y correcciones

    private List<Correccion> correcciones;
    private List<InformeEtapa> informeEtapa;
    private List<InformeEtapaDet> informeEtapaDet;
    private List<DetalleCaja> detalleCaja;

    public List<Correccion> getCorrecciones() {
        return correcciones;
    }

    public void setCorrecciones(List<Correccion> correcciones) {
        this.correcciones = correcciones;
    }

    public List<InformeEtapa> getInformeEtapa() {
        return informeEtapa;
    }

    public void setInformeEtapa(List<InformeEtapa> informeEtapa) {
        this.informeEtapa = informeEtapa;
    }

    public List<InformeEtapaDet> getInformeEtapaDet() {
        return informeEtapaDet;
    }

    public void setInformeEtapaDet(List<InformeEtapaDet> informeEtapaDet) {
        this.informeEtapaDet = informeEtapaDet;
    }

    public List<DetalleCaja> getDetalleCaja() {
        return detalleCaja;
    }

    public void setDetalleCaja(List<DetalleCaja> detalleCaja) {
        this.detalleCaja = detalleCaja;
    }
}