package com.example.comprasmu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.comprasmu.ui.listadetalle.PostViewModel;

import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG ="Ejemplo" ;
    Button cancelButton,sortButton;
    public TextView progressLabel;
    private TextView mResponseTv;
    private PostViewModel  vmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cancelButton= (Button) findViewById(R.id.btncancelar);
        sortButton= (Button) findViewById(R.id.btnejecutar);
        progressLabel=  findViewById(R.id.progresslabel);
        sortButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, com.example.comprasmu.PruebaService.class));
                cancelButton.setVisibility(View.VISIBLE);
                sortButton.setEnabled(false);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, PruebaService.class));
                cancelButton.setVisibility(View.INVISIBLE);

                sortButton.setEnabled(true);
            }
        });

    }
    public void sendPost(String title, String body) {
        vmp =  new ViewModelProvider(this).get(PostViewModel.class);
        vmp.sendPost("hola", "esta es mi primera prueba");
        

    }

    public void verTabs(View v){
       // Intent intente=new Intent(this, SelPlantaActivity.class);
        Intent intente=new Intent(this, NavigationDrawerActivity.class);

        startActivity(intente);
    }

    public void showResponse(String response) {
        if(mResponseTv.getVisibility() == View.GONE) {
            mResponseTv.setVisibility(View.VISIBLE);
        }
        mResponseTv.setText(response);
    }



}