package com.example.comprasmu.data.remote;


import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.Tienda;

import java.util.ArrayList;

public class TiendasResponse {
    ArrayList<Tienda> tiendas;
    ArrayList<Geocerca> geocercas;

    public ArrayList<Tienda> getTiendas() {
        return tiendas;
    }

    public void setTiendas(ArrayList<Tienda> tiendas) {
        this.tiendas = tiendas;
    }

    public ArrayList<Geocerca> getGeocercas() {
        return geocercas;
    }

    public void setGeocercas(ArrayList<Geocerca> geocercas) {
        this.geocercas = geocercas;
    }
}
