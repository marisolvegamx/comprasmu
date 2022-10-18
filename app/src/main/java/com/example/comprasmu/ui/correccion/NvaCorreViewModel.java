package com.example.comprasmu.ui.correccion;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.SolicitudWithCor;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import java.util.Date;
import java.util.List;


public class NvaCorreViewModel extends AndroidViewModel {

    private CorreccionRepoImpl correpository;
    private int idNuevo;
    private Correccion nvocorreccion;
    final String TAG="NvaCorreViewModel";
    Application application;

    public NvaCorreViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.correpository = new CorreccionRepoImpl(application);

    }

    public int insertarCorreccion(int solicitudid,String ruta1, String ruta2,String ruta3){
        Correccion correccion=new Correccion();
        correccion.setSolicitudId(solicitudid);
        correccion.setRuta_foto1(ruta1);
        correccion.setRuta_foto2(ruta2);
        correccion.setRuta_foto3(ruta3);
        correccion.setEstatusSync(0);
        correccion.setEstatus(1);

        correccion.setCreatedAt(new Date());
        idNuevo=(int)correpository.insert(correccion);
        correccion.setId(idNuevo);
        this.nvocorreccion =correccion;
        return idNuevo;

    }
    public LiveData<List<SolicitudWithCor>> getCorreccionesxEta(int etapa, String indice, int plantaSel){
        return correpository.getAllxEtaPlan(plantaSel,indice,etapa);
    }
    public LiveData<SolicitudWithCor> getCorreccion(int id){
        return correpository.findSolCor(id);
    }

    public int getIdNuevo() {
        return idNuevo;
    }

    public void setIdNuevo(int idNuevo) {
        this.idNuevo = idNuevo;
    }

    public Correccion getNvocorreccion() {
        return nvocorreccion;
    }


    public void setNvocorreccion(Correccion nvocorreccion) {
        this.nvocorreccion = nvocorreccion;
    }
}