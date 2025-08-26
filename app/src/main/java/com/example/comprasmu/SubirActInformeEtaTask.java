package com.example.comprasmu;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.ui.informe.PostInformeViewModel;

public class SubirActInformeEtaTask extends AsyncTask<String, Float, Integer> {

    public static String TAG = "SubirInfEtaTask";
    InformeEtapaEnv envio;
    Context context;


    public SubirActInformeEtaTask(InformeEtapaEnv envio, Context context) {

        this.envio=envio;
        this.context=context;

    }


    @Override
    protected void onPreExecute() {
       Log.v(TAG, "ANTES de EMPEZAR a subir. Hilo PRINCIPAL");
    }


    @Override
    protected Integer doInBackground(String... variableNoUsada) {

        enviarReporte();

        return 0;
    }


    @Override
    protected void onProgressUpdate(Float... porcentajeProgreso) {
        Log.v(TAG, "Progreso envio: "+porcentajeProgreso[0]+"%. Hilo PRINCIPAL");

    }


    @Override
    protected void onPostExecute(Integer cantidadProcesados) {
        Log.v(TAG, "DESPUÉS de TERMINAR el envio. Se han descarcado "+cantidadProcesados+" imágenes. Hilo PRINCIPAL");


    }


    @Override
    protected void onCancelled (Integer cantidadProcesados) {
        Log.v(TAG, "DESPUÉS de CANCELAR envio. Se han descarcado "+cantidadProcesados+" imágenes. Hilo PRINCIPAL");


    }
    public void enviarReporte() {
        //reviso si tengo conexion
        if(NavigationDrawerActivity.isOnlineNet(context)) {
            PostInformeViewModel postviewModel = new PostInformeViewModel(context);

            postviewModel.actualizarInformeEta(envio);


        }
    }


}