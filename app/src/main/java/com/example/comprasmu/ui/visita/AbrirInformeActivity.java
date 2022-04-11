package com.example.comprasmu.ui.visita;

import android.os.Bundle;

import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.comprasmu.R;
import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;

public class AbrirInformeActivity extends AppCompatActivity {
    private TextView mTextView;
    private Toolbar myChildToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_informe);
        //
        myChildToolbar =
                (Toolbar) findViewById(R.id.my_child_toolbarai);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
       // ab.setDisplayHomeAsUpEnabled(true);
// add
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        //para saber que fragment cargar
      //  Bundle datosRecuperados = getIntent().getExtras();
   /*     AbririnformeFragment detailFragment = new AbririnformeFragment();
        //  SelClienteFragment detailFragment = new SelClienteFragment();

      //  detailFragment.setArguments(datosRecuperados);
        ft.add(R.id.ai_fragment, detailFragment);
        ft.commit();*/

    }
}