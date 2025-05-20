package com.example.comprasmu.workmanager;

import static java.util.concurrent.TimeUnit.SECONDS;

import android.content.Context;

import com.example.comprasmu.DescargasIniciales;
import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class DescargasAutomaticasControl {
    private static DescargasAutomaticasControl INSTANCE;
    static Context context;
    ScheduledFuture<?> handle;

    public static DescargasAutomaticasControl getInstance(Context contexto) {
        if (INSTANCE == null) {
           context=contexto;
            synchronized (Context.class) {
                if (INSTANCE == null) {
                    INSTANCE=new DescargasAutomaticasControl();
                }
            }
        }
        return INSTANCE;

    }
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void ejecutar() {
        final Runnable  runnable= new Runnable() {
            public void run() {
                DescargasIniciales descini=new DescargasIniciales(context);
                descini.ejecutar(); }
        };
         handle =scheduler.scheduleAtFixedRate(runnable, 0, 10, SECONDS);

    }

    public void detener(){
        handle.cancel(true);
       scheduler.shutdown();
    }
}
