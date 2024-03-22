package com.example.comprasmu.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.data.remote.SubirLog;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.Random;


public class SubirLogService extends IntentService
{
    private static final String TAG = "SubirLogService";
    String userId;

    public static final String ACTION_UPLOAD_LOG = "com.example.comprasmu.intentservice.action.PROGRESOLOG";

    public static final String EXTRA_LOG_PATH = "com.example.comprasmu.intentservice.extra.EXTRA_LOG_PATH";


    public SubirLogService()
    {
        super("SubirLogService");
        Log.d(TAG,"action**");
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d(TAG,"action");
        if (intent != null)
        {
            final String action = intent.getAction();
           // intent.setAction(ACTION_UPLOAD_IMG);

            if (ACTION_UPLOAD_LOG.equals(action))
            {
              //  Log.d(TAG,"action"+action);
                handleUploadImg();
            }
        }
    }
    private void handleUploadImg()
    {

            // Instanciar y registrar un Observador
            SubirLogListener objObservador  = new SubirLogListener();

            try {
               // notificar();
                SubirLog sf = new SubirLog();
                sf.agregarObservador(objObservador);
                Log.d(TAG,"ahora si voy a subir* log");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dir=   this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath()+"/compraslog.txt";

                sf.subirlog(Constantes.CLAVEUSUARIO,dir, this);
               // pvm.actualizarEstatusFoto(imagenSubir);
               // Thread.sleep(10000);
                // enviarOtra();
            }catch (Exception ex){

                Log.e(TAG,"error"+ex.getMessage());
                ex.printStackTrace();
            }


    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        //Log.e(TAG,"onCreate");
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
     //   Log.e(TAG,"onDestroy");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent){
    }




    public class SubirLogListener {

        boolean downloadComplete = false;

        public SubirLogListener() {


        }

        public void onProgress(int progress)
        {
           // updateNotification(progress);
        }
        public void onSuccess(){

            downloadComplete = true;
          //  onDownloadComplete(downloadComplete);

        }


    }

}
