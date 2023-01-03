package com.example.comprasmu.ui.home;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.comprasmu.DescargasIniAsyncTask;
import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.PruebaListener;
import com.example.comprasmu.R;
import com.example.comprasmu.SimpleTask;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.remote.RespInfEtapaResponse;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);

        progreso = new ProgressDialog(this);

        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progreso.setMessage("Actualizando, por favor permanezca en la aplicación...");

        progreso.setCancelable(false);


        progreso.show();

       // mTextView = findViewById(R.id.txtlllog);


        //todo va a cambiar, se definirá en el servidor
        definirIndice();


        descargasIniciales();
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
    private void definirIndice() {
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
        Log.d(TAG, "***** hoy " + mesactual);
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

        Constantes.INDICEACTUAL=ComprasUtils.indiceLetra(mesactual);
        // Constantes.INDICEACTUAL=mesactual.replace('-','.');
        Constantes.INDICEACTUAL = "10.2022";
        if(Constantes.CLAVEUSUARIO.equals("4")){
            Constantes.INDICEACTUAL = "6.2022";
        }

        Log.d(TAG, "***** indice " + Constantes.INDICEACTUAL);



    }

    public void descargasIniciales(){
        //pueda descargar
        //Inicio un servicio que se encargue de descargar
        CatalogoDetalleRepositoryImpl cdrepo=new CatalogoDetalleRepositoryImpl(getApplicationContext());
        TablaVersionesRepImpl tvRepo=new TablaVersionesRepImpl(getApplicationContext());

        AtributoRepositoryImpl atRepo=new AtributoRepositoryImpl(getApplicationContext());
        ListaCompraDao dao= ComprasDataBase.getInstance(getApplicationContext()).getListaCompraDao();
        ListaCompraDetRepositoryImpl lcdrepo=new ListaCompraDetRepositoryImpl(getApplicationContext());
        ListaCompraRepositoryImpl lcrepo=ListaCompraRepositoryImpl.getInstance(dao);
        SustitucionRepositoryImpl sustRepo=new SustitucionRepositoryImpl(getApplicationContext());
        GeocercaRepositoryImpl georep=new GeocercaRepositoryImpl(getApplicationContext());
        DescargasIniAsyncTask task = new DescargasIniAsyncTask(this,cdrepo,tvRepo,atRepo,lcdrepo,lcrepo,this,sustRepo,georep);

        task.execute("cat","");

        //descarga solicitudes compra
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


}