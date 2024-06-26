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
        textusuario= findViewById(R.id.txthmusuario);
        prep= findViewById(R.id.btnhmprep);
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
        //textusuario.on
        Log.d("HomeActivity","entreeeee");
    }

    public void ira(int etapa){
        Constantes.ETAPAACTUAL=0;
        Intent intento=new Intent(this, NavigationDrawerActivity.class);
        intento.putExtra(NavigationDrawerActivity.ETAPA, etapa);
        startActivity(intento);
        finish();
    }



}