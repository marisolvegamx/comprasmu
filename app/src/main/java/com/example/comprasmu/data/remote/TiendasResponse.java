package com.example.comprasmu.data.remote;


import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.TiendaJson;

import java.util.ArrayList;

public class TiendasResponse {
    ArrayList<TiendaJson> tiendas;
    ArrayList<Geocerca> geocercas;

    public ArrayList<TiendaJson> getTiendas() {
        return tiendas;
    }

    public void setTiendas(ArrayList<TiendaJson> tiendas) {
        this.tiendas = tiendas;
    }

    public ArrayList<Geocerca> getGeocercas() {
        return geocercas;
    }

    public void setGeocercas(ArrayList<Geocerca> geocercas) {
        this.geocercas = geocercas;
    }
}
