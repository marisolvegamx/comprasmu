package com.example.comprasmu.test;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.CorreccionDao;
import com.example.comprasmu.data.modelos.SolicitudWithCor;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ConsultasCorreTest {

    private CorreccionDao idetDao;
    private CorreccionRepoImpl repository;
    private ComprasDataBase db;
    String tag="ConsultasCorreTest";
    Context context;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        context = ApplicationProvider.getApplicationContext();
        repository=new CorreccionRepoImpl(context);
    }

    @After
    public void closeDb() throws IOException {
       // db.close();
    }

    @Test
    public void getCorreccionTest(){

        repository.getAllxEtaPlan(63,"6.2022",1).observeForever(new Observer<List<SolicitudWithCor>>() {
            @Override
            public void onChanged(@Nullable List<SolicitudWithCor> todos) {
                 // Assert.assertNotNull(todos);
               if(todos!=null&&todos.size()>0)
                   Log.d("hola>>",todos.size()+"");
                else
                    Log.d("hola","es nulo");
              //  Assert.assertTrue(todos.informe.getEstatus()==0);
                Assert.assertTrue(todos.get(0).solicitud.getEtapa()==1);
            }
        });
    }
}
