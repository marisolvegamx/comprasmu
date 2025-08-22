package com.example.comprasmu.test;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.services.DescargaCambiosImagenes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Observable;

@RunWith(AndroidJUnit4.class)
public class DescargaCambiosImagenesTest {
    Context context;
    private ComprasDataBase db;
    TablaVersionesRepImpl tvrepo;
    @Before
    public void createDb() {
        context = ApplicationProvider.getApplicationContext();
        db = Room.databaseBuilder(context, ComprasDataBase.class,"compras_data").allowMainThreadQueries().build();
        tvrepo=new TablaVersionesRepImpl(context);
        tvrepo.deleteAll();

    }
    @Test
    public void ejecutar() {
        String usuario="9";
        String indice="3.2025";

        ImagenDetalleDao imagenDao= db.getImagenDetalleDao();
        ImagenDetRepositoryImpl imagenRepo=ImagenDetRepositoryImpl.getInstance(imagenDao);

        MutableLiveData<Boolean> observable= new MutableLiveData<Boolean>();
        observable.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                Log.d("PRUEBAS","termine");
                ImagenDetalle resp= imagenRepo.findsimple(6633);
               if(resp!=null)
                   Log.d("PRUEBAS",resp.getRuta());
            }
        });
        String dirLog=context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath();
        DescargaCambiosImagenes descarga=new DescargaCambiosImagenes(dirLog,tvrepo,imagenRepo, usuario, indice, observable);
        descarga.ejecutar();
        Assert.assertTrue(true);
    }
}