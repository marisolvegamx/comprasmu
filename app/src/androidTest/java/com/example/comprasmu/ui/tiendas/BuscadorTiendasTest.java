package com.example.comprasmu.ui.tiendas;

import static org.junit.Assert.*;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.data.remote.ServiceGenerator;
import com.example.comprasmu.data.remote.TiendasResponse;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;
import com.example.comprasmu.utils.ComprasUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(AndroidJUnit4.class)
public class BuscadorTiendasTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }
    @Test
    public void hayTiendas() {
        BuscadorTiendas bc=new BuscadorTiendas();
        List<Tienda> tiendas=new ArrayList<>();
        int usuario=42;
        PeticionesServidor ps=new PeticionesServidor("42");
        MutableLiveData<List<Tienda>> listatiendas=new MutableLiveData<>();
        String ffin= "";
        String indicefin="8.2024";
        ffin= ComprasUtils.indiceaFecha2(indicefin);
        String fini="";
        //calculo el fin
        String[] aux1=indicefin.replace(".","-").split("-");
        String anio=aux1[1];
        int anioant=Integer.parseInt(anio)-1;
        String indiceini=aux1[0]+"."+anioant;
        fini=ComprasUtils.indiceaFecha2(indiceini);
        final Call<TiendasResponse> batch = ServiceGenerator.getApiService().getTiendas("0", "ESTADO DE MEXICO", 70, 5, fini,ffin,"0","0",usuario+"");
        //  double mix=19.5137788;
        //  double miy=-99.2086756;
        double mix=19.532728;
        double miy=-99.198736;
        batch.enqueue(new Callback<TiendasResponse>() {
            @Override
            public void onResponse(@Nullable Call<TiendasResponse> call, @Nullable Response<TiendasResponse> response) {
//               Log.d(TAG,"llego algo"+response.body().toString());
                if (response.isSuccessful() && response.body() != null) {
                    TiendasResponse respuestaTiendas = response.body();
                    if(respuestaTiendas!=null) {
                        //  Log.d(TAG,"llego algo"+respuestaTiendas.getTiendas().size());

                        listatiendas.setValue(respuestaTiendas.getTiendas());
                        Tienda tienda=bc.hayTiendas2(respuestaTiendas.getTiendas(),mix,miy);
                        Log.d("res**",tienda.getUne_descripcion());
                        Log.d("res**",tienda.getUne_coordenadasxy());
                        assertNull(tienda);

                    }
                    //  return lista;


                }
            }
            @Override
            public void onFailure(@Nullable Call<TiendasResponse> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e("osi", t.getMessage());

                }
            }
        });

       /* listatiendas.observeForever( new Observer<List<Tienda>>() {
            @Override
            public void onChanged(List<Tienda> tiendas) {
                Tienda tienda=bc.hayTiendas2(tiendas,mix,miy);
                Log.d("res**",tienda.getUne_descripcion());
                Log.d("res**",tienda.getUne_coordenadasxy());
                assertNull(tienda);
            }
        });*/

    }
}