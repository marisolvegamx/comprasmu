package com.example.comprasmu.ui.gasto;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.comprasmu.R;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.AcuseRecibo;
import com.example.comprasmu.data.remote.PostResponse;
import com.example.comprasmu.data.repositories.AcuseReciboRepositoryImpl;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Preguntasino;

public class RevReciboActivity extends AppCompatActivity {
    Button btndescargar;
    int estatusRecibo; //1- está listo 0 -no
    LinearLayout llprin, llpregunta;
    int estatusAceptado;//0 no ,1 -si
    private Toolbar myChildToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rev_recibo);
        String BASE_URL;
        //todo necesito saber si ya está aceptado para que no lo vuelva a pedir
        Button btnenviar=findViewById(R.id.btnrrenviar);
        TextView comentarios=findViewById(R.id.txtrrcomentarios);
        Preguntasino pregunta=findViewById(R.id.rrsinoacuerdo);

        // Enable the Up button
        myChildToolbar =
                findViewById(R.id.rrtoolbarinf);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        myChildToolbar.setTitle("REVISAR RECIBO");
        // Enable the Up button
      //  ab.setDisplayHomeAsUpEnabled(true);
        btndescargar=findViewById(R.id.btnrrdescargar);
        llpregunta=findViewById(R.id.llrrpregunta);
        llprin=findViewById(R.id.llrrweb);
        llprin.setVisibility(View.GONE);
      //  llpregunta.setVisibility(View.GONE);
        pregunta.setmLabel(getString(R.string.esta_acuerdo));
        btndescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargar();
            }
        });
        if (Build.PRODUCT.contains ("sdk")||Build.MODEL.contains ("2006C3MG2")){//pruebas y el lenovo
            //nam
            BASE_URL = "http://192.168.1.84/comprasv1/api/public/";
            BASE_URL = Constantes.URLPRUEBAS1+ "api/public/";

        }else
        {
            BASE_URL = Constantes.URLSERV+"api/public/";

            //  BASE_URL = "http://192.168.1.84/comprasv1/pruebas/public/";
            //   BASE_URL = "https://muesmerc.mx/comprasv1/pruebas/public/";
        }
        //revisar conexion a internet
        if(!ComprasUtils.isOnlineNet(this)){
            Toast.makeText(this,"esta acción requiere conexión a internet",Toast.LENGTH_LONG);
            return;
        }
        //reviso si ya aceptó
        AcuseReciboRepositoryImpl acrepo=new AcuseReciboRepositoryImpl(this);
        AcuseRecibo acuse=acrepo.findsimple(Constantes.INDICEACTUAL);
        if(acuse!=null&&acuse.getAceptado()==1){
            //solo muestro boton descargar
            btndescargar.setVisibility(View.VISIBLE);
            return;
        }
        llprin.setVisibility(View.VISIBLE);
        //busco el estatus recibo
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        ListenerRec listener=new ListenerRec();
        ps.getEstatusRecibo(Constantes.INDICEACTUAL,Constantes.CIUDADTRABAJO,listener);


        String urlrecibo= BASE_URL+"/revisarRecibo?indice="+Constantes.INDICEACTUAL+"&cverec="+Constantes.CLAVEUSUARIO+"&cd="+Constantes.CIUDADTRABAJO;
        WebView webView = (WebView)findViewById(R.id.rrwebView);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(BASE_URL);

        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnenviar.setEnabled(false);
                enviarResp(comentarios.getText().toString(),pregunta.getRespuesta());
            }
        });


    }
    private void enviarResp(String comentarios, boolean respuesta){

        estatusAceptado=respuesta?1:0;
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        ps.acuseRecibo(Constantes.INDICEACTUAL,Constantes.CIUDADTRABAJO,comentarios,estatusAceptado, new ListenerRec());
    }
    private void descargar(){
        String MY_URL = Constantes.URLSERV+"descargarRecibo.php?cd="+Constantes.CIUDADTRABAJO+"&indice="+ Constantes.INDICEACTUAL+"&cverec="+Constantes.CLAVEUSUARIO;
        Uri uri = Uri.parse(MY_URL); // Path where you want to download file.
        String nombrearch="recibo_"+Constantes.INDICEACTUAL.replace(".","_");

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("DESCARGA RECIBO"); // Title for notification.
        // request.setVisibleInDownloadsUi(true);
        // request.setTitle("DESCARGA ETIQUETAS");
        //  Log.d(TAG,"hola"+MY_URL);

        request.setDestinationInExternalFilesDir(this ,Environment.DIRECTORY_PICTURES, nombrearch+".pdf");  // Storage directory path
        long archact=((DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading

    }

    public class ListenerRec {
        //para terminar cuando envia la respuesta
        //muestro boton para descargar y guardo en la app
        public void guardarEstatus(PostResponse response){
            if(response!=null&&response.getData().equals("si")) {
                estatusRecibo = 1;
                llprin.setVisibility(View.VISIBLE);

            }else
            {
                //todo anuncio de no está listo
            }
        }

        public void guardarRes(PostResponse respuesta){
            if(respuesta!=null) {
                AcuseReciboRepositoryImpl acrepo=new AcuseReciboRepositoryImpl(RevReciboActivity.this);
                // guardo acuse o actualizo
                AcuseRecibo acuse=acrepo.findsimple(Constantes.INDICEACTUAL);
                if(acuse!=null){
                    acuse.setAceptado(estatusAceptado);
                }else {
                    AcuseRecibo nvoacuse = new AcuseRecibo();
                    nvoacuse.setIndice(Constantes.INDICEACTUAL);
                    nvoacuse.setAceptado(estatusAceptado);
                    acrepo.insert(nvoacuse);
                }

                if (estatusAceptado == 1) {

                    btndescargar.setVisibility(View.VISIBLE);
                    //todo quito boton aceptar
                    llpregunta.setVisibility(View.GONE);
                } else {
                    Toast.makeText(RevReciboActivity.this, "Su comentario ha sido enviado", Toast.LENGTH_LONG);
                    btndescargar.setVisibility(View.GONE);
                    llpregunta.setVisibility(View.GONE);
                }
            }
            //todo quitar despues de la prueba
           if(estatusAceptado==1){

               btndescargar.setVisibility(View.VISIBLE);
               //todo quito boton aceptar
               llpregunta.setVisibility(View.GONE);
           }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_continuar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.csalir:
                finish();
            break;

        }

      return true;
    }
}