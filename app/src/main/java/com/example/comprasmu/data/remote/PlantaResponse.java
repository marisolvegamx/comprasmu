package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.CatalogoDetalle;

public class PlantaResponse {
    String status;
    CatalogoDetalle data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CatalogoDetalle getData() {
        return data;
    }

    public void setData(CatalogoDetalle data) {
        this.data = data;
    }
}
