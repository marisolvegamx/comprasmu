package com.example.comprasmu.data.remote;

import java.util.Date;

public class MuestraCancelada {
    private int inf_id;
    private int inf_visitasIdlocal;
    private int ind_id;
    private int ind_comprasid;
    private int ind_compraddetid;
    private int ind_comprasIdbu;
    private int ind_comprasDetIdbu;
    private String vas_observaciones;
    private Date vas_fecha;

    public int getInf_id() {
        return inf_id;
    }

    public void setInf_id(int inf_id) {
        this.inf_id = inf_id;
    }

    public int getInf_visitasIdlocal() {
        return inf_visitasIdlocal;
    }

    public void setInf_visitasIdlocal(int inf_visitasIdlocal) {
        this.inf_visitasIdlocal = inf_visitasIdlocal;
    }

    public int getInd_id() {
        return ind_id;
    }

    public void setInd_id(int ind_id) {
        this.ind_id = ind_id;
    }

    public int getInd_comprasid() {
        return ind_comprasid;
    }

    public void setInd_comprasid(int ind_comprasid) {
        this.ind_comprasid = ind_comprasid;
    }

    public int getInd_compraddetid() {
        return ind_compraddetid;
    }

    public void setInd_compraddetid(int ind_compraddetid) {
        this.ind_compraddetid = ind_compraddetid;
    }

    public int getInd_comprasIdbu() {
        return ind_comprasIdbu;
    }

    public void setInd_comprasIdbu(int ind_comprasIdbu) {
        this.ind_comprasIdbu = ind_comprasIdbu;
    }

    public int getInd_comprasDetIdbu() {
        return ind_comprasDetIdbu;
    }

    public void setInd_comprasDetIdbu(int ind_comprasDetIdbu) {
        this.ind_comprasDetIdbu = ind_comprasDetIdbu;
    }

    public String getVas_observaciones() {
        return vas_observaciones;
    }

    public void setVas_observaciones(String vas_observaciones) {
        this.vas_observaciones = vas_observaciones;
    }

    public Date getVas_fecha() {
        return vas_fecha;
    }

    public void setVas_fecha(Date vas_fecha) {
        this.vas_fecha = vas_fecha;
    }
}
