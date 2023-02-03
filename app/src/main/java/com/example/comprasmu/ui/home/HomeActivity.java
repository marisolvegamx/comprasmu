package com.example.comprasmu.ui.home;


import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.ui.login.LoginActivity;
import com.example.comprasmu.utils.Constantes;
/****ahora si es la pagina de inicio******/
public class HomeActivity extends AppCompatActivity {


    TextView textusuario;
    Button prep;
    Button comp,etiq,emp;
    private boolean isEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        final TextView textView = findViewById(R.id.text_home);
        Constantes.ETAPAACTUAL=0;
        Log.d("HomeActivity","entreeeee"+Constantes.ETAPAMENU);
        textusuario= findViewById(R.id.txthmusuario);
        prep= findViewById(R.id.btnhmprep);
        TextView mensaje= findViewById(R.id.txtherror);

        etiq= findViewById(R.id.btnetiq);
        emp= findViewById(R.id.btnemp);
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
                ira(3);
            }
        });
        emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ira(4);
            }
        });
        if(Constantes.ETAPAMENU==0){
            //no puede empezar
            mensaje.setText("No puede empezar");
            mensaje.setVisibility(View.VISIBLE);
            emp.setVisibility(View.GONE);
            comp.setVisibility(View.GONE);
            prep.setVisibility(View.GONE);
            etiq.setVisibility(View.GONE);
            Button btnsalir=findViewById(R.id.btnhsalir);
            btnsalir.setVisibility(View.VISIBLE);
            btnsalir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    salir();
                }
            });

        }else
            switch (Constantes.ETAPAMENU){
                case 1:  prep.setVisibility(View.VISIBLE);
                    break;
                case 2:  comp.setVisibility(View.VISIBLE);
                    break;
                case 3:  etiq.setVisibility(View.VISIBLE);
                    break;
                case 4:  emp.setVisibility(View.VISIBLE);
                    break;
            }
        //textusuario.on

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


}