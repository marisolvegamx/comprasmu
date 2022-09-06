package com.example.comprasmu.data.remote;


import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.modelos.Tienda;

import java.util.ArrayList;

public class SolCorreResponse {
    ArrayList<SolicitudCor> solicitudes;

    public ArrayList<SolicitudCor> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(ArrayList<SolicitudCor> solicitudes) {
        this.solicitudes = solicitudes;
    }
}
