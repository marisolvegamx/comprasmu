package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.example.comprasmu.data.modelos.Visita;

import java.util.List;

public class CambiosInformesReponse {

    private List<InformeCompraDetalle> ID;
    private List<DetalleCaja> DC;
    private List<InformeEtapaDet> IED;
    private List<InformeGastoDet> IGD;
    private List<InformeCompra> IC; //se utilizará para eliminación de informes
    private List<Visita> V; //tambien cambian los datos de la tienda y no seactualizan
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

    public List<InformeCompra> getIC() {
        return IC;
    }

    public void setIC(List<InformeCompra> IC) {
        this.IC = IC;
    }

    public List<Visita> getV() {
        return V;
    }

    public void setV(List<Visita> V) {
        this.V = V;
    }

}
