package com.example.comprasmu.services;

import android.content.Context;
import android.content.Intent;

public class DescAutomaticasServiceManager {
    private static DescAutomaticasServiceManager instancia;
    private DescargasAutomaticasService descargasAutomaticasservicio;
    private boolean servicioIniciado = false;
    private DescAutomaticasServiceManager() {}

    public static DescAutomaticasServiceManager getInstancia() {
        if (instancia == null) {
            instancia = new DescAutomaticasServiceManager();
        }
        return instancia;
    }

    public void setServicio(DescargasAutomaticasService servicio) {
        this.descargasAutomaticasservicio = servicio;
    }

    public void pausarServicio() {
        if (descargasAutomaticasservicio != null) {
            descargasAutomaticasservicio.pausar();
        }
    }

    public void iniciarServicio(Context context) {
        if (!servicioIniciado) {
            Intent intent = new Intent(context, DescargasAutomaticasService.class);
            context.startService(intent);
            servicioIniciado = true;
        }
        descargasAutomaticasservicio.reanudar();
    }
    public void detenerServicio() {
        servicioIniciado=false;
        descargasAutomaticasservicio.detenerServicio();
    }

}
