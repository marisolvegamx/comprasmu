package com.example.comprasmu.data.remote;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.utils.ComprasLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*proceso que se usa actualmente 2025 con retrofit*/
public class SubirFoto implements ImageUploadCallback {

    private final ArrayList<SubirFotoService.SubirFotoListener> observadores = new ArrayList<SubirFotoService.SubirFotoListener>();
    ImagenDetRepositoryImpl idrepo;
    private final String TAG="SubirFoto";
    ImagenDetalle imagen;
    ComprasLog milog;
    public SubirFoto() {
        milog=ComprasLog.getSingleton();
    }

    public void agregarObservador(SubirFotoService.SubirFotoListener o)
    {
        observadores.add(o);

    }
    public void notificarObservadores()
    {
        // Enviar la notificación a cada observador a través de su propio método
        for (SubirFotoService.SubirFotoListener obj : observadores) {
            obj.onSuccess();
        }
    }
    public void notificarObservadoresIm(ImagenDetalle imagen)
    {
        // Enviar la notificación a cada observador a través de su propio método
        for (SubirFotoService.SubirFotoListener obj : observadores) {
            obj.onSuccess2(imagen);
        }
    }
    public void notificarAvance(int progress)
    {
        // Enviar la notificación a cada observador a través de su propio método
        for (SubirFotoService.SubirFotoListener obj : observadores) {
            obj.onProgress(progress);
        }
    }


    public void subirFoto( String idusuario,String dir, ImagenDetalle imagen,String indice,  Context context, ImagenDetRepositoryImpl idrepo) throws Exception {

        try {
            this.idrepo = idrepo;
            this.imagen=imagen;

             String uploadFileArrayList = dir + imagen.getRuta();
            milog.info(TAG,"subir foto", " ahora si voy a subir" + uploadFileArrayList);
            File file = new File(uploadFileArrayList);
            if(!file.exists()){
                throw new Exception("No se encontró el archivo");
            }
            ProgressRequestBody fileBody = new ProgressRequestBody(file, "image", this);
            /* Notice here the first argument in the createFormData function is the name of the key whose value will be the file you send */
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
            // MultipartBody.Part is used to send also the actual file name

            // add another part within the multipart request
            RequestBody ridlocalim =
                    RequestBody.create(MultipartBody.FORM,imagen.getId()+"" );
            RequestBody rruta =
                    RequestBody.create(MultipartBody.FORM,imagen.getRuta() );

            RequestBody rindice =
                    RequestBody.create(MultipartBody.FORM,indice );

            RequestBody rusuario =
                    RequestBody.create(MultipartBody.FORM,idusuario );


            Call<PostResponse> uploadImage = ServiceGeneratorIm.getApiService().uploadImage(filePart, rruta, ridlocalim, rusuario, rindice);

            uploadImage.enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                    milog.grabarError(TAG+ "subir foto Respuesta->" + response);
                    onSuccess(response);

                }



                @Override
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                   t.printStackTrace();
                    milog.grabarError(TAG+"subir foto error"+t.getMessage());
                    onError(null);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
            milog.grabarError(TAG+" error:"+e.getMessage());
            this.onError(null);

        }

    }

    public void subirFotoGen( String idusuario,String dir, ImagenDetalle imagen,String indice,  Context context,String tabla) throws Exception {

           //filenameGaleria=getFilename();

        try {
            ImagenDetalleDao imagenDetalleDao= ComprasDataBase.getInstance(context).getImagenDetalleDao();
            this.idrepo= ImagenDetRepositoryImpl.getInstance(imagenDetalleDao);
            this.imagen=imagen;
             String uploadFileArrayList = dir + imagen.getRuta();
            milog.info(TAG,"subirFotoGen", " ahora si voy a subirgen" + uploadFileArrayList);
            File file = new File(uploadFileArrayList);
            ProgressRequestBody fileBody = new ProgressRequestBody(file, "image", this);
            /* Notice here the first argument in the createFormData function is the name of the key whose value will be the file you send */
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
            // MultipartBody.Part is used to send also the actual file name

            // add another part within the multipart request
            RequestBody ridlocalim =
                    RequestBody.create(MultipartBody.FORM, imagen.getId() + "");
            RequestBody rruta =
                    RequestBody.create(MultipartBody.FORM, imagen.getRuta());

            RequestBody rindice =
                    RequestBody.create(MultipartBody.FORM, indice);

            RequestBody rusuario =
                    RequestBody.create(MultipartBody.FORM, idusuario);


            Call<PostResponse> uploadImage = ServiceGeneratorIm.getApiService().uploadImage(filePart, rruta, ridlocalim, rusuario, rindice);

            uploadImage.enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                    milog.grabarError(TAG+ " subir fotogen Respuesta->" + response);
                    onSuccessGen(response);

                }


                @Override
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                    Log.e(TAG,"hubo un error al subir"+t.getMessage());
                    milog.grabarError("SubirFoto"+ " subir foto gen Error al subir"+t.getMessage());

                    onErrorGen(null);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SubirFoto","Error al subir....."+e.getMessage());

            milog.grabarError(TAG,"Error al subir", "Error al subir"+e.getMessage());

            this.onError(null);

        }
    }
    @Override
    public void onProgressUpdate(int percentage) {
        // set current progress
        Log.i("SubirFoto","Subiendo foto....."+percentage);

    }
    @Override
    public void onError(Response<PostResponse> response) {
        milog.grabarError(TAG,"SubirFoto", "Error al subir"+response);
        if(response!=null) {
            PostResponse compraResp = response.body();
            milog.grabarError(TAG,"SubirFoto", "Error al subir" + compraResp.getData());
        }
        notificarObservadores();
    }
    @Override
    public void onSuccess(Response<PostResponse> response) {
        milog.info(TAG, "onSuccess","Respuesta->" + response);
        if (response != null) {
            milog.info(TAG, "onSuccess","Respuesta->" + response.message());
            PostResponse compraResp = response.body();
            // do something on upload finished
            //for example, start next uploading at the queue
            if (response.isSuccessful() && compraResp != null) {

                milog.info(TAG, "onSuccess","Respuesta->" + compraResp.getData());
                //todo descomentar
                 actualizarEstado(imagen);
            } else { //hubo un error
                //lo registro en el log
                if (compraResp != null) {
                    milog.grabarError(TAG,"SubirFoto", "Hubo un error al subir imagen " + compraResp.getData());
                }
            }
            milog.grabarError(TAG,"SubirFoto", "terminó de subir");
        }
        notificarObservadores();


    }

    @Override
    public void onErrorGen(Response<PostResponse> response) {
        if(response!=null) {
            PostResponse compraResp = response.body();
            milog.grabarError("SubirFoto"+ "Error al subir" + compraResp.getData());
        }
        notificarObservadores();
    }

    @Override
    public void onSuccessGen(Response<PostResponse> response) {

        if (response != null) {
            milog.grabarError(TAG+ "Respuesta->" + response.message());
            PostResponse compraResp = response.body();
            // do something on upload finished
            //for example, start next uploading at the queue
            if (response.isSuccessful() && compraResp != null) {

                milog.grabarError(TAG+ "Respuesta->" + compraResp.getData());
               // actualizarEstado(imagen);
                notificarObservadoresIm(imagen);
            }
        }else { //hubo un error
            milog.grabarError("SubirFoto"+"Hubo un error al subir imagen "+response.body());
            notificarObservadoresIm(null);

        }
    }

    public void actualizarEstado(ImagenDetalle imagen){
        //  for(Imagen imagen:lista){
        ImagenDetalle imagenedit=idrepo.findsimple(imagen.getId());
        imagenedit.setEstatusSync(2);
        idrepo.insert(imagenedit);


    }


    // Nuevo método para WorkManager
    public boolean subirFotoSync(String idusuario, String dir, ImagenDetalle imagen, String indice, Context context) {
        try {
            String uploadFileArrayList = dir + imagen.getRuta();
            milog.info(TAG,"subir foto", " ahora si voy a subir" + uploadFileArrayList);
            Log.d(TAG,"subir foto"+uploadFileArrayList+"--"+idusuario);
            File file = new File(uploadFileArrayList);
            if(!file.exists()){
                throw new Exception("No se encontró el archivo");
            }

          //  RequestBody fileBody = RequestBody.create(MultipartBody.FORM, file);
            ProgressRequestBody fileBody = new ProgressRequestBody(file, "image", this);

            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

            // MultipartBody.Part is used to send also the actual file name

            // add another part within the multipart request
            RequestBody ridlocalim =
                    RequestBody.create(MultipartBody.FORM,imagen.getId()+"" );
            RequestBody rruta =
                    RequestBody.create(MultipartBody.FORM,imagen.getRuta() );

            RequestBody rindice =
                    RequestBody.create(MultipartBody.FORM,indice );

            RequestBody rusuario =
                    RequestBody.create(MultipartBody.FORM,idusuario );


            // EJECUCIÓN SÍNCRONA
            Response<PostResponse> response = ServiceGeneratorIm.getApiService()
                    .uploadImage(filePart, rruta, ridlocalim, rusuario, rindice)
                    .execute();

            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SubirFoto","Error al subir....."+e.getMessage());

            milog.grabarError("SubirFotoSync Error: " + e.getMessage());
            return false;
        }
    }



}
