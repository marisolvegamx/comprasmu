package com.example.comprasmu.ui.visita;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.comprasmu.R;
import com.example.comprasmu.data.PeticionesServidor;

import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.Visita;

import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.util.List;

public class ListaVisitasViewModel extends AndroidViewModel {
    private final VisitaRepositoryImpl visitaRepository;
    private LiveData<List<Visita>> listas;
    private  LiveData<Integer> size;
    private  LiveData<Boolean> empty;
    private final static String TAG="ListaVisitasNewModel";
    private final MutableLiveData<String> mSnackbarText = new MutableLiveData<>();
    private String ciudadSel;
    private String nombreTienda;
    private String indiceSel;
    private Application application;

    public ListaVisitasViewModel(Application application) {
        super(application);
        this.application=application;
        visitaRepository = new VisitaRepositoryImpl(application);

    }

    public void cargarDetalles(){
        listas =visitaRepository.getSearchResults(indiceSel, nombreTienda,ciudadSel);
        size = Transformations.map(listas, res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
    }

    int i;
    public void eliminarVisita(int id){
        //solo puedo eliminar si no tiene informes
        InformeCompraRepositoryImpl infrepo=new InformeCompraRepositoryImpl(application);
        LiveData<List<InformeCompra>> informes=infrepo.getAllByVisita(id);
        informes.observeForever(new Observer<List<InformeCompra>>() {
            @Override
            public void onChanged(List<InformeCompra> informeCompras) {
                informes.removeObserver(this);
                if(informeCompras.size()>0){
                    //no puedo borrar
                    mSnackbarText.setValue("No se puede eliminar");
                }else{
                    Visita im=null;
                    for( i=0;i<listas.getValue().size();i++){
                        im=listas.getValue().get(i);
                        if(im.getId()==id){
                            break;
                        }
                    }
                    Log.d(TAG,"borrando el"+id);

                   LiveData<Visita> eliminar= visitaRepository.find(id);
                   eliminar.observeForever(new Observer<Visita>() {
                        @Override
                        public void onChanged(Visita visita) {
                            eliminar.removeObserver(this);
                            visitaRepository.delete(visita);

                            mSnackbarText.setValue("Se eliminó correctamente");

                        }
                    });


                }
            }
        });



    }


    public LiveData<List<Visita>> getListas() {
        return listas;
    }

    public LiveData<Integer> getSize() {
        return size;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }



    public String getCiudadSel() {
        return ciudadSel;
    }



    public String getIndiceSel() {
        return indiceSel;
    }

    public MutableLiveData<String> getmSnackbarText() {
        return mSnackbarText;
    }

    public void setCiudadSel(String ciudadSel) {
        this.ciudadSel = ciudadSel;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public void setIndiceSel(String indiceSel) {
        this.indiceSel = indiceSel;
    }
}