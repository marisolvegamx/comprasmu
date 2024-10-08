package com.example.comprasmu.workmanager;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.comprasmu.DescargasIniciales;
import com.example.comprasmu.utils.ComprasLog;



public class NotificacionesWork extends Worker {

    final String TAG="NotificacionesWork";
    ComprasLog flog;
    public NotificacionesWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
        @Override
        public Result doWork() {

       flog= ComprasLog.getSingleton();
        flog.grabarError("iniciando NotificacionesWork ");
        descargarNotificaciones();
        return Result.success();
        }

    public void descargarNotificaciones( ){
        DescargasIniciales descini=new DescargasIniciales(super.getApplicationContext());
        descini.ejecutar();


    }

}
