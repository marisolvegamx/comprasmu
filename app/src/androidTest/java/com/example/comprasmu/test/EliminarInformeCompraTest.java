package com.example.comprasmu.test;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.DescargaListaCompraAuto;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.dao.ListaCompraDetalleDao;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.remote.CambiosInformesReponse;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.utils.Constantes;
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
public class EliminarInformeCompraTest {
    private ComprasDataBase db;

    Application context;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();


    @Before
    public void createDb() {
        context = ApplicationProvider.getApplicationContext();
       // db = Room.databaseBuilder(context, ComprasDataBase.class,"compras_data").build();


        db = Room.databaseBuilder(context, ComprasDataBase.class,"compras_data").allowMainThreadQueries().build();


    }

    @After
    public void closeDb() throws IOException {
        db.close();
        //userDao.findAll().removeObservers();
    }


    @Test
    public void eliminarAuto() throws Exception {
        String indice="5.2022";

        String directorioImagenes=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/";
        InformeCompraRepositoryImpl infrepo = new InformeCompraRepositoryImpl(context);
        InformeComDetRepositoryImpl infdrepo = new InformeComDetRepositoryImpl(context);
        ImagenDetalleDao imagenDetalleDao= ComprasDataBase.getInstance(context).getImagenDetalleDao();
        ImagenDetRepositoryImpl imagenDetRepo= ImagenDetRepositoryImpl.getInstance(imagenDetalleDao);
        VisitaRepositoryImpl visitaRepo=new VisitaRepositoryImpl(context);
        ProductoExhibidoRepositoryImpl prodExibidoRepo=new ProductoExhibidoRepositoryImpl(context);
        InformeCompra informeCompra=new InformeCompra();
        ListaCompraDetRepositoryImpl lcdrepo = new ListaCompraDetRepositoryImpl(context);
        informeCompra.setId(1);
        informeCompra.setVisitasId(1);
        informeCompra.setTicket_compra(17);
        informeCompra.setCondiciones_traslado(18);
        infrepo.insertInformeCompra(informeCompra);
        InformeWithDetalle informeCompra1=infrepo.getInformeWithDetalleByIdsimple(1);
        if(informeCompra1!=null)
        Log.d("PRUEBAS","****"+informeCompra1.informe.getId());
       // DescargaListaCompraAuto.eliminarInforme(visitaRepo, prodExibidoRepo,infrepo,informeCompra,imagenDetRepo,infdrepo,lcdrepo,directorioImagenes);

    }

    @Test
    public void queondaconelJson() throws Exception {
        String indice="5.2022";

        PeticionesServidor peticionesServidor=new PeticionesServidor("1");
        LiveData<CambiosInformesReponse> lcambiosInformesResponse=peticionesServidor.getCambiosInformes(indice,"2026-01-01");
        Observer observadorCambios= new Observer<CambiosInformesReponse>() {
            @Override
            public void onChanged(CambiosInformesReponse cambiosInformesReponse) {
                if(cambiosInformesReponse!=null&& cambiosInformesReponse.getIC()!=null){

                }
            }
        };
        lcambiosInformesResponse.observeForever(observadorCambios);
    }
}
