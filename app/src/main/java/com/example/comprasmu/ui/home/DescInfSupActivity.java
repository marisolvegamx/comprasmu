package com.example.comprasmu.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.comprasmu.DescRespInformes;
import com.example.comprasmu.DescRespInformesEta;
import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;

/***actualizo los informes de compras que se modificaron desde el sistema web
 * se utiliza antes de entrar al modulo etiquetado
 *
 * */
public class DescInfSupActivity extends AppCompatActivity implements DescRespInformes.ProgresoRespListener,DescRespInformesEta.ProgresoRespIEListener {


    TablaVersionesRepImpl tvRepo;
    String TAG="DescInfSupActivity";
    ProgressDialog progreso;

    private ComprasLog complog;
    int procesos;
   TextView destxtsincon;
   int contprocesos=0;//para saber que ya terminó las 2 descargas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas);
        //  if(NavigationDrawerActivity.isOnlineNet()) {
        Log.d(TAG,"iniciando descarga");
        complog= ComprasLog.getSingleton();
        complog.crearLog(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath());

        progreso = new ProgressDialog(this);

        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progreso.setMessage("Actualizando, por favor permanezca en la aplicación...");

        progreso.setCancelable(false);
        destxtsincon=(TextView) findViewById(R.id.destxtsincon);
      tvRepo=new TablaVersionesRepImpl(this);

        progreso.show();

        if(!ComprasUtils.isOnlineNet()) {
            notificarSinConexion();
            return;
        }
        /**descargo inf compras**/
        DescRespInformes desc=new DescRespInformes(this,this,tvRepo);

        desc.getInformes();
        //descargo actualizaciones de etiquetado //solo se modifica qr y estatus
        DescRespInformesEta desetiq=new DescRespInformesEta(this,this,tvRepo);
        desetiq.getCambiosSupEtiq();
        //lo hago al inicio para consultarlo en cualquier modulo
       // desetiq.getCambiosEtiq();

    }

    private void notificarSinConexion() {
        destxtsincon.setVisibility(View.VISIBLE);
        todoBien();
    }


    @Override
            public void finalizarrespie(){
      //  if(contprocesos==1) {
            //pasaría a otra actividad
        procesos++;
            //Intent intento=new Intent(this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
           if(procesos==1) {
               progreso.dismiss();
               Constantes.ACTUALIZADO = true;
               Intent intento = new Intent(this, NavigationDrawerActivity.class);
               intento.putExtra(NavigationDrawerActivity.ETAPA, 3);
               startActivity(intento);
               finish();
           }

      //  }
      ////  else{
        //    contprocesos++;
       // }
    }


    @Override
    public void todoBien() {
        this.finalizarrespie();
    }
}