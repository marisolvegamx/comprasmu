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
        int clave=1;
        Log.i("DescAutomaticasServiceManager",Constantes.CLAVEUSUARIO);

        //todo quitar este codigo de prueba
        if(Constantes.CLAVEUSUARIO!=null&&!Constantes.CLAVEUSUARIO.equals("")){
           try{
               clave=Integer.parseInt(Constantes.CLAVEUSUARIO);

           }catch (NumberFormatException ex){
               Log.e("DescAutomaticasServiceManager","Error al convertir usuario");
           }
        }
        if((clave%3==0||clave%2==0)) {
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
    }
    public void detenerServicio() {
        if (descargasAutomaticasservicio != null) {
            servicioIniciado = false;
            descargasAutomaticasservicio.detenerServicio();
        }
    }


}
