package com.example.comprasmu.ui.tiendas;

import android.app.Application;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class BuscarTiendaViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Tienda>> listaTiendas;
     private  LiveData<Integer> size;
    private MutableLiveData<HashMap<Integer,String>> listaCiudades;
    private MutableLiveData<HashMap<Integer,String>> tiposTienda;

    Application context;
    PeticionMapaCd ps;
    private  LiveData<Boolean> empty;
    private final String TAG="BuscarTiendaViewModel";



    public BuscarTiendaViewModel(Application context) {
        super(context);
        this.context=context;
        ps =new PeticionMapaCd("4");
        listaTiendas=new MutableLiveData<>();

    }

   

    public MutableLiveData<List<Tienda>> getTiendas(String ciudad, String nombreTienda, String tipoTienda, LifecycleOwner lo) {
        if((ciudad!=null&&!ciudad.equals(""))||(nombreTienda!=null&&!nombreTienda.equals("")))
                //||(tipoTienda!=null&&!tipoTienda.getValue().equals("")))
        {
        //    ps.getTiendas(ciudad,tipoTienda,nombreTienda);
           return ps.getListatiendas();



        }
        else{
            Toast.makeText(context, "Debe seleccionar un filtro de búsqueda", Toast.LENGTH_LONG).show();

        }
        return null;
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