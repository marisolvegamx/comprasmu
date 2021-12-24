package com.example.comprasmu.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.remote.SubirFoto;
import com.example.comprasmu.data.remote.TodoEnvio;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.informe.PostInformeViewModel;
import com.example.comprasmu.utils.ComprasUtils;

import java.text.SimpleDateFormat;
import java.util.Random;

/****un servicio para subir lo pendiente****/
public class SubirPendService extends IntentService
{
    private static final String TAG = "SubirPendService";
    public static String EXTRA_INDICE="comprasmu.spextraindice";


    String userId, indiceimagen;
    ImagenDetalle imagenSubir;
    PostInformeViewModel pvm;
    int idnot;


    public static final String ACTION_UPLOAD_PEND = "com.example.comprasmu.subirpend";

    public static final String EXTRA_IMG_PATH = "com.example.comprasmu.subirpend.EXTRA_IMG_PATH";
    public static final String EXTRA_IMAGE_ID = "com.example.comprasmu.subirpend.EXTRA_IMAGE_ID";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public SubirPendService()
    {
        super("SubirPendService");
        pvm=new PostInformeViewModel(getApplication());

    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        //Log.d(TAG,"action");
        if (intent != null)
        {
            final String action = intent.getAction();
           // intent.setAction(ACTION_UPLOAD_IMG);

            if (ACTION_UPLOAD_PEND.equals(action))
            {
                Log.d(TAG,"action"+action);


                try {
                    SubirTodoListener objObservador  = new SubirTodoListener();



                    notificar();
                    enviarReporte(objObservador);
                    // enviarOtra();
                }catch (Exception ex){

                    Log.e(TAG,"error"+ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e(TAG,"onCreate");
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

    private void onErrors(Throwable throwable) {
        sendBroadcastMessage("Error in file upload " + throwable.getMessage());
        Log.e(TAG, "onErrors: ", throwable);
    }

    public void sendBroadcastMessage(String message) {
        Intent localIntent = new Intent("brodcast.subir.pend");
        localIntent.putExtra("result", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private void notificar(){
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("comsubp", "compras", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Random random = new Random();
        idnot = random.nextInt(9999 - 1000) + 1000;
        assert notificationManager != null;
        notificationBuilder = new NotificationCompat.Builder(this, "comsubp")
                .setSmallIcon(android.R.drawable.stat_sys_upload
                )
                .setContentTitle("Compras")
                .setContentText("Subiendo informes")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(idnot, notificationBuilder.build());
        sendBroadcastMessage("terminé");

    }


       // protected void onPreExecute() {
            // mostramos el círculo de progreso
           // progressBar.setVisibility(View.VISIBLE);
       // }

    private void sendProgressUpdate(boolean downloadComplete) {

        Intent intent = new Intent(NavigationDrawerActivity.PROGRESS_PEND);
        intent.putExtra("uploadComplete", downloadComplete);
        LocalBroadcastManager.getInstance(SubirPendService.this).sendBroadcast(intent);
    }
    /*
 Impresión del progreso en tiempo real
   */
    private void updateNotification(int currentProgress) {


        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText("Downloaded: " + currentProgress + "%");
        notificationManager.notify(idnot, notificationBuilder.build());
    }
    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(idnot);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
        notificationBuilder.setContentText("Envio completado");
        notificationManager.notify(idnot, notificationBuilder.build());

    }

    public void enviarReporte(SubirTodoListener listener) {
        //reviso si tengo conexion
        if(ComprasUtils.isOnlineNet()) {
            //busco los pendientes

            PostInformeViewModel postviewModel = new PostInformeViewModel(this);
          postviewModel.iniciarConexiones();
            //hago el post
            TodoEnvio envio= postviewModel.prepararInformes();

            postviewModel.sendTodo(envio,listener);


        }
    }
    public class SubirTodoListener {


        boolean downloadComplete = false;

        public SubirTodoListener() {


        }

        public void onProgress(int progress){
            updateNotification(progress);
        }
        public void onSuccess(String mensaje){
          /*  contador++;
            //revisa si son todos
            if(tamañolista==contador) {
                progreso.setVisibility(View.GONE);
                cargarLista();
            }
*/
            //aviso a la actividad



            downloadComplete = true;
            onDownloadComplete(downloadComplete);
            Log.d(TAG, " resultado servidor"+mensaje);
        }

       /* public void onRequestError(String errorMessage, int index){
            Log.d("ejemplo","Hubo un errror");
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setMessage(R.string.error_imagen);
            builder.setNeutralButton("OK", null);

            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
        }*/


    }

}
