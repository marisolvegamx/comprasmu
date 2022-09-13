package com.example.comprasmu.ui.preparacion;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.comprasmu.data.modelos.Atributo;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.Reactivo;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeTempRepositoryImpl;
import com.example.comprasmu.data.repositories.ReactivoRepositoryImpl;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;
import com.example.comprasmu.utils.Event;

import java.util.Date;
import java.util.List;

public class NvaPreparacionViewModel extends AndroidViewModel {
    private InfEtapaRepositoryImpl infEtaRepository;
    private InfEtapaDetRepoImpl infDetRepo;
    private int idNuevo;
    private int iddetalle;
    private InformeEtapa nvoinforme;
    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();
    final String TAG="NvaPrepVM";
    Application application;
    public boolean variasPlantas;//indica si tengo varias plantas
   public int preguntaAct;
    public NvaPreparacionViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.infEtaRepository = new InfEtapaRepositoryImpl(application);
        this.infDetRepo = new InfEtapaDetRepoImpl(application);

    }

    public int insertarInformeEtapa(String indice,String plantaNombre,int plantaId, String clienteNombre,int clienteId){
        InformeEtapa informe=new InformeEtapa();
        informe.setIndice(indice);
        informe.setPlantaNombre(plantaNombre);
        informe.setPlantasId(plantaId);
        informe.setClientesId(clienteId);
        informe.setClienteNombre(clienteNombre);
        informe.setEstatusSync(0);
        informe.setEstatus(1);
        informe.setEtapa(1);
        informe.setCreatedAt(new Date());
        idNuevo=(int)infEtaRepository.insert(informe);
        this.nvoinforme=informe;
        return idNuevo;

    }
    public int insertarInfEtaDet(int idinf,int descripcionid,String descripcion, String ruta ,int iddet){
        InformeEtapaDet detalle=new InformeEtapaDet();
        detalle.setDescripcion(descripcion);
        detalle.setInformeEtapaId(idinf);
        detalle.setRuta_foto(ruta);
        detalle.setDescripcionId(descripcionid);
        detalle.setEstatusSync(0);
        detalle.setEtapa(1);
        if(iddet>0)
        detalle.setId(iddet);
        iddetalle=(int)infDetRepo.insert(detalle);
        return iddetalle;
    }


    public int insertarEtiq(String indice,String plantaNombre,int plantaId, String clienteNombre,int clienteId,int num_cajas, int tot_muestras){
        InformeEtapa informe=new InformeEtapa();
        informe.setIndice(indice);
        informe.setPlantaNombre(plantaNombre);
        informe.setPlantasId(plantaId);
        informe.setClientesId(clienteId);
        informe.setClienteNombre(clienteNombre);
        informe.setEstatusSync(0);
        informe.setEstatus(1);
        informe.setEtapa(3);
        informe.setTotal_cajas(num_cajas);
        informe.setTotal_muestras(tot_muestras);
        informe.setCreatedAt(new Date());
        idNuevo=(int)infEtaRepository.insert(informe);
        this.nvoinforme=informe;
        return idNuevo;

    }
    public int insertarEtiqDet(int idinf,int descripcionid,String descripcion, String ruta ,int iddet, int numcaja,String qr,int num_muestra){
        InformeEtapaDet detalle=new InformeEtapaDet();
        detalle.setDescripcion(descripcion);
        detalle.setInformeEtapaId(idinf);
        detalle.setRuta_foto(ruta);
        detalle.setDescripcionId(descripcionid);
        detalle.setNum_caja(numcaja);
        detalle.setQr(qr);
        detalle.setNum_muestra(num_muestra);
        detalle.setEstatusSync(0);
        detalle.setEtapa(3);
        if(iddet>0)
            detalle.setId(iddet);
        iddetalle=(int)infDetRepo.insert(detalle);
        return iddetalle;
    }
    public int insertarDetCaja(int idinf,int numcaja,String foto, String qr){
        DetalleCaja detalle=new DetalleCaja();
        detalle.setNum_caja(numcaja);
        detalle.setInformeEtapaId(idinf);
       // detalle.set(ruta);
      //  detalle.setDescripcionId(descripcionid);
        detalle.setEstatusSync(0);
      //  detalle.setEtapa(1);
      //  if(iddet>0)
      //      detalle.setId(iddet);
       // iddetalle=(int)infDetRepo.insert(detalle);
        return iddetalle;
    }

    public int actualizarInfEtaDet(InformeEtapaDet detalle){
        iddetalle=(int)infDetRepo.insert(detalle);
        return iddetalle;
    }
    public LiveData<InformeEtapa> getInformeEdit(int id){
     return   infEtaRepository.getInformeEtapa(id);
    }
    public InformeEtapa getInformePend(String indice){
        return   infEtaRepository.getInformePend(indice, 1);
    }
    public InformeEtapa getInformePend(String indice, int etapa){
        return   infEtaRepository.getInformePend(indice, etapa);
    }
    public LiveData<InformeEtapaDet> getDetalleEtEdit(int idinf, int preguntaAct){

        return infDetRepo.getByDescripcion("foto_preparacion"+preguntaAct,idinf);
    }
    public List<InformeEtapaDet> getDetEtaxCaja(int idinf, int etapa,int numcaja){
       Log.d(TAG,"buscando a "+idinf+"--"+numcaja);
         return infDetRepo.getByCaja(idinf, etapa, numcaja);
    }
    public void borrarDetEtaxCaja(int idinf, int etapa,int numcaja){
        // Log.d(TAG,"buscando a "+idinf+"--"+numcaja);
        infDetRepo.deleteByCaja(idinf, etapa, numcaja);
    }
    public void actualizarComentarios(int idinf, String comentarios){
        infEtaRepository.actualizarComentariosPrep(idinf,comentarios);
    }
    public void actualizarComentEtiq(int idinf, String comentarios){
        infEtaRepository.actualizarcomentariosEtiq(idinf,comentarios);
    }
    public void actualizarComentEmp(int idinf, String comentarios){
        infEtaRepository.actualizarComentariosEmp(idinf,comentarios);
    }
    public void actualizarInfEtapa(InformeEtapa informe){
        infEtaRepository.insert(informe);
    }

    public List<InformeEtapaDet> cargarInformeDet(int id){
       return infDetRepo.getAllSencillo(id);
    }
    public List<DetalleCaja> cargarDetCajas(int id){
       // muestrasCap=detalleRepo.getAllSencillo(id);
        return null;
    }
    public void finalizarInf(){
        infEtaRepository.actualizarEstatus(idNuevo,2);
    }

    public int getIdNuevo() {
        return idNuevo;
    }

    public void setIdNuevo(int idNuevo) {
        this.idNuevo = idNuevo;
    }

    public int getIddetalle() {
        return iddetalle;
    }

    public void setIddetalle(int iddetalle) {
        this.iddetalle = iddetalle;
    }

    public MutableLiveData<Event<Integer>> getmSnackbarText() {
        return mSnackbarText;
    }

    public InformeEtapa getNvoinforme() {
        return nvoinforme;
    }


    public void setNvoinforme(InformeEtapa nvoinforme) {
        this.nvoinforme = nvoinforme;
    }
}