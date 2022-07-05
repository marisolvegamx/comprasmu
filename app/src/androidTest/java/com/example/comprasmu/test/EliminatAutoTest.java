package com.example.comprasmu.test;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.dao.ListaCompraDetalleDao;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.utils.EliminadorIndice;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class EliminatAutoTest {
    private ComprasDataBase db;
    private ListaCompraDetalleDao userDao;
    Application context;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    private InformeCompraDao infoDao;

    @Before
    public void createDb() {
        context = ApplicationProvider.getApplicationContext();
       // db = Room.databaseBuilder(context, ComprasDataBase.class,"compras_data").build();


        db = Room.inMemoryDatabaseBuilder(context, ComprasDataBase.class).build();

    }

    @After
    public void closeDb() throws IOException {
        db.close();
        //userDao.findAll().removeObservers();
    }


    @Test
    public void eliminarAuto() throws Exception {
        String indice="5.2022";
        userDao=db.getListaCompraDetalleDao();
        infoDao=db.getInformeCompraDao();
        EliminadorIndice ei=new EliminadorIndice(context,indice);
    ei.eliminarVisitas();
        LiveData<List<ListaCompraDetalle>> todos=userDao.getListasDetalleOrdByLista(1);
        todos.observeForever( new Observer<List<ListaCompraDetalle>>() {

            @Override
            public void onChanged(@Nullable List<ListaCompraDetalle> todos) {
                Log.d("probando", "****" + todos.size());
                Assert.assertNotNull(todos);
            }
        });
        infoDao.getInformeWithDetalleById(1).observeForever(new Observer<InformeWithDetalle>() {
            @Override
            public void onChanged(@Nullable InformeWithDetalle todos) {
                // Assert.assertNotNull(todos);
                if(todos!=null&&todos.informeDetalle!=null) {
                    Log.d("hola>>", todos.informe.getEstatus() + "");
                    Assert.assertTrue(todos.informeDetalle.get(0).getEstatus() == 0);
                }
                else
                    Log.d("hola", "es nulo");
                    //  Assert.assertTrue(todos.informe.getEstatus()==0);

            }
        });
        }
}
