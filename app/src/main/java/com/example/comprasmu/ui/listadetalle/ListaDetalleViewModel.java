package com.example.comprasmu.ui.listadetalle;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.util.List;

public class ListaDetalleViewModel extends AndroidViewModel {

    private final ListaCompraRepositoryImpl repository;
    private ListaCompraDetRepositoryImpl detRepo;

  //  private final LiveData<ListaWithDetalle> filter = new LiveData<ListaWithDetalle>("*");

    private  LiveData<List<ListaWithDetalle>> listas;
    private final MutableLiveData<Event<Integer>> mOpenListaCompraEvent = new MutableLiveData<>();
    private  LiveData<Integer> size;

    private  LiveData<Boolean> empty;
    public ListaCompra listaSelec;
    private int clienteSel;
    private int plantaSel;
    public String nombrePlantaSel;
    public int ciudadSel;
    public String nombreCiudadSel;
    private static final String TAG=ListaDetalleViewModel.class.getCanonicalName();
    private boolean nuevaMuestra=false;  //indica si se agregará muestra
    Context context;
    public ListaDetalleViewModel(Application application) {
        super(application);
        ListaCompraDao dao=ComprasDataBase.getInstance(application).getListaCompraDao();
        repository = ListaCompraRepositoryImpl.getInstance(dao);
        context=application;
    }


    public void cargarDetalles(){


       listas =repository.getListaWithDetalleByFiltros(Constantes.INDICEACTUAL,plantaSel,clienteSel);
        size = Transformations.map(listas,res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
    }

    public  LiveData<List<ListaCompra>>  cargarPestañas(String ciudadSel){

        return repository.getAllByIndiceCiudad(Constantes.INDICEACTUAL,ciudadSel);


    }
    public  LiveData<List<ListaCompra>>  cargarClientes(String ciudadSel){

        return repository.getClientesByIndiceCiudad(Constantes.INDICEACTUAL,ciudadSel);


    }
    //actualiza comprados y codigos no permitidos
    public void comprarMuestra(int idDetalle,String nuevoCodigo){
        detRepo=new ListaCompraDetRepositoryImpl(context);
        ListaCompraDetalle listaCompraDetalle=detRepo.findsimple(idDetalle);

                //valido que se pueda comprar
                if(listaCompraDetalle.getCantidad()>=listaCompraDetalle.getComprados()+1){
                  //  detRepo.actualizarComprados(idDetalle,1);
                    listaCompraDetalle.setComprados(1);
                    //lo pongo al inicio porque viene de manera descendente
                    String listaCodigos=nuevoCodigo+";"+listaCompraDetalle.getCodigosNoPermitidos();
                    listaCompraDetalle.setCodigosNoPermitidos(listaCodigos);
                    //actualizo
                    detRepo.insert(listaCompraDetalle);
                    //agrego codigo no permitido
                 //   detRepo.find(idDetalle).removeObserver(this);
                }


        Log.d(TAG,"Se actualizo la lista de compras id="+idDetalle);
    }


    public void sumarTotales(){

    }



    public void codigosNoPermitidos(String categoryId) {
     //   mOpenListaCompraEvent.setValue(new Event<>(categoryId));
    }

    public LiveData<List<ListaWithDetalle>> getListas() {
        return listas;
    }

    public MutableLiveData<Event<Integer>> getmOpenListaCompraEvent() {
        return mOpenListaCompraEvent;
    }

    public LiveData<Integer> getSize() {
        return size;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }

    public int getClienteSel() {
        return clienteSel;
    }

    public int getPlantaSel() {
        return plantaSel;
    }

    public void setClienteSel(int clienteSel) {
        this.clienteSel = clienteSel;
    }

    public boolean isNuevaMuestra() {
        return nuevaMuestra;
    }

    public void setNuevaMuestra(boolean nuevaMuestra) {
        this.nuevaMuestra = nuevaMuestra;
    }

    public void setPlantaSel(int plantaSel) {
        this.plantaSel = plantaSel;
    }
}
