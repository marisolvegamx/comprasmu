package com.example.comprasmu;


import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.dao.ListaCompraDetalleDao;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.ui.informedetalle.ValidadorDatos;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
    public class ValidadorDatosTest {
        private ListaCompraDetalleDao userDao;
        private ComprasDataBase db;
    Context context;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
        @Before
        public void createDb() {
            context = ApplicationProvider.getApplicationContext();
            db = Room.databaseBuilder(context, ComprasDataBase.class,"compras_data").build();


          //  db = Room.inMemoryDatabaseBuilder(context, ComprasDataBase.class).build();
            userDao=db.getListaCompraDetalleDao();
        }

        @After
        public void closeDb() throws IOException {
            db.close();
            //userDao.findAll().removeObservers();
        }

        @Test
        public void validar() throws Exception {
            String texto="24-12-22";

            userDao.find(9,2).observeForever(new Observer<ListaCompraDetalle>() {
              @Override
              public void onChanged(@Nullable ListaCompraDetalle todos) {
                  ValidadorDatos valdat = new ValidadorDatos();
                  Log.d("algo1c ",todos.getCodigosNoPermitidos());
                  if (!valdat.validarCodigoprodPep(texto, todos.getCodigosNoPermitidos())) {
                      if (valdat.mensaje > 0)
                          Log.d("algo",context.getString(valdat.mensaje));
                  }

                          Assert.assertTrue(valdat.mensaje==0);
              }
          });
        }


}
