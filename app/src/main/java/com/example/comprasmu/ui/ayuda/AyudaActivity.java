package com.example.comprasmu.ui.ayuda;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import com.example.comprasmu.R;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.MenuVideo;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class AyudaActivity extends AppCompatActivity implements  VideosAdapter.AdapterCallback{
    Toolbar myChildToolbar;
    RecyclerView rvVideos;
    VideosAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        rvVideos=findViewById(R.id.rvvideos);
        myChildToolbar =findViewById(R.id.aytoolbarinf);
        setSupportActionBar(myChildToolbar);
        setupListAdapter();
        llenarDetalle();
    }
    private void setupListAdapter() {
        listAdapter = new VideosAdapter(this);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));
        rvVideos.setHasFixedSize(true);
        rvVideos.setAdapter(listAdapter);

    }
    public void llenarDetalle() {

        PeticionesServidor peticionesServidor=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        peticionesServidor.getVideos().observe(this, new Observer<List<MenuVideo>>() {
            @Override
            public void onChanged(List<MenuVideo> lista) {

                listAdapter.setList(lista,AyudaActivity.this);
                listAdapter.notifyDataSetChanged();
            }

        });


    }

    @Override
    public void onClickVer(String liga) {
        //todo agregar fragment de video
        Intent intento=new Intent(this,ExoPlayerActivity.class);
        intento.putExtra(ExoPlayerActivity.ARG_LIGAVIDEO,liga);
        startActivity(intento);
    }
}