package com.example.comprasmu;


import android.content.Context;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.comprasmu.data.remote.InformeEnvio;

import com.example.comprasmu.ui.informe.PostInformeViewModel;

public class SubirInformeTask extends AsyncTask<String, Float, Integer> {
    private boolean cancelarSiHayMas100Archivos;
    private ProgressBar miBarraDeProgreso;
    public static String TAG="SubirInformeTask";
    InformeEnvio envio;
    Context context;
    /**
     * Contructor de ejemplo que podemos crear en el AsyncTask
     *
     * @param en este ejemplo le pasamos un booleano que indica si hay más de 100 archivos o no. Si le pasas true se cancela por la mitad del progreso, si le pasas false seguirá hasta el final sin cancelar la descarga simulada
     */
    public SubirInformeTask(boolean cancelarSiHayMas100Archivos,InformeEnvio envio, Context context ) {
        this.cancelarSiHayMas100Archivos = cancelarSiHayMas100Archivos;
        this.envio=envio;
        this.context=context;

    }

    /**
     * Se ejecuta antes de empezar el hilo en segundo plano. Después de este se ejecuta el método "doInBackground" en Segundo Plano
     *
     * Se ejecuta en el hilo: PRINCIPAL
     */
    @Override
    protected void onPreExecute() {
     //   Toast.makeText(getActivity(),"ANTES de EMPEZAR la descarga. Hilo PRINCIPAL",Toast.LENGTH_SHORT);
        Log.v(TAG, "ANTES de EMPEZAR la descarga. Hilo PRINCIPAL");

        // miBarraDeProgreso = (ProgressBar) root.findViewById(R.id.progressBar_indicador);
    }

    /**
     * Se ejecuta después de "onPreExecute". Se puede llamar al hilo Principal con el método "publishProgress" que ejecuta el método "onProgressUpdate" en hilo Principal
     *
     * Se ejecuta en el hilo: EN SEGUNDO PLANO
     *
     * @param array con los valores pasados en "execute"
     * @return devuelve un valor al terminar de ejecutar este segundo plano. Se lo envía y ejecuta "onPostExecute" si ha termiado, o a "onCancelled" si se ha cancelado con "cancel"
     */
    @Override
    protected Integer doInBackground(String... variableNoUsada) {

        enviarReporte();

        return 0;
    }

    /**
     * Se ejecuta después de que en "doInBackground" ejecute el método "publishProgress".
     *
     * Se ejecuta en el hilo: PRINCIPAL
     *
     * @param array con los valores pasados en "publishProgress"
     */
    @Override
    protected void onProgressUpdate(Float... porcentajeProgreso) {
        // TV_mensaje.setText("Progreso descarga: "+porcentajeProgreso[0]+"%. Hilo PRINCIPAL");
        Log.v(TAG, "Progreso descarga: "+porcentajeProgreso[0]+"%. Hilo PRINCIPAL");

        //  miBarraDeProgreso.setProgress( Math.round(porcentajeProgreso[0]) );
    }

    /**
     * Se ejecuta después de terminar "doInBackground".
     *
     * Se ejecuta en el hilo: PRINCIPAL
     *
     * @param array con los valores pasados por el return de "doInBackground".
     */
    @Override
    protected void onPostExecute(Integer cantidadProcesados) {
        //   TV_mensaje.setText("DESPUÉS de TERMINAR la descarga. Se han descarcado "+cantidadProcesados+" imágenes. Hilo PRINCIPAL");
        Log.v(TAG, "DESPUÉS de TERMINAR la descarga. Se han descarcado "+cantidadProcesados+" imágenes. Hilo PRINCIPAL");

        // TV_mensaje.setTextColor(Color.GREEN);
    }

    /**
     * Se ejecuta si se ha llamado al método "cancel" y después de terminar "doInBackground". Por lo que se ejecuta en vez de "onPostExecute"
     * Nota: Este onCancelled solo funciona a partir de Android 3.0 (Api Level 11 en adelante). En versiones anteriores onCancelled no funciona
     *
     * Se ejecuta en el hilo: PRINCIPAL
     *
     * @param array con los valores pasados por el return de "doInBackground".
     */
    @Override
    protected void onCancelled (Integer cantidadProcesados) {
        //   TV_mensaje.setText("DESPUÉS de CANCELAR la descarga. Se han descarcado "+cantidadProcesados+" imágenes. Hilo PRINCIPAL");
        Log.v(TAG, "DESPUÉS de CANCELAR la descarga. Se han descarcado "+cantidadProcesados+" imágenes. Hilo PRINCIPAL");

        // TV_mensaje.setTextColor(Color.RED);
    }
    public void enviarReporte() {
        //reviso si tengo conexion
        if(NavigationDrawerActivity.isOnlineNet()) {
            PostInformeViewModel postviewModel = new PostInformeViewModel(context);

            postviewModel.sendInforme(envio);

            String result = postviewModel.getMensaje();
            Log.d(TAG, result+" resultado servidor");
        }
    }


}