package com.example.comprasmu.test;

import android.app.Application;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class InformeComDetRepositoryTest {
    private ComprasDataBase db;
    private InformeComDetRepositoryImpl informeComDetRepository;
    Application context;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        context = ApplicationProvider.getApplicationContext();
       // db = Room.databaseBuilder(context, ComprasDataBase.class,"compras_data").build();


        db = Room.inMemoryDatabaseBuilder(context, ComprasDataBase.class).build();
        informeComDetRepository=new InformeComDetRepositoryImpl(context);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
        //userDao.findAll().removeObservers();
    }


    @Test
    public void getByProductoAnalisisxInf() throws Exception {
        String indice = "9.2025";

        List<InformeCompraDetalle> todos = informeComDetRepository.getByProductoAnalisisxInf(indice, 5, 6, 4, 1, "3 L");
        Log.d("******",">>"+(todos!=null?todos.size():0));
        Assert.assertNotNull(todos);

        // Assert.assertTrue(todos.informe.getEstatus()==0);
    }

}
