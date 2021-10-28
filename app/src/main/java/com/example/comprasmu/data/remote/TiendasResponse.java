package com.example.comprasmu.data.remote;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.modelos.Tienda;

import java.util.ArrayList;

public class TiendasResponse {
    ArrayList<Tienda> tiendas;

    public ArrayList<Tienda> getTiendas() {
        return tiendas;
    }

    public void setTiendas(ArrayList<Tienda> tiendas) {
        this.tiendas = tiendas;
    }
}
