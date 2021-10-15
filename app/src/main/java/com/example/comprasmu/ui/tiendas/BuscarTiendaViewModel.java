package com.example.comprasmu.ui.tiendas;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

    public MutableLiveData<String> nombreTienda;
    public MutableLiveData<String> ciudad;
    public MutableLiveData<String> tipoTienda;
    private LiveData<ArrayList<Tienda>> listaTiendas;
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

    }

    private void getCatalogosBT() {
     ps.getCatalogos(new CatalogoDetalleRepositoryImpl(context));

    }


    public void getTiendas() {
        if((ciudad.getValue()!=null&&!ciudad.getValue().equals(""))||(tipoTienda.getValue()!=null&&!tipoTienda.getValue().equals(""))||(nombreTienda.getValue()!=null&&!nombreTienda.getValue().equals("")))
        {
            listaTiendas=ps.getTiendas(ciudad.getValue(),tipoTienda.getValue(),nombreTienda.getValue());

            tiendaToDescrion();


        }
        else{
            Toast.makeText(context, "Debe seleccionar un filtro de búsqueda", Toast.LENGTH_LONG).show();

        }
    }

    public void tiendaToDescrion(){
        listadescripcion=new MutableLiveData<List<DescripcionGenerica>>();
        List<DescripcionGenerica> lista=new ArrayList<DescripcionGenerica>();
        for(Tienda tienda:listaTiendas.getValue()){
            DescripcionGenerica desc=new DescripcionGenerica();
            desc.id=tienda.getTiendaId();
            desc.nombre=tienda.getTiendaNombre();
            desc.descripcion=tienda.getDireccion();

            lista.add(desc);
        }
        listadescripcion.setValue(lista);

    }
    public MutableLiveData<String> getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(MutableLiveData<String> nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public MutableLiveData<String> getCiudad() {
        return ciudad;
    }

    public void setCiudad(MutableLiveData<String> ciudad) {
        this.ciudad = ciudad;
    }

    public MutableLiveData<String> getTipoTienda() {
        return tipoTienda;
    }

    public void setTipoTienda(MutableLiveData<String> tipoTienda) {
        this.tipoTienda = tipoTienda;
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