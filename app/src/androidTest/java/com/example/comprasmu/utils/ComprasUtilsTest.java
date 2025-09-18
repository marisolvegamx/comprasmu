package com.example.comprasmu.utils;

import static org.junit.Assert.*;

import android.content.Context;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.modelos.HistoricoMuestras;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaDetalleBu;
import com.example.comprasmu.data.repositories.HistoricoMuestrasRepositoryImpl;
import com.example.comprasmu.data.repositories.TiendaRepositoryImpl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComprasUtilsTest {
    Context context;
    private ComprasDataBase db;
    HistoricoMuestrasRepositoryImpl historicoMuestrasRepository;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    @Before
    public void createDb() {
        context = ApplicationProvider.getApplicationContext();
        db = Room.databaseBuilder(context, ComprasDataBase.class,"compras_data").allowMainThreadQueries().build();


    }
    @After
    public void closeDb() throws IOException {
        db.close();
        //userDao.findAll().removeObservers();
    }

    @Test
    public void restarIndice() {
        try {
           System.out.println("1--"+ComprasUtils.restarIndice("2.2025",3));

            System.out.println("2--"+ComprasUtils.restarIndice("2.2025",2));
            System.out.println("3--"+ComprasUtils.restarIndice("5.2025",1));
            Assert.assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void consultaFiltros() {
        try {
             historicoMuestrasRepository=HistoricoMuestrasRepositoryImpl.getInstance(db.getHistoricoMuestrasDao());

            String indice1="4.2025",indice2="5.2025";

            List<HistoricoMuestras> listaHistorico=historicoMuestrasRepository.getDetalleByFiltros(20,3,"PEPSI","PET",13,indice1, indice2);
            System.out.println("wwwww");
            System.out.println(">>"+listaHistorico.size());
            Assert.assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}