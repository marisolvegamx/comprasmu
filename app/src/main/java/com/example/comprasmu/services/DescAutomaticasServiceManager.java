package com.example.comprasmu.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.comprasmu.utils.Constantes;

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

        Log.i("DescAutomaticasServiceManager",Constantes.CLAVEUSUARIO);



            if (!servicioIniciado) {

                Intent intent = new Intent(context, DescargasAutomaticasService.class);
                context.startService(intent);
                servicioIniciado = true;
            }
            else{
                if(descargasAutomaticasservicio!=null){
                    descargasAutomaticasservicio.reanudar();
                }
            }


    }
    public void detenerServicio() {
        if (descargasAutomaticasservicio != null) {
            servicioIniciado = false;
            descargasAutomaticasservicio.detenerServicio();
        }
    }


}
