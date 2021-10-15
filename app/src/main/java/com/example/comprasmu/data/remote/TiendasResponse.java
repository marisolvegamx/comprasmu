package com.example.comprasmu.data.remote;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.modelos.Tienda;

import java.util.ArrayList;

public class TiendasResponse {
    LiveData<ArrayList<Tienda>> tiendas;

    public LiveData<ArrayList<Tienda>> getTiendas() {
        return tiendas;
    }

    public void setTiendas(LiveData<ArrayList<Tienda>> tiendas) {
        this.tiendas = tiendas;
    }
}
