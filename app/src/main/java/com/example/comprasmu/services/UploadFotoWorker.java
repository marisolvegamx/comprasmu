package com.example.comprasmu.services;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.remote.SubirFoto;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.utils.Constantes;


public class UploadFotoWorker extends Worker {
    public static final String EXTRA_IMG_PATH = "com.example.comprasmu.intentservice.extra.EXTRA_IMG_PATH";
    public static final String EXTRA_IMAGE_ID = "com.example.comprasmu.intentservice.extra.EXTRA_IMAGE_ID";
    public static String EXTRA_INDICE="comprasmu.extraindice";
    ImagenDetalle imagenSubir;
    String indiceimagen;
    public UploadFotoWorker(@NonNull Context context, @NonNull WorkerParameters params) {
            super(context, params);
        }

        @NonNull
        @Override
        public Result doWork() {
            // Leer datos enviados al worker

            imagenSubir=new ImagenDetalle();
            imagenSubir.setRuta(getInputData().getString(EXTRA_IMG_PATH));
            imagenSubir.setId(getInputData().getInt(EXTRA_IMAGE_ID,0));
            indiceimagen=getInputData().getString(EXTRA_INDICE);

            Context ctx = getApplicationContext();
            ImagenDetRepositoryImpl repo = ImagenDetRepositoryImpl.getInstance(
                    ComprasDataBase.getInstance(ctx).getImagenDetalleDao());

            SubirFoto sf = new SubirFoto();
            String dir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/";

            // Intentar subida
            boolean exito = sf.subirFotoSync(Constantes.CLAVEUSUARIO, dir, imagenSubir, indiceimagen, ctx);

            if (exito) {
                // REGISTRO EN BD LOCAL (ÉXITO)
                repo.actualizarEstatusSync(imagenSubir.getId(), 2); // 2 = Enviado
                return Result.success();
            } else {
                // Si falla, WorkManager puede reintentar según la configuración
                return Result.retry();
            }
        }

}
