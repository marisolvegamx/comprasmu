package com.example.comprasmu.ui.correccion;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.modelos.SolicitudWithCor;
import com.example.comprasmu.data.remote.CorreccionEnvio;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.utils.Constantes;

import java.util.Date;
import java.util.List;


public class NvaCorreViewModel extends AndroidViewModel {

    private final CorreccionRepoImpl correpository;
    private final SolicitudCorRepoImpl solRepo;
    private int idNuevo;
    private Correccion nvocorreccion;
    final String TAG="NvaCorreViewModel";
    Application application;

    public NvaCorreViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.correpository = new CorreccionRepoImpl(application);
        this.solRepo=new SolicitudCorRepoImpl(application);

    }

    public int insertarCorreccion(int solicitudid,int numFoto,String ruta1, String ruta2,String ruta3){
        Correccion correccion=new Correccion();
        correccion.setSolicitudId(solicitudid);
        correccion.setRuta_foto1(ruta1);
        correccion.setRuta_foto2(ruta2);
        correccion.setRuta_foto3(ruta3);
        correccion.setEstatusSync(0);
        correccion.setEstatus(1);
        correccion.setNumfoto(numFoto);

        correccion.setCreatedAt(new Date());
        idNuevo=(int)correpository.insert(correccion);
        correccion.setId(idNuevo);
        this.nvocorreccion =correccion;
        return idNuevo;

    }

    public CorreccionEnvio prepararEnvio(Correccion nvacorreccion){
        CorreccionEnvio envio=new CorreccionEnvio();

        envio.setCorreccion(nvacorreccion);
        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        return envio;

    }
    public Correccion getCorreccionesxid(int idcor,String indice, int etapa){
        return correpository.findsimple(idcor);
    }
    public LiveData<List<SolicitudWithCor>> getCorreccionesxEta(int etapa, String indice, int plantaSel){
        return correpository.getAllxEtaPlan(plantaSel,indice,etapa);
    }
    public LiveData<Correccion> getCorreccion(int id){
        Log.d(TAG,"ESTOY AQUI");
        return correpository.find(id);
    }
    public SolicitudCor getSolicitud(int id, int numfoto){

        return solRepo.findsimple(id,numfoto);
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