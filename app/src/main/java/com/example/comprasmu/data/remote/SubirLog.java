package com.example.comprasmu.data.remote;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.services.SubirLogService;
import com.example.comprasmu.utils.Constantes;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class SubirLog {
    MultipartUploadRequest upload;
    String URL_SUBIRPICTURE="api/public/subirlog";
    private SubirLogService.SubirLogListener observadores;
    ImagenDetRepositoryImpl idrepo;
    private final String TAG="SubirLog";

   public void agregarObservador(SubirLogService.SubirLogListener obs){
       observadores=obs;
   }
    public void notificarObservadores(String mensaje)
    {
        // Enviar la notificación a cada observador a través de su propio método

        observadores.onSuccess();

    }

    public void notificarAvance(int progress)
    {
        // Enviar la notificación a cada observador a través de su propio método

        observadores.onProgress(progress);

    }



    public void subirlog( String idusuario,String arch, String nombrearch, Context context) throws Exception {

//filenameGaleria=getFilename();

        try {
            this.idrepo=idrepo;

            String uploadFileArrayList=arch;
            Log.d(TAG,"ahora si voy a subir"+uploadFileArrayList+"  "+Constantes.URLSERV+URL_SUBIRPICTURE);
           String serverurl="";
            if (Build.PRODUCT.contains ("sdk")||Build.MODEL.contains ("2006C3MG2")) {//pruebas y el lenovo
                serverurl=Constantes.URLPRUEBAS1;

            }else
            {
                serverurl=Constantes.URLSERV;

            }
                upload= new MultipartUploadRequest(context, serverurl+URL_SUBIRPICTURE)
                    .setMaxRetries(2)
                     .addParameter ("usuario", idusuario)
                    .addParameter ("archivo", nombrearch)
                      .setUtf8Charset();
            upload.setDelegate(new UploadStatusDelegate(){

                @Override
                public void onProgress(UploadInfo uploadInfo) {
                    Log.d(TAG,"Subiendo log....."+uploadFileArrayList);
                    notificarAvance(uploadInfo.getProgressPercent());
                }

                @Override
                public void onError(UploadInfo uploadInfo, Exception exception) {
                    Log.d(TAG,"Error al subir"+uploadInfo.getUploadedBytes());
                }

                @Override
                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                   String mensaje="TODO BIEN";
                    if(serverResponse.getHttpCode()==200) {
                        Log.d(TAG,"todo bien");

                    }else { //hubo un error
                        Log.d(TAG,"HUBO UN ERROR");
                        //lo registro en el log
                        mensaje="HUBO UN ERROR";
                            }

                      notificarObservadores(mensaje);


                }

                @Override
                public void onCancelled(UploadInfo uploadInfo) {
                    Log.d("ejemplo", "se cancelo");
                }


            });



            addFileToUploadRequest(uploadFileArrayList,1);

            upload.startUpload();
        } catch (Exception e) {
            throw e;
        }

    }



    public void addFileToUploadRequest(String uf,int i) throws Exception {
        try {
            upload.addFileToUpload(uf, "file").setMethod("POST");
        } catch (FileNotFoundException e) {
            throw new Exception("No se encontró el archivo "+uf+" Verifique");
        }
    }


}
