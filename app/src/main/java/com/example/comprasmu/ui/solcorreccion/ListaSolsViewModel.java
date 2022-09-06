package com.example.comprasmu.ui.solcorreccion;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import java.util.List;

public class ListaSolsViewModel extends AndroidViewModel {
    private final SolicitudCorRepoImpl repository;
    private  LiveData<Integer> size;
    private  LiveData<Boolean> empty;
    private final static String TAG="ListaSolsViewModel";
    ImagenDetRepositoryImpl imrepo;
    InfEtapaDetRepoImpl etadetRepo;


    public ListaSolsViewModel(Application application) {
        super(application);
        repository = new SolicitudCorRepoImpl(application);

    }

    public LiveData<List<SolicitudCor>>  cargarDetalles(int etapa,String indiceSel,int plantaSel, int estatus){
        LiveData<List<SolicitudCor>> listas =repository.getAll(etapa,indiceSel,plantaSel, estatus);
        size = Transformations.map(listas, res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
        return listas;
    }
    public LiveData<SolicitudCor>  getSolicitud(int id){
        LiveData<SolicitudCor> solicitud =repository.find(id);
        return solicitud;
    }
    public void actualizarEstSolicitud(int id, int estatus){
      repository.actualizarEstatus(id,estatus);

    }
    public LiveData<ImagenDetalle> buscarImagenCom(int numfoto){
        imrepo=new ImagenDetRepositoryImpl(getApplication());
        return imrepo.find(numfoto);
    }

    public LiveData<InformeEtapaDet> buscarEtapaDet(int iddet){
        etadetRepo=new InfEtapaDetRepoImpl(getApplication());
        return etadetRepo.find(iddet);
    }

    public LiveData<Integer> getSize() {
        return size;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }

}