package com.example.comprasmu.ui.envio;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesGuias {

    public static String buscarInformeEnvio(Activity activity, String indice, String ciudadNombre)
    {
        if(activity!=null) {
            SharedPreferences prefe = activity.getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
            String infEnvioId = prefe.getString(indice + "_" + ciudadNombre + "_infenvio", "");
            return infEnvioId;
        }
        return null;
    }
    public static void guardarInformeEnvio(Activity activity,String indice, String ciudadNombre,int infEnvioId){
        if(activity!=null) {
            String datoGuardado = PreferencesGuias.buscarInformeEnvio(activity, indice, ciudadNombre);
            if (datoGuardado == null || datoGuardado.equals("")) {
                SharedPreferences prefe = activity.getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefe.edit();
                editor.putString(indice + "_" + ciudadNombre + "_infenvio", infEnvioId + "");
                editor.commit();
            }
        }

    }
}
