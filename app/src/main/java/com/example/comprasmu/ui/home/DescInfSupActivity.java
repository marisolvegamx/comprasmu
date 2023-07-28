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
import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;

/***actualizo los informes de compras que se modificaron desde el sistema web*/
public class DescInfSupActivity extends AppCompatActivity implements DescRespInformes.ProgresoRespListener {

    InformeComDetRepositoryImpl infdrepo;

    InformeCompraRepositoryImpl infrepo;

    SimpleDateFormat sdfdias;
    TablaVersionesRepImpl tvRepo;
    String TAG="DescInfSupActivity";
    ProgressDialog progreso;

    private ComprasLog complog;

   TextView destxtsincon;
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
        DescRespInformes desc=new DescRespInformes(this,this,tvRepo);

        desc.getInformes();

    }

    private void notificarSinConexion() {
        destxtsincon.setVisibility(View.VISIBLE);
        todoBien();
    }


    @Override
    public void todoBien() {
        //pasaría a otra actividad
        progreso.dismiss();
        Constantes.ACTUALIZADO=true;
        //Intent intento=new Intent(this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Intent intento=new Intent(this, NavigationDrawerActivity.class);
        intento.putExtra(NavigationDrawerActivity.ETAPA, 3);
        startActivity(intento);
        finish();
    }


}