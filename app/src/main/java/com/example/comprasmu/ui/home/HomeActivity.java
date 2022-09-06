package com.example.comprasmu.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.utils.Constantes;
/****ahora si es la pagina de inicio******/
public class HomeActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    TextView textusuario;
    Button prep;
    Button comp;
    private boolean isEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        final TextView textView = findViewById(R.id.text_home);

        textusuario= findViewById(R.id.txthmusuario);
        prep= findViewById(R.id.btnhmprep);
        prep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ira(1);
            }
        });
         comp = (Button)findViewById(R.id.btnhmcom);
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ira(2);
            }
        });
        //textusuario.on

    }

    public void ira(int etapa){
        Intent intento=new Intent(this, NavigationDrawerActivity.class);
        intento.putExtra(NavigationDrawerActivity.ETAPA, etapa);
        startActivity(intento);
    }



}