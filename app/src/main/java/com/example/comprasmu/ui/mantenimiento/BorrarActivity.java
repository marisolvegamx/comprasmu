package com.example.comprasmu.ui.mantenimiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.comprasmu.R;
import com.example.comprasmu.ui.home.HomeActivity;
import com.example.comprasmu.ui.home.PruebasActivity;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.EliminadorIndice;

public class BorrarActivity extends AppCompatActivity {
    public static String INDICEACT="comprasmu.borindiceact";
    TextView aviso;
    private BorrarDatosViewModel mViewModel;
    String indiceact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar);
        mViewModel = new ViewModelProvider(this).get(BorrarDatosViewModel.class);

        Button btnborrar=findViewById(R.id.btnboaceptar);
        Button btncancelar=findViewById(R.id.btnbocancelar);
        aviso=findViewById(R.id.txtbomensaje);
        Bundle extras = getIntent().getExtras(); // Aquí es null


        if(extras!=null) {
            indiceact = extras.getString(INDICEACT);
        }
        aviso.setText("Para iniciar el nuevo indice necesita borrar la información de "+ ComprasUtils.indiceLetra(indiceact)+". Presione borrar para eliminar o cancelar para continuar con el indice actual ");
        btnborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //borrarxindice();
                borrarautomatico();
                irAPruebas();
            }
        });
        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAHome();
            }
        });

    }

    private void irAHome() {
        Intent intento=new Intent(this, HomeActivity.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intento);
        finish();
    }
    private void irAPruebas() {
        //inicializo prefs
        inicializarEtapaPref();
        Intent intento=new Intent(this, PruebasActivity.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intento);
        finish();
    }
    public void borrarautomatico(){

        String indice_anterior=indiceact;
        EliminadorIndice ei=new EliminadorIndice(this,indice_anterior);
        ei.eliminarVisitas();
        aviso.setVisibility(View.VISIBLE);

        // mViewModel.borrarInformes(indice_anterior);
        mViewModel.borrarListasCompra(indice_anterior);
        // Log.d("Comprasmu.BorrarDatosFragment","Se eliminaron las listas");
        //todo borrar informes etapa
        mViewModel.borrarInformesetapa(indice_anterior);
        ei.eliminarCorrecciones();
        ei.eliminarSolicitudes();
        ei.borrarImagenes();
    }
    public void inicializarEtapaPref(){
        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putInt("etapaact",0 );
        editor.putString("indiceact", "");
        editor.putInt("etapafin", 0);
        editor.commit();

    }
}