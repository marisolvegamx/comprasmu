package com.example.comprasmu.data.remote;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.services.SubirFotoServiceAlt;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SubirFotoAlt {

    String URL_SUBIRPICTURE="https://muesmerc.mx/comprasv1/api/public/subirfoto";
    private final ArrayList<SubirFotoServiceAlt.SubirFotoListenerAlt> observadores = new ArrayList<SubirFotoServiceAlt.SubirFotoListenerAlt>();
    ImagenDetRepositoryImpl idrepo;
    private final String TAG="SubirFotoAlt";
    List<ImagenDetalle> imagenlist;
    int index;
    public SubirFotoAlt() {
        if (Build.PRODUCT.contains ("sdk")||Build.PRODUCT.contains ("A2016b30")){

            //nam
            URL_SUBIRPICTURE = "http://192.168.1.84/comprasv1/api/public/subirfoto";
           //URL_SUBIRPICTURE = "http://192.168.1.66/comprasv1/api/public/subirfoto";
          //  URL_SUBIRPICTURE = "https://muesmerc.mx/comprasv1/api/public/subirfoto";

        }else
        {
          //  URL_SUBIRPICTURE = "http://192.168.1.84/comprasv1/api/public/subirfoto";

            URL_SUBIRPICTURE = "https://muesmerc.mx/comprasv1/api/public/subirfoto";
        }

    }

    public void agregarObservador(SubirFotoServiceAlt.SubirFotoListenerAlt o)
    {
        observadores.add(o);

    }
    public void notificarObservadores()
    {
        // Enviar la notificación a cada observador a través de su propio método
        for (SubirFotoServiceAlt.SubirFotoListenerAlt obj : observadores) {
            obj.onSuccess();
        }
    }

    public void notificarAvance(int progress)
    {
        // Enviar la notificación a cada observador a través de su propio método
        for (SubirFotoServiceAlt.SubirFotoListenerAlt obj : observadores) {
            obj.onProgress(progress);
        }
    }



    public void subirFoto(String idusuario, String dir, ImagenDetalle imagen, String indice, Context context, ImagenDetRepositoryImpl idrepo) throws Exception {



        try {
            this.idrepo=idrepo;
                this.subirFotosi(idusuario,dir, imagen,indice,context,idrepo );
        } catch (Exception e) {
            throw e;
        }

    }

    public void subirFotosi(String idusuario, String dir, ImagenDetalle imagen, String indice, Context context, ImagenDetRepositoryImpl idrepo) {

//filenameGaleria=getFilename();

        try {
            this.idrepo=idrepo;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String uploadFileArrayList=dir + imagen.getRuta();
            Log.d(TAG,"ahora si voy a subir"+uploadFileArrayList);

            MultipartUploadRequest upload= new MultipartUploadRequest(context,URL_SUBIRPICTURE)
                    .setMaxRetries(2)
                    .addParameter ("ruta", imagen.getRuta())
                    // .addParameter ("descripcion1", (imagen.getId_descripcion()!=null&& imagen.getId_descripcion()!="")?imagen.getId_descripcion():"")
                    .addParameter ("idlocalim", imagen.getId() + "")
                    .addParameter ("idusuario", idusuario)
                    .addParameter("indice",indice)
                    // .addParameter("indice", imagen.getIndice());
                    //  .addParameter("recolector", recolector)
                    //  .addParameter("planta", planta)
                    .setUtf8Charset();
            upload.setDelegate(new UploadStatusDelegate(){

                @Override
                public void onProgress(UploadInfo uploadInfo) {
                    Log.d("SubirFoto","Subiendo foto....."+uploadFileArrayList);
                    notificarAvance(uploadInfo.getProgressPercent());
                }

                @Override
                public void onError(UploadInfo uploadInfo, Exception exception) {
                    Log.d("SubirFoto","Error al subir"+uploadInfo.getUploadedBytes());
                }

                @Override
                public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                    Log.d("ejemploimagen","Respuesta->"+serverResponse.getHttpCode());
                    Log.d("ejemploimagen","Respuesta->"+serverResponse.getBodyAsString());
                    if(serverResponse.getHttpCode()==200) {
                        actualizarEstado(imagen);
                    }else { //hubo un error
                        //lo registro en el log
                        Log.d("SubirFoto","Hubo un error al subir imagen "+serverResponse.getBodyAsString());
                    }
                    Log.d("SubirFoto", "terminó de subir");
                    notificarObservadores();

                }

                @Override
                public void onCancelled(UploadInfo uploadInfo) {

                    Log.d("ejemplo", "se cancelo");
                }


            });

            int i=0;
            try{

                upload.addFileToUpload(uploadFileArrayList, "file").setMethod("POST");
                upload.startUpload();
            } catch (FileNotFoundException e) {
                throw new Exception("No se encontró el archivo "+uploadFileArrayList+" Verifique");
            }
        } catch (Exception e) {
            e.printStackTrace();
            index++;
            if(index<imagenlist.size()) {
                subirFotosi(idusuario, dir, imagenlist.get(index), indice, context, idrepo);

            }
        }

    }


    public void actualizarEstado(ImagenDetalle imagen){
        //  for(Imagen imagen:lista){
        ImagenDetalle imagenedit=idrepo.findsimple(imagen.getId());
        imagenedit.setEstatusSync(2);
        idrepo.insert(imagenedit);


    }




}