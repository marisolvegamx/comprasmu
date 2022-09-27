package com.example.comprasmu.data.remote;


import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.modelos.TablaVersiones;

import java.util.List;

public class SolCorreResponse {

    private List<TablaVersiones> versiones;
    private String status;
    private String data;
    private List<SolicitudCor> inserts;

    public List<TablaVersiones> getVersiones() {
        return versiones;
    }

    public void setVersiones(List<TablaVersiones> versiones) {
        this.versiones = versiones;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<SolicitudCor> getInserts() {
        return inserts;
    }

    public void setInserts(List<SolicitudCor> inserts) {
        this.inserts = inserts;
    }

    public List<SolicitudCor> getUpdates() {
        return updates;
    }

    public void setUpdates(List<SolicitudCor> updates) {
        this.updates = updates;
    }

    private List<SolicitudCor> updates;


}
