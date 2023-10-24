package com.example.comprasmu.data.remote;

import android.content.Context;
import android.os.Build;
import android.util.Log;


import androidx.annotation.NonNull;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.services.SubirFotoService;


import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubirFoto implements ImageUploadCallback {

    private final ArrayList<SubirFotoService.SubirFotoListener> observadores = new ArrayList<SubirFotoService.SubirFotoListener>();
    ImagenDetRepositoryImpl idrepo;
    private final String TAG="SubirFoto";
    ImagenDetalle imagen;
    public SubirFoto() {

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
           // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String uploadFileArrayList = dir + imagen.getRuta();
            Log.d(TAG, "ahora si voy a subir" + uploadFileArrayList);
            File file = new File(uploadFileArrayList);
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
                    onSuccess(response);

                }



                @Override
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                    onError(null);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

            this.onSuccess(null);

        }

    }

    public void subirFotoGen( String idusuario,String dir, ImagenDetalle imagen,String indice,  Context context,String tabla) throws Exception {

        //filenameGaleria=getFilename();

        try {
            this.idrepo = new ImagenDetRepositoryImpl(context);
            this.imagen=imagen;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String uploadFileArrayList = dir + imagen.getRuta();
            Log.d(TAG, "ahora si voy a subirgen" + uploadFileArrayList);
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
                    onSuccessGen(response);

                }


                @Override
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                    onErrorGen(null);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

            this.onSuccess(null);

        }
    }
    @Override
    public void onProgressUpdate(int percentage) {
        // set current progress
        Log.d("SubirFoto","Subiendo foto....."+percentage);

    }
    @Override
    public void onError(Response<PostResponse> response) {
        if(response!=null) {
            PostResponse compraResp = response.body();
            Log.d("SubirFoto", "Error al subir" + compraResp.getData());
        }
        notificarObservadores();
    }
    @Override
    public void onSuccess(Response<PostResponse> response) {
        if (response != null) {
            Log.d(TAG, "Respuesta->" + response.message());
            PostResponse compraResp = response.body();
            // do something on upload finished
            //for example, start next uploading at the queue
            if (response.isSuccessful() && compraResp != null) {

                Log.d(TAG, "Respuesta->" + compraResp.getData());
                //todo descomentar
                actualizarEstado(imagen);
            } else { //hubo un error
                //lo registro en el log
                if (compraResp != null) {
                    Log.d("SubirFoto", "Hubo un error al subir imagen " + compraResp.getData());
                }
            }
            Log.d("SubirFoto", "terminó de subir");
        }
        notificarObservadores();


    }

    @Override
    public void onErrorGen(Response<PostResponse> response) {
        if(response!=null) {
            PostResponse compraResp = response.body();
            Log.d("SubirFoto", "Error al subir" + compraResp.getData());
        }
        notificarObservadores();
    }

    @Override
    public void onSuccessGen(Response<PostResponse> response) {
        if (response != null) {
            Log.d(TAG, "Respuesta->" + response.message());
            PostResponse compraResp = response.body();
            // do something on upload finished
            //for example, start next uploading at the queue
            if (response.isSuccessful() && compraResp != null) {

                Log.d(TAG, "Respuesta->" + compraResp.getData());
                notificarObservadoresIm(imagen);
            }
        }else { //hubo un error
            notificarObservadoresIm(null);
            Log.d("SubirFoto","Hubo un error al subir imagen "+response.body());
        }
    }

    public void actualizarEstado(ImagenDetalle imagen){
        //  for(Imagen imagen:lista){
        ImagenDetalle imagenedit=idrepo.findsimple(imagen.getId());
        imagenedit.setEstatusSync(2);
        idrepo.insert(imagenedit);


    }




}
