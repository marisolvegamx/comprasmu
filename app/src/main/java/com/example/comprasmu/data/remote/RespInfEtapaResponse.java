package com.example.comprasmu.data.remote;


import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.InformeEnvioDet;
import com.example.comprasmu.data.modelos.InformeEnvioPaq;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeGastoDet;

import java.util.List;

public class RespInfEtapaResponse { //y correcciones


    private List<InformeEtapa> informeEtapa;
    private List<InformeEtapaDet> informeEtapaDet;
    private List<DetalleCaja> detalleCaja;
    private List<InformeEnvioPaq> informeEnvio;
    private List<InformeEnvioDet> enviodetalle;
    private List<InformeGastoDet> gastodetalle;



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

    public List<InformeEnvioPaq> getInformeEnvio() {
        return informeEnvio;
    }

    public void setInformeEnvio(List<InformeEnvioPaq> informeEnvio) {
        this.informeEnvio = informeEnvio;
    }

    public List<InformeEnvioDet> getEnviodetalle() {
        return enviodetalle;
    }

    public void setEnviodetalle(List<InformeEnvioDet> enviodetalle) {
        this.enviodetalle = enviodetalle;
    }

    public List<InformeGastoDet> getGastodetalle() {
        return gastodetalle;
    }

    public void setGastodetalle(List<InformeGastoDet> gastodetalle) {
        this.gastodetalle = gastodetalle;
    }
}