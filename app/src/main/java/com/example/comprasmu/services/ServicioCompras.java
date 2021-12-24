package com.example.comprasmu.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.data.remote.TodoEnvio;
import com.example.comprasmu.ui.informe.PostInformeViewModel;
import com.example.comprasmu.utils.ComprasUtils;

import java.util.Random;

//hará dos cosas
//buscará notificaciones
//enviará informes
public class ServicioCompras {



    public static final String ACTION_UPLOAD_PEND = "com.example.comprasmu.subirpend";

    private static final String TAG="ServicioCompras";
    private Handler handler;
    private MainThread mConnectThread;

    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }



    public class MainThread extends Thread {
        Context context;
        boolean ejecutar=true;
     /*   IntentFilter filter = new IntentFilter();
        filter.addAction(SubirFotoService.ACTION_UPLOAD_IMG);

        rcv = new SubirFotoProgressReceiver();
        registerReceiver(rcv, filter);
        LocalBroadcastManager.getInstance(this).registerReceiver(rcv, filter);
*/
      /*  sbt = new MiServicioBT();

                this.dispositivo = btAdapter.getRemoteDevice(macImpresora);

                sbt.conectar(this.dispositivo, MIUUID);
*/

        public MainThread(Context context,String usuario) throws Exception {
            this.context=context;

        }

        public void run() {

            try {
                Log.d(TAG,"iniciando servicio");
                while(ejecutar) {
                    Intent i = new Intent();
                    i.setComponent(new ComponentName("com.example.comprasmu", SubirPendService.ACTION_UPLOAD_PEND));
                    ComponentName c =context.startService(i);

                    Thread.sleep(30000);
                }
                // enviarOtra();
            } catch (Exception ex) {

                Log.e(TAG, "error" + ex.getMessage());
                ex.printStackTrace();
            }

        }

        public void cancel() {
            //  exit();
            ejecutar=false;
        }


    }

    public void iniciar(Context context, String usuario) throws Exception{
        mConnectThread=new MainThread(context, usuario);
        mConnectThread.start();


    }




    public void detener(){
        if(mConnectThread!=null){
            mConnectThread.cancel();
        }
    }


}
