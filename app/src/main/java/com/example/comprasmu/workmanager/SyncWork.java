package com.example.comprasmu.workmanager;


import android.content.Context;
import android.content.Intent;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.services.SubirPendService;
import com.example.comprasmu.utils.ComprasLog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class SyncWork  extends Worker {

    final String TAG="SyncWork";
    ComprasLog flog;
    public SyncWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
        @Override
        public Result doWork() {

       flog= ComprasLog.getSingleton();
        flog.grabarError("iniciando trabajo");
           subirImagenes();
        return Result.success();
        }

    public void subirImagenes(){
        subirPendientes();
        //busco las imagenes pendientes de subir
        ImagenDetRepositoryImpl imagenRepo=new ImagenDetRepositoryImpl(super.getApplicationContext());
        Calendar calhoy = Calendar.getInstance(); // Obtenga un calendario utilizando la zona horaria y la configuración regional predeterminadas
        calhoy.setTime(new Date());
        calhoy.set(Calendar.HOUR_OF_DAY, -2);
        calhoy.set(Calendar.MINUTE, 0);
        calhoy.set(Calendar.SECOND, 0);
        calhoy.set(Calendar.MILLISECOND, 0);

        List<ImagenDetalle> imagenDetalles=imagenRepo.getImagenPendSyncsimple2(calhoy.getTime().getTime());

        if(imagenDetalles!=null&&imagenDetalles.size()>0){
                    for(ImagenDetalle imagen:imagenDetalles){
                        flog.grabarError(" subiendo a"+imagen.getDescripcion());

                        //subo cada una
                        Intent msgIntent = new Intent(getApplicationContext(), SubirFotoService.class);
                        msgIntent.putExtra(SubirFotoService.EXTRA_IMAGE_ID, imagen.getId());
                        msgIntent.putExtra(SubirFotoService.EXTRA_IMG_PATH,imagen.getRuta());
                        msgIntent.putExtra(SubirFotoService.EXTRA_INDICE,imagen.getIndice());

                        msgIntent.setAction(SubirFotoService.ACTION_UPLOAD_IMG);
                        getApplicationContext().startService(msgIntent);
                        //cambio su estatus a subiendo
                        imagen.setEstatusSync(1);

                        //  imagenRepo.insert(imagen);

                    }

                }



    }
    private void subirPendientes(){
        Intent msgIntent = new Intent(getApplicationContext(), SubirPendService.class);
        flog.grabarError("subiendo pendientes");
        msgIntent.setAction(SubirPendService.ACTION_UPLOAD_PEND);
        getApplicationContext().startService(msgIntent);
    }
      /*  public void sendNotification(String title, String message) {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            //If on Oreo then notification required a notification channel.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher);

            notificationManager.notify(1, notification.build());
        }*/

}
