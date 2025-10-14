package com.example.comprasmu.test;

import static org.junit.Assert.*;

import android.content.Context;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.HistoricoMuestras;
import com.example.comprasmu.data.modelos.MenuVideo;
import com.example.comprasmu.data.repositories.HistoricoMuestrasRepositoryImpl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class PeticionesServidorTest {

    Context context;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    @Test
    public void getVideos() {
        PeticionesServidor peticionesServidor = new PeticionesServidor("12");
        //reviso si tengo algo en la tabla
        LiveData<List<MenuVideo>> registros = peticionesServidor.getVideos();
        registros.observeForever(new Observer<List<MenuVideo>>() {
            @Override
            public void onChanged(List<MenuVideo> menuVideos) {
                Log.d("PRUEBAS", "termine" + menuVideos.size());
            }
        });
        assertTrue(true);
    }
}