package com.example.comprasmu.ui.home;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.ui.login.LoginActivity;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;
/****ahora si es la pagina con botones para seleccionar la etapa******/
public class HomeActivity extends AppCompatActivity {


    TextView textusuario;
    Button prep;
    Button comp,etiq,emp,envio,gasto;
    private boolean isEdit;
    private int tiporec;
    ComprasLog milog;
    int mivar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        milog=ComprasLog.getSingleton();
        milog.crearLog(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath());
    mivar=5;
        final TextView textView = findViewById(R.id.text_home);
        Constantes.ETAPAACTUAL=0;
        getTipoRec();
        Log.d("HomeActivity","entreeeee"+Constantes.ETAPAMENU);
        textusuario= findViewById(R.id.txthmusuario);
        prep= findViewById(R.id.btnhmprep);
        TextView mensaje= findViewById(R.id.txtherror);

        etiq= findViewById(R.id.btnetiq);
        emp= findViewById(R.id.btnemp);
        envio= findViewById(R.id.btnenvio);
        gasto= findViewById(R.id.btngasto);
        prep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ira(1);
            }
        });
         comp = findViewById(R.id.btnhmcom);
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ira(2);
            }
        });
        etiq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iraEtiq();
            }
        });
        emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ira(4);
            }
        });
        envio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ira(5);
            }
        });
        gasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ira(6);
            }
        });

            //veo que tipo de recolector es
            if(tiporec==1) { //es forenaeo
                if(Constantes.ETAPAMENU==0){
                //no puede empezar
                mensaje.setText("No puede empezar");
                mensaje.setVisibility(View.VISIBLE);
                emp.setVisibility(View.GONE);
                comp.setVisibility(View.GONE);
                prep.setVisibility(View.GONE);
                etiq.setVisibility(View.GONE);
                envio.setVisibility(View.GONE);
                gasto.setVisibility(View.GONE);
                Button btnsalir = findViewById(R.id.btnhsalir);
                btnsalir.setVisibility(View.VISIBLE);
                btnsalir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        salir();
                    }
                });
            }else{
                switch (Constantes.ETAPAMENU){
                    case 1:  prep.setVisibility(View.VISIBLE);
                        break;
                    case 2:  comp.setVisibility(View.VISIBLE);
                        break;
                    case 3:  etiq.setVisibility(View.VISIBLE);
                        break;
                    case 4:  emp.setVisibility(View.VISIBLE);
                        break;
                    case 5:  envio.setVisibility(View.VISIBLE);
                        break;
                    case 6:  gasto.setVisibility(View.VISIBLE);
                        break;

                }
            }

        }else // es local ve todos los botones
            {
                prep.setVisibility(View.VISIBLE);
                comp.setVisibility(View.VISIBLE);
                etiq.setVisibility(View.VISIBLE);
                emp.setVisibility(View.VISIBLE);
                envio.setVisibility(View.VISIBLE);
              //  gasto.setVisibility(View.VISIBLE);
            }

    }

    private void salir() {
        Constantes.LOGGEADO=false;
        Intent intento=new Intent(this, LoginActivity.class);
        intento.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intento);
        finish();
    }

    public void ira(int etapa){
        Constantes.ETAPAACTUAL=etapa;
        Intent intento=new Intent(this, NavigationDrawerActivity.class);
        intento.putExtra(NavigationDrawerActivity.ETAPA, etapa);
        startActivity(intento);
        finish();
    }
    public void iraEtiq(){
        Log.d("HomeActivity","mivar "+mivar);
        Constantes.ETAPAACTUAL=3;
        Intent intento=new Intent(this, DescInfSupActivity.class);
        startActivity(intento);
        finish();

       /* Constantes.ETAPAACTUAL=3;
        Intent intento=new Intent(this, NavigationDrawerActivity.class);
        intento.putExtra(NavigationDrawerActivity.ETAPA, 3);
        startActivity(intento);
        finish();*/
    }


    public void getTipoRec(){
        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);

        tiporec= prefe.getInt("tiporec", 0);
    }
}