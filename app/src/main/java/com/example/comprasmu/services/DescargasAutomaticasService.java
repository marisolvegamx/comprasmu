package com.example.comprasmu.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.comprasmu.DescargasIniciales;
import com.example.comprasmu.utils.ComprasLog;

public class DescargasAutomaticasService extends Service {
    private static final int INTERVALO = 10*1000; // 10seg
    private Handler handler;
    private Runnable runnable;
    private boolean ejecutando = false;
    private boolean pausado = false;
    ComprasLog flog;
    @Override
    public void onCreate() {
        super.onCreate();
        flog= ComprasLog.getSingleton();
        flog.grabarError("DescargasAutomaticasService","oncreate","iniciando ");
        DescAutomaticasServiceManager.getInstancia().setServicio(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!pausado) {
                    DescargasIniciales descini=new DescargasIniciales(getApplication());
                    descini.ejecutar();
                }
                handler.postDelayed(this, INTERVALO);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!ejecutando) {
            ejecutando = true;
            handler.post(runnable);
        }
        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        flog.grabarError("DescargasAutomaticasService","muriendo"," ");

        handler.removeCallbacks(runnable);
        ejecutando = false;
        handler = null;
        runnable = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void pausar() {
        flog.grabarError("DescargasAutomaticasService","pausando"," ");

        pausado = true;
    }

    public void reanudar() {
        pausado = false;
    }

    public void detenerServicio() {
        stopSelf();
    }

}
