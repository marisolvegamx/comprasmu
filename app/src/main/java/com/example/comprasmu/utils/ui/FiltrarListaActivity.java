package com.example.comprasmu.utils.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.os.Bundle;

import com.example.comprasmu.R;
import com.example.comprasmu.ui.informe.BuscarInformeFragment;

public class FiltrarListaActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_INFORME =1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_lista);
        Toolbar myChildToolbar =
                findViewById(R.id.my_child_toolbar);
        setSupportActionBar(myChildToolbar);
       // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        // get fragment manager

        // add
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new BuscarInformeFragment();
        ft.add(R.id.filtercontainer, fragment);
        ft.commit();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

}