package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeGastoDet;

import java.util.List;

public class CambiosInformesReponse {

    private List<InformeCompraDetalle> ID;
    private List<DetalleCaja> DC;
    private List<InformeEtapaDet> IED;
    private List<InformeGastoDet> IGD;

    public List<InformeCompraDetalle> getID() {
        return ID;
    }

    public void setID(List<InformeCompraDetalle> ID) {
        this.ID = ID;
    }

    public List<DetalleCaja> getDC() {
        return DC;
    }

    public void setDC(List<DetalleCaja> DC) {
        this.DC = DC;
    }

    public List<InformeEtapaDet> getIED() {
        return IED;
    }

    public void setIED(List<InformeEtapaDet> IED) {
        this.IED = IED;
    }

    public List<InformeGastoDet> getIGD() {
        return IGD;
    }

    public void setIGD(List<InformeGastoDet> IGD) {
        this.IGD = IGD;
    }
}
