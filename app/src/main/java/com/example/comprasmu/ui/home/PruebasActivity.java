package com.example.comprasmu.ui.home;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.example.comprasmu.DescargasIniAsyncTask;
import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.remote.EtapaResponse;
import com.example.comprasmu.data.remote.RespInfEtapaResponse;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.ui.login.LoginActivity;
import com.example.comprasmu.ui.mantenimiento.BorrarActivity;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PruebasActivity  extends AppCompatActivity  implements    DescargasIniAsyncTask.ProgresoListener  {

   ProgressDialog progreso;
    String TAG="PruebasActivity";

    private static final String DOWNLOAD_PATH = "https://muesmerc.mx/comprasv1/fotografias";
    private   String DESTINATION_PATH ;
    SimpleDateFormat sdfparaindice=new SimpleDateFormat("M-yyyy");
    private int etapapref;
    private String indicepref;
    private int etapafinpref;
    private int tiporec;
    private boolean puedodescargar;
    private ComprasLog complog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);
        complog= ComprasLog.getSingleton();
        complog.crearLog(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath());

        progreso = new ProgressDialog(this);

        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progreso.setMessage("Actualizando, por favor permanezca en la aplicación...");

        progreso.setCancelable(false);


        progreso.show();

       // mTextView = findViewById(R.id.txtlllog);


        //se definirá en el servidor
        definirTienda();
        if(!isOnlineNet()) { //no hay conexion trabajo conl lo que hay
            getEtapaPref();
            if(!indicepref.equals("")&&etapafinpref>0){
                Constantes.INDICEACTUAL = indicepref;
                Constantes.ETAPAMENU = etapapref;
                descargasIniciales(indicepref, etapapref, etapafinpref);
                return;
            }else {
                progreso.setMessage("Necesita conexión a internet para actualizar la aplicación");

                progreso.setCancelable(true);
                cerrarSesion();
                return;
            }
        }

        buscarEtapa();


     //   descargasIniciales();
     //   previewView = findViewById(R.id.activity_main_previewView);

      //  cameraProviderFuture = ProcessCameraProvider.getInstance(this);


    }



    public void success() {
        //pasaría a otra actividad
        progreso.dismiss();
        Constantes.ACTUALIZADO=true;
        //Intent intento=new Intent(this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Intent intento=new Intent(this, HomeActivity.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intento);
        finish();
    }
    public void cerrarSesion(){
        Constantes.LOGGEADO=false;
        Intent intento=new Intent(this, LoginActivity.class);
        intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intento);
        finish();
    }
    private void definirTienda() {
        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);

        Constantes.CLAVEUSUARIO = prefe.getString("claveusuario", "");

        Constantes.TIPOTIENDA=new HashMap<>();

        Constantes.TIPOTIENDA.put(1,getString(R.string.grande));
        Constantes.TIPOTIENDA.put(2,getString(R.string.mediana));
        Constantes.TIPOTIENDA.put(3,getString(R.string.chica));
        Constantes.TIPOTIENDA.put(4,getString(R.string.otras));
        //obtengo solo mes

        Calendar cal = Calendar.getInstance(); // Obtenga un calendario utilizando la zona horaria y la configuración regional predeterminadas
        Date hoy=new Date();
        cal.setTime(hoy);
        cal.add(Calendar.MONTH, +1);
        String mesactual = sdfparaindice.format(cal.getTime());

        String[] aux = mesactual.split("-");
        int mes = Integer.parseInt(aux[0])+1;
        int anio = Integer.parseInt(aux[1]);

        Constantes.listaindices = new String[4];
        int j = 3;
        int nuevomes = mes;
        for (int i = 1; i < 5; i++) {
            Constantes.listaindices[j] = ComprasUtils.mesaLetra(nuevomes + "") + " " + anio + "";

            nuevomes = nuevomes - 1;
            if (nuevomes == 0) //empezo en 1
            {
                nuevomes = 12;
                anio = anio - 1;
            }
            j--;
        }


        // Constantes.INDICEACTUAL=mesactual.replace('-','.');
     /*   Constantes.INDICEACTUAL = "11.2022";
        if(Constantes.CLAVEUSUARIO.equals("4")){
            Constantes.INDICEACTUAL = "11.2022";
        }*/




    }
    public void buscarEtapa(){

        getEtapaPref();
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        EtapaListener listener=new EtapaListener();
        ps.getEtapaAct(listener);

    }
    public void validarBorrar(String indicenvo, int etapanva,int etapafin, int tiporec){
      Log.d(TAG,"en valdar borrar"+indicepref);
       if(indicepref!=null&&!indicepref.equals("")) {
           if (!indicenvo.equals(indicepref)) {
               //cambie de indice
               //veo si es la ultima etapa y puedo borrar
               //todo revisar la etapaactual de todas las plantas y si todas == a etapa fin
               if (etapanva == etapafinpref) {
                   //voy a borrar datos
                   //por si no quiere borrar
                   Constantes.INDICEACTUAL = indicepref;
                   Constantes.ETAPAMENU =etapapref ;
                   irABorrar(); // necesito ir a una actividad donde pregunte al usuario
               } else {
                   //todo avisar al usuario que hubo un error no descargar
                   puedodescargar = true;
                   Constantes.INDICEACTUAL = indicepref;
                  // Constantes.ETAPAMENU = etapanva;
                   //envio etapa act y etapaini
                   descargasIniciales(indicenvo, etapanva, etapafin);

               }
           } else {
               //actualizo en prefs asigno constantes y sigo
               puedodescargar = true;
               guardarEtapaPref(etapanva, indicenvo, etapafin,tiporec);
               Constantes.INDICEACTUAL = indicenvo;
               Constantes.ETAPAMENU = etapanva;
               descargasIniciales(indicenvo, etapanva, etapafin);

           }
       }//es 1a vez y ya puede descargar
        else{
            Log.d(TAG,etapanva+"--"+ indicenvo+"--"+ etapafin);
           puedodescargar = true;
           guardarEtapaPref(etapanva, indicenvo, etapafin,tiporec);
           Constantes.INDICEACTUAL = indicenvo;
           Constantes.ETAPAMENU = etapanva;
           descargasIniciales(indicenvo, etapanva, etapafin);
       }
    }
    public void getEtapaPref(){
        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        etapapref= prefe.getInt("etapaact", 0);
        indicepref= prefe.getString("indiceact", "");
        etapafinpref= prefe.getInt("etapafin", 0);
        tiporec= prefe.getInt("tiporec", 0);
    }
    public void guardarEtapaPref(int etapa, String indice, int etapafin, int tiporec){
        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        editor.putInt("tiporec",tiporec );
        editor.putInt("etapaact",etapa );
        editor.putString("indiceact", indice);
        editor.putInt("etapafin", etapafin);
        editor.commit();

    }
    //todo
    private void irABorrar(){
        Log.d(TAG,"aqui borro");
        Intent intento=new Intent(this, BorrarActivity.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intento.putExtra(BorrarActivity.INDICEACT,indicepref);
        startActivity(intento);
        finish();
    }



    public void descargasIniciales(String indicenvo, int etapanva, int etapafin){
        //pueda descargar
        //saber si voy a borrar
        Log.d(TAG, "***** indice " + Constantes.INDICEACTUAL+"--"+Constantes.ETAPAMENU);

        ListaCompraDao dao= ComprasDataBase.getInstance(getApplicationContext()).getListaCompraDao();

        ListaCompraRepositoryImpl lcrepo=ListaCompraRepositoryImpl.getInstance(dao);
        List<ListaCompra> lista=lcrepo.getAllsimple();
        if(lista!=null&&lista.size()>0){
            String indicelis=lista.get(0).getIndice();
            if(indicelis.equals(indicenvo)){
                puedodescargar=true;

            }else{
                //cambie de indice, tengo que borrar
                //todo revisar la etapaactual de todas las plantas y si todas == a etapa fin

                if(etapanva==etapafin){
                    //voy a borrar datos
                    irABorrar(); //todo necesito ir a una actividad donde pregunte al usuario
                    return;
                }else
                {
                 //sigo con el mismo indice y lista
                    puedodescargar=true;


                }
            }
        }
        Log.d(TAG, "indice " + Constantes.INDICEACTUAL+"--"+Constantes.ETAPAMENU);

        //Inicio un servicio que se encargue de descargar
        //catalogos listas de compra y respaldos de informes informes etapas y correcciones
        CatalogoDetalleRepositoryImpl cdrepo=new CatalogoDetalleRepositoryImpl(getApplicationContext());
        TablaVersionesRepImpl tvRepo=new TablaVersionesRepImpl(getApplicationContext());

        AtributoRepositoryImpl atRepo=new AtributoRepositoryImpl(getApplicationContext());
         ListaCompraDetRepositoryImpl lcdrepo=new ListaCompraDetRepositoryImpl(getApplicationContext());
         SustitucionRepositoryImpl sustRepo=new SustitucionRepositoryImpl(getApplicationContext());
        GeocercaRepositoryImpl georep=new GeocercaRepositoryImpl(getApplicationContext());
        DescargasIniAsyncTask task = new DescargasIniAsyncTask(this,cdrepo,tvRepo,atRepo,lcdrepo,lcrepo,this,sustRepo,georep,puedodescargar);

        task.execute("cat","");

        //descarga solicitudes correccion
        //se hace en el navigationdrawer por cada etapa
        //   SolicitudCorRepoImpl solcorRepo=new SolicitudCorRepoImpl(getApplicationContext());

        //  DescCorrecAsyncTask corTask=new DescCorrecAsyncTask(solcorRepo,tvRepo,this,Constantes.ETAPAACTUAL,Constantes.INDICEACTUAL);
        //  corTask.execute("");
      /*  AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.stat_sys_download);
        builder.setTitle("Descargando");
        builder.setMessage("Por favor mantengase en la aplicación hasta que termine la descarga");
        builder.setInverseBackgroundForced(true);

        AlertDialog alert=builder.create();
        alert.show();*/

      /*  Dialog builder = new Dialog(act);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setCancelable(false);
*/

    }


    @Override
    public void todoBien(RespInfEtapaResponse maininfoetaResp, RespInformesResponse maininfoResp, List<Correccion> mainRespcor) {
        if (maininfoResp!=null&&maininfoResp.getImagenDetalles() != null && maininfoResp.getImagenDetalles().size() > 0) {

            descargarImagenes(maininfoResp.getImagenDetalles());

        }
        imagenesEtapa(maininfoetaResp);
        imagenesCor(mainRespcor);
        success();
    }




    public void imagenesCor(List<Correccion> infoResp) {
        if (infoResp!=null&& infoResp.size() > 0) {
            for(Correccion img:infoResp){
                startDownload(DOWNLOAD_PATH+"/"+Constantes.INDICEACTUAL.replace(".","_")+"/"+img.getRuta_foto1(), DESTINATION_PATH);
                startDownload(DOWNLOAD_PATH+"/"+Constantes.INDICEACTUAL.replace(".","_")+"/"+img.getRuta_foto2(), DESTINATION_PATH);
                startDownload(DOWNLOAD_PATH+"/"+Constantes.INDICEACTUAL.replace(".","_")+"/"+img.getRuta_foto3(), DESTINATION_PATH);
                // Log.d(TAG," **descargando "+DOWNLOAD_PATH+"/"+img.getRuta_foto1());
            }
            // cerrarAlerta(true);


        }
    }

    public void imagenesEtapa(RespInfEtapaResponse infoResp) {
        if (infoResp!=null&&infoResp.getInformeEtapaDet() != null && infoResp.getInformeEtapaDet().size() > 0) {

            for(InformeEtapaDet img:infoResp.getInformeEtapaDet()){
                startDownload(DOWNLOAD_PATH+"/"+ Constantes.INDICEACTUAL.replace(".","_")+"/"+img.getRuta_foto(), DESTINATION_PATH);
                Log.d(TAG," **descargando "+DOWNLOAD_PATH+"/"+img.getRuta_foto());
            }
            // cerrarAlerta(true);


        }


    }
    private void descargarImagenes(List<ImagenDetalle> imagenes){
        for(ImagenDetalle img:imagenes){
            startDownload(DOWNLOAD_PATH+"/"+img.getIndice().replace(".","_")+"/"+img.getRuta(), DESTINATION_PATH);
            Log.d(TAG," descargando "+DOWNLOAD_PATH+"/"+img.getIndice().replace(".","_")+"/"+img.getRuta());
        }
        // cerrarAlerta(true);
    }

    long archact;
    private long startDownload(String downloadPath, String destinationPath) {
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        // registrer receiver in order to verify when download is complete
        //  registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle("Downloading a file"); // Title for notification.
        request.setVisibleInDownloadsUi(true);

        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, uri.getLastPathSegment());  // Storage directory path
        archact=((DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
        return 0;

    }

    @Override
    public void notificarSinConexion() {
        //pasaría a otra actividad
      /*  progreso.dismiss();
        TextView sincon=findViewById(R.id.destxtsincon);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sincon.setVisibility(View.VISIBLE);
            }
        });*/
        success();

       // Intent intento=new Intent(this, HomeActivity.class);

      //  startActivity(intento);
       // finish();
    }
    public class EtapaListener {


        public EtapaListener() {

        }

        public void validarEtapa(EtapaResponse response) {

            if (response != null) {
                Log.e(TAG,response.getIndiceact()+"--"+response.getEtapaact()+"--"+response.getEtapafin());
               // if (response.getEtapaact() > 0) {
                    //validar si cambio de indice y borro
                    validarBorrar(response.getIndiceact(), response.getEtapaact(), response.getEtapafin(),response.getTiporec());
               // } else {
                    // es 1a vez descargo pero la validación se hace en el menu
               //     puedodescargar = true;
                 //   Constantes.INDICEACTUAL = response.getIndiceact();
                 //   Constantes.ETAPAMENU = 0;
                 //   descargasIniciales(response.getIndiceact(), 0, response.getEtapafin());

              //  }

            }
            else
                notificarSinConexion();
        }
    }
    public static Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }


    }