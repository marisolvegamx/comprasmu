package com.example.comprasmu.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.remote.SubirFoto;
import com.example.comprasmu.data.remote.SubirFotoAlt;
import com.example.comprasmu.data.remote.SubirFotoRetro;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.ui.informe.PostInformeViewModel;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/****un servicio x foto****/
public class SubirFotoServiceAlt extends IntentService
{

    private static final String TAG = "SubirFotoServiceAlt";
    public static String EXTRA_INDICE="comprasmu.extraindice";
    InfEtapaDetRepoImpl etapadetRepo;
    CorreccionRepoImpl correccionRepo;
    ComprasLog milog;
    String userId, indiceimagen, cadenarutas;

    PostInformeViewModel pvm;
    private String tipo; //para saber que tabla actualizare


    public static final String ACTION_UPLOAD_LISTA = "com.example.comprasmu.intentservice.action.lista";

    public static final String EXTRA_IMG_PATH = "com.example.comprasmu.intentservice.extra.EXTRA_IMG_PATH";
    public static final String EXTRA_IMAGE_ID = "com.example.comprasmu.intentservice.extra.EXTRA_IMAGE_ID";

    int index;
    SubirFotoListenerAlt objObservador;
    List<ImagenDetalle> listaimagenes;
    String dir;
    public SubirFotoServiceAlt()
    {
        super("SubirFotoServiceAlt");
        pvm=new PostInformeViewModel(getApplication());
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        milog=ComprasLog.getSingleton();
      //  Log.d(TAG,"action");
        if (intent != null)
        {
            final String action = intent.getAction();
           // intent.setAction(ACTION_UPLOAD_IMG);

           if (ACTION_UPLOAD_LISTA.equals(action)) //para informe compra foto en fila
            {
                //  Log.d(TAG,"action"+action);
              //  imagenSubir=new ImagenDetalle();
                cadenarutas=intent.getStringExtra(EXTRA_IMG_PATH);
          //      imagenSubir.setId(intent.getIntExtra(EXTRA_IMAGE_ID,0));
                indiceimagen=intent.getStringExtra(EXTRA_INDICE);
                handleUploadImgFila();
            }
        }
    }

    //para subirlas en fila
    private void handleUploadImgFila()
    {

        // Instanciar y registrar un Observador
         objObservador  = new SubirFotoListenerAlt();

        try {
            Log.d(TAG,"cadena "+cadenarutas);
            if(this.cadenarutas.length()>0) { //o sea trae algo

                String cadenarutas = this.cadenarutas;
                String partes[] = cadenarutas.split("¬");
                 listaimagenes = new ArrayList<ImagenDetalle>();

                this.index=0;


                if(partes!=null&&partes.length>0)
             for(int i=1;i<partes[i].length();i++) {
                        ImagenDetalle imagen = new ImagenDetalle();
                        imagen.setId(i);
                        imagen.setIndice(indiceimagen);
                        imagen.setRuta(partes[i]);
                        listaimagenes.add(imagen);
                }

                if(listaimagenes!=null&&listaimagenes.size()>0) {
                    // notificar();
                  //  SubirFotoAlt sf = new SubirFotoAlt();
                    SubirFotoRetro sf = new SubirFotoRetro();

                    sf.agregarObservador(objObservador);
                    //   Log.d(TAG,"ahora si voy a subir*"+imagenSubir.getRuta());
                    dir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/";

                    ImagenDetRepositoryImpl idrepo = new ImagenDetRepositoryImpl(this);
                    sf.subirFoto(Constantes.CLAVEUSUARIO, dir, listaimagenes.get(0), indiceimagen, this, idrepo);
                }

                // pvm.actualizarEstatusFoto(imagenSubir);
                // Thread.sleep(10000);
                // enviarOtra();
            }
        }catch (Exception ex){

            Log.e(TAG,"error"+ex.getMessage());
            ex.printStackTrace();
            milog.grabarError(TAG+".handleUploadImgFila "+ex.getMessage());
        }


    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        //Log.e(TAG,"onCreate");
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
     //   Log.e(TAG,"onDestroy");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //notificationManager.cancel(0);
    }




    public class SubirFotoListenerAlt {

        boolean downloadComplete = false;

        public SubirFotoListenerAlt() {


        }


        public void onProgress(int progress)
        {
           // updateNotification(progress);
        }
        public void onSuccess(){

            downloadComplete = true;
          //  onDownloadComplete(downloadComplete);
            index++;

            if(index<listaimagenes.size()) {
               // SubirFotoAlt sf = new SubirFotoAlt();
                SubirFotoRetro sf = new SubirFotoRetro();
                sf.agregarObservador(objObservador);
                //   Log.d(TAG,"ahora si voy a subir*"+imagenSubir.getRuta());

                ImagenDetRepositoryImpl idrepo = new ImagenDetRepositoryImpl(SubirFotoServiceAlt.this);
                try {
                    sf.subirFoto(Constantes.CLAVEUSUARIO, dir, listaimagenes.get(index), indiceimagen, SubirFotoServiceAlt.this, idrepo);
                } catch (Exception e) {
                    Log.e(TAG,"error"+e.getMessage());
                    e.printStackTrace();
                    milog.grabarError(TAG+".onSuccess "+e.getMessage());
                }

               /* SubirFotoRetro sf = new SubirFotoRetro();
                sf.agregarObservador(objObservador);
                //   Log.d(TAG,"ahora si voy a subir*"+imagenSubir.getRuta());

                ImagenDetRepositoryImpl idrepo = new ImagenDetRepositoryImpl(SubirFotoServiceAlt.this);
                try {
                    sf.subirFoto(Constantes.CLAVEUSUARIO, dir, listaimagenes.get(index), indiceimagen, SubirFotoServiceAlt.this, idrepo);
                } catch (Exception e) {
                    Log.e(TAG,"error"+e.getMessage());
                    e.printStackTrace();
                    milog.grabarError(TAG+".onSuccess "+e.getMessage());
                }*/


            }
        }
        public void onSuccess2(ImagenDetalle imagen){
            downloadComplete = true;
           if(tipo.equals("correccion"))
                //actualizo correccion
            correccionRepo.actualizarEstatusSync(imagen.getId(), Constantes.ENVIADO);
           else
               if(tipo.equals("etapa")){
                   etapadetRepo.actualizarEstatusSync(imagen.getId(), Constantes.ENVIADO);
               }
            //  onDownloadComplete(downloadComplete);

        }
        public void onSuccessEtapa(ImagenDetalle imagen){
            downloadComplete = true;

            //actualizo correccion

            //  onDownloadComplete(downloadComplete);

        }



    }

}
