package com.example.comprasmu.test;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.data.modelos.TiendaJson;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.data.repositories.TiendaRepositoryImpl;
import com.example.comprasmu.services.DescargaCambiosImagenes;
import com.example.comprasmu.ui.tiendas.PeticionMapaCd;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Observable;

@RunWith(AndroidJUnit4.class)
public class DescargaTiendasTest {
    Context context;
    private ComprasDataBase db;
    TiendaRepositoryImpl tvrepo;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    @Before
    public void createDb() {
        context = ApplicationProvider.getApplicationContext();
        db = Room.databaseBuilder(context, ComprasDataBase.class,"compras_data").allowMainThreadQueries().build();
        tvrepo=TiendaRepositoryImpl.getInstance(db.getInstance(context).getTiendaDao());

        tvrepo.deleteAll();

    }
    @After
    public void closeDb() throws IOException {
        db.close();
        //userDao.findAll().removeObservers();
    }
    @Test
    public void getTiendas() {
        String usuario="9";
        String indice="3.2025";
        String ciudad="ACAPULCO";
        PeticionMapaCd pm=new PeticionMapaCd(usuario);
        pm.getTiendas("0",ciudad,"2025-04-18");
        pm.getListatiendas().observeForever( new Observer<List<TiendaJson>>() {
            @Override
            public void onChanged(List<TiendaJson> tiendas) {
                //guardo en la tabla local
                if(tiendas!=null&& tiendas.size()>0)
                 Log.d(">>>","Hubo algo"+ tiendas.size());
            }
        });

        Assert.assertTrue(true);
    }


}