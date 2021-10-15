package com.example.comprasmu.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.comprasmu.MainActivity;
import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.remote.SubirFoto;

import java.text.SimpleDateFormat;
import java.util.List;

/****un servicio x foto****/
public class SubirFotoService extends IntentService
{
    private static final String TAG = "SubirFotoService";
    Context context;

    String userId;
    ImagenDetalle imagenSubir;

    public static final String ACTION_UPLOAD_IMG = "com.example.comprasmu.intentservice.action.PROGRESO";

    public static final String EXTRA_IMG_PATH = "com.example.comprasmu.intentservice.extra.IMG_PATH";
    public static final String EXTRA_IMAGE_ID = "com.example.comprasmu.intentservice.extra.IMG_PATH";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public SubirFotoService()
    {
        super("SubirFotoService");
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();

            if (ACTION_UPLOAD_IMG.equals(action))
            {
                imagenSubir=new ImagenDetalle();
                imagenSubir.setRuta(intent.getStringExtra(EXTRA_IMG_PATH));
                imagenSubir.setId(intent.getIntExtra(EXTRA_IMAGE_ID,0));

                handleUploadImg();
            }
        }
    }
    private void handleUploadImg()
    {

            // Instanciar y registrar un Observador
            SubirFotoListener objObservador  = new SubirFotoListener();



            try {
                notificar();
                SubirFoto sf = new SubirFoto();
                sf.agregarObservador(objObservador);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sf.subirFoto(userId, imagenSubir, context);


                // enviarOtra();
            }catch (Exception ex){
                ex.getStackTrace();
            }


    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e("TAG","onCreate");
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("TAG","onDestroy");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

    private void onErrors(Throwable throwable) {
        sendBroadcastMeaasge("Error in file upload " + throwable.getMessage());
        Log.e(TAG, "onErrors: ", throwable);
    }

    public void sendBroadcastMeaasge(String message) {
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("result", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private void notificar(){
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationBuilder = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("Download")
                .setContentText("Downloading Image")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

    }


       // protected void onPreExecute() {
            // mostramos el círculo de progreso
           // progressBar.setVisibility(View.VISIBLE);
       // }

    private void sendProgressUpdate(boolean downloadComplete) {

        Intent intent = new Intent(NavigationDrawerActivity.PROGRESS_UPDATE);
        intent.putExtra("uploadComplete", downloadComplete);
        LocalBroadcastManager.getInstance(SubirFotoService.this).sendBroadcast(intent);
    }
    /*
 Impresión del progreso en tiempo real
   */
    private void updateNotification(int currentProgress) {


        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText("Downloaded: " + currentProgress + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }
    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("Image Download Complete");
        notificationManager.notify(0, notificationBuilder.build());

    }


    public class SubirFotoListener {


        boolean downloadComplete = false;

        public SubirFotoListener() {


        }

        public void onProgress(int progress){
            updateNotification(progress);
        }
        public void onSuccess(){
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
