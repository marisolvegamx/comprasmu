package com.example.comprasmu;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class PruebaService extends Service implements  PruebaListener{

    private final int[] numbers={56,76,23,56,78,98,1,3,65,33,57,94,27};


    private NotificationManager nm;



    private static final int ID_NOTIFICACION_CREAR=1;



    @Override
    public void onCreate(){
        Toast.makeText(this,"Servicio creado", Toast.LENGTH_SHORT).show();

// Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int idArranque){


        Toast.makeText(this,"Servicio iniciado"+idArranque, Toast.LENGTH_SHORT).show();
        execWithAsyncTask();

        return START_STICKY;
    }

    public void notificar(){
        Log.d("Ejemplo","Tendría que mandar el mensaje");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "tutorialspoint_01";

            String description = "mi canal";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("tutorialspoint_01", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }else
            nm = getSystemService(NotificationManager.class);
        NotificationCompat.Builder notificacion=new NotificationCompat.Builder(this,"tutorialspoint_01")
                .setSmallIcon(R.drawable.ic_baseline_emoji_emotions_24)
                .setContentTitle("Ya terminé")
                .setContentText("informacion adicional")
                .setWhen(System.currentTimeMillis());//trae la fecha actual
        Intent intent2 = new Intent(this, MainActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent intPendiente = PendingIntent.getActivity(this, 0, intent2, 0);
        notificacion.setContentIntent(intPendiente);
        notificacion.setAutoCancel(true); //quitar la alerta cunado se presiona
        nm.notify(ID_NOTIFICACION_CREAR,notificacion.build());
       // nm.cancel(ID_NOTIFICACION_CREAR);

        //ver como cancelar
    }
    @Override
    public void onDestroy(){

        Toast.makeText(this,"Servicio detenido",Toast.LENGTH_SHORT).show();

    }
    @Override
    public IBinder onBind(Intent intent) {

       return null;

    }

    public void execWithAsyncTask(){
       // SimpleTask simpleTask= new SimpleTask(getApplicationContext());
       // simpleTask.agregarObservador(this);
       // simpleTask.execute();
    }


    @Override
    public void onSuccess() {
        notificar();
        stopSelf();
    }

    @Override
    public void onRequestError(String errorMessage, int index) {

    }
}