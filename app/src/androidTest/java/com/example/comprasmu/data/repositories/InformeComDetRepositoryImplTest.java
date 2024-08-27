package com.example.comprasmu.data.repositories;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.InformeCompraDetDao;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class InformeComDetRepositoryImplTest {
    InformeCompraDetDao dao;
    private ComprasDataBase db;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    @Before
    public void createDb() {

        Context context = ApplicationProvider.getApplicationContext();
        // db = Room.databaseBuilder(context, ComprasDataBase.class,"compras_data").build();


        db = Room.inMemoryDatabaseBuilder(context, ComprasDataBase.class).build();

    }
    @Before
    public void setUp() throws Exception {
      //  ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=db.getInformeCompraDetDao();
    }

    @Test
    public void gettotCancelados() {
        List<InformeCompraDetalle> informe=dao.getByEstatussimpl("8.2024",2);
        assertNotNull(informe);
        assertTrue("tam"+informe.size(),informe.size()>1);

    }
}