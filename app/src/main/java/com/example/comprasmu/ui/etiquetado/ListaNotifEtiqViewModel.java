package com.example.comprasmu.ui.etiquetado;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;
import java.util.List;

public class ListaNotifEtiqViewModel extends AndroidViewModel {

    private final InfEtapaDetRepoImpl detrepository;

    private LiveData<List<InformeEtapaDet>> cancelados;
    private LiveData<List<InformeEtapa>> infcancelados;
     private  LiveData<Integer> size;

    private  LiveData<Boolean> empty;
    private final static String TAG="ListaNotifEtiqViewModel";
    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();

    private InfEtapaRepositoryImpl inferepo;

    public ListaNotifEtiqViewModel(Application application) {
        super(application);
        inferepo=new InfEtapaRepositoryImpl(application);

        detrepository = new InfEtapaDetRepoImpl(application);
    }


    public void cargarDetalles(){
        cancelados =detrepository.getCanceladasEtiq(Constantes.INDICEACTUAL);
        size = Transformations.map(cancelados, res->{ return cancelados.getValue().size();});
        empty = Transformations.map(cancelados, res->{return cancelados.getValue().isEmpty();});
    }
    public void cargarCanceladosEtiq(){
        infcancelados =inferepo.getCanceladasEtiq(Constantes.INDICEACTUAL);
        size = Transformations.map(cancelados, res->{ return cancelados.getValue().size();});
        empty = Transformations.map(cancelados, res->{return cancelados.getValue().isEmpty();});
    }

    public List<InformeEtapa> cargarCanceladosEmp(){
        return inferepo.getCancelados(Constantes.INDICEACTUAL,4);
    }
    //para los informes etiq cuando se agregó muestras
    public List<InformeEtapa> cargarInfxCompletar(){
        return inferepo.getInformesxEstatusas(Constantes.INDICEACTUAL,3,4);
    }
    public LiveData<List<InformeEtapaDet>> getCancelados() {
        return cancelados;
    }

    public LiveData<List<InformeEtapa>> getInfcancelados() {
        return infcancelados;
    }
}