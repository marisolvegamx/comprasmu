package com.example.comprasmu.ui.tiendas;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.data.remote.APIService;
import com.example.comprasmu.data.remote.ServiceGenerator;
import com.example.comprasmu.data.remote.TiendasResponse;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscarTiendaViewModel extends AndroidViewModel {




    private MutableLiveData<List<Tienda>> listaTiendas;
     private  LiveData<Integer> size;
    private MutableLiveData<HashMap<Integer,String>> listaCiudades;
    private MutableLiveData<HashMap<Integer,String>> tiposTienda;
    public MutableLiveData<List<DescripcionGenerica>> listadescripcion;
    Application context;
    PeticionesServidor ps;
    private  LiveData<Boolean> empty;




    public BuscarTiendaViewModel(Application context) {
        super(context);
        this.context=context;
        ps =new PeticionesServidor(Constantes.CLAVEUSUARIO);
        listaTiendas=new MutableLiveData<>();
        listadescripcion=new MutableLiveData<>();

    }

    private void getCatalogosBT() {
   //  ps.getCatalogos(new CatalogoDetalleRepositoryImpl(context));

    }


    public void getTiendas(String ciudad, String nombreTienda, String tipoTienda, LifecycleOwner lo) {
        if((ciudad!=null&&!ciudad.equals(""))||(nombreTienda!=null&&!nombreTienda.equals("")))
                //||(tipoTienda!=null&&!tipoTienda.getValue().equals("")))
        {
            ps.getTiendas(ciudad,tipoTienda,nombreTienda);
           ps.getLista().observeForever( new Observer<List<Tienda>>() {
                @Override
                public void onChanged(List<Tienda> tiendas) {
                    Log.d("BuscarTiendaViewModel","llegué aquí"+tiendas.size());
                    tiendaToDescripcion(tiendas);
                }
            });



        }
        else{
            Toast.makeText(context, "Debe seleccionar un filtro de búsqueda", Toast.LENGTH_LONG).show();

        }
    }

    public void tiendaToDescripcion(List<Tienda> listaTiendas){

        List<DescripcionGenerica> lista=new ArrayList<DescripcionGenerica>();
        for(Tienda tienda:listaTiendas){
            Log.d("BuscarTiendaVM","llegué aquí"+ tienda.getUne_descripcion());


            DescripcionGenerica desc=new DescripcionGenerica();
            desc.id=tienda.getUne_id();
            desc.nombre=tienda.getUne_descripcion();
            desc.descripcion=tienda.getUne_direccion();

            lista.add(desc);
        }
        listadescripcion.setValue(lista);

    }
     public MutableLiveData<List<DescripcionGenerica>> getListadescripcion() {
        return listadescripcion;
    }

    public void setListadescripcion(MutableLiveData<List<DescripcionGenerica>> listadescripcion) {
        this.listadescripcion = listadescripcion;
    }

    public LiveData<Integer> getSize() {
        return size;
    }

    public void setSize(LiveData<Integer> size) {
        this.size = size;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }

    public void setEmpty(LiveData<Boolean> empty) {
        this.empty = empty;
    }
}