package com.example.comprasmu.utils.ui;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.CorEtiquetadoCajaDetDao;
import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.CorEtiquetadoCajaDet;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEnvioDet;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.remote.RespInfEtapaResponse;
import com.example.comprasmu.data.repositories.CorEtiqCajaDetRepoImpl;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.data.repositories.DetalleCajaRepoImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InfGastoDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeEnvioRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.services.DescargaCambiosImagenes;
import com.example.comprasmu.ui.gasto.VerInformeGasFragment;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class InformesGenViewModel extends AndroidViewModel {

    private final InfEtapaRepositoryImpl ierepository;
    private final InfEtapaDetRepoImpl idrepository;
    private final InformeEnvioRepositoryImpl infEnvioRepo;
    private final CorreccionRepoImpl correpo;
    private final ImagenDetRepositoryImpl imagenDetRepository;
    private final DetalleCajaRepoImpl detCajaRepo;
    CorEtiqCajaDetRepoImpl cocrepo;
    private InfGastoDetRepositoryImpl infGastoDetRepo;
    CorEtiquetadoCajaDetDao corEtiquetadoDao;
    TablaVersionesRepImpl tablaVersionesRepo;
    String indice;
    String usuario;
    MutableLiveData<Boolean> verLista;

    public InformesGenViewModel(Application application) {
        super(application);
        ierepository=new InfEtapaRepositoryImpl(application);
        idrepository=new InfEtapaDetRepoImpl(application);
        correpo=new CorreccionRepoImpl(application);
        ImagenDetalleDao imagenDetalleDao= ComprasDataBase.getInstance(application).getImagenDetalleDao();
        this.imagenDetRepository= ImagenDetRepositoryImpl.getInstance(imagenDetalleDao);
        this.detCajaRepo=new DetalleCajaRepoImpl(application);
        infEnvioRepo = new InformeEnvioRepositoryImpl(application);
        infGastoDetRepo=new InfGastoDetRepositoryImpl(application);
        corEtiquetadoDao= ComprasDataBase.getInstance(application).getCorEtiquetadoCajaDetDao();
        this.cocrepo=CorEtiqCajaDetRepoImpl.getInstance(corEtiquetadoDao);
        tablaVersionesRepo=new TablaVersionesRepImpl(application);

    }


    public LiveData<List<InformeEtapa>> cargarEtapa(int etapa, String indice, int plantaid){
        return ierepository.getAll(etapa, indice, plantaid);

    }
    public LiveData<List<InformeEtapa>> cargarEtapaAll(int etapa, String indice){
        return ierepository.getActivos(etapa, indice);

    }
    public LiveData<List<InformeEtapa>> cargarPreparacion(String indice){
        return ierepository.getActivosPreparacion( indice);

    }
    public LiveData<InformeEtapa> getInforme(int id, String indice){
        return ierepository.find(id);

    }
    public LiveData<List<InformeEtapaDet>> getfotosPrep(int id){
        return idrepository.getAllxEtapa(id, 1);

    }
    public LiveData<List<InformeEtapaDet>> getfotosxetapa(int id, int etapa){
        return idrepository.getAllxEtapa(id, etapa);

    }
    public LiveData<List<InformeEtapaDet>> getfotosxetapaxcaj(int id, int etapa, int numcaja){
        return idrepository.getByCajaEmp(id, etapa, numcaja);

    }
    public ImagenDetalle getfotoxid(String id){

            int idim = Integer.parseInt(id);
            return imagenDetRepository.findsimple(idim);


    }
    public LiveData<List<DetalleCaja>> getDetalleCajaEmp(int infid){
        return detCajaRepo.getDetalles(infid);
    }

    public InformeEnvioDet getInformeEnvioDet(int idinf){
        return  infEnvioRepo.findsimple(idinf);
    }
    public ImagenDetalle getFoto(int idfoto){
        return imagenDetRepository.findsimple(idfoto);

    }
    public LiveData<List<InformeGastoDet>> getfotosGasto(int id){

        return infGastoDetRepo.getAll(id);

    }
    public List<ImagenDetalle> getfotosCorEtiq(int id){
     List<ImagenDetalle>fotos= new ArrayList<>();
       List<CorEtiquetadoCajaDet>detalles=cocrepo.getAllByCorId(id);
       ImagenDetalle imagen;
        for (CorEtiquetadoCajaDet detalle:detalles
             ) {
            imagen=imagenDetRepository.findsimple(detalle.getRuta_fotonva());
            fotos.add(imagen);
        }
        return fotos;

    }

    public LiveData<List<InformeEtapa>> cargarEtapaAll(int etapa, String indice, int estatus){
        return ierepository.getAllsp(etapa, indice, estatus);

    }
    //estatus gastos puede estar en 2 o 6
    public LiveData<List<InformeEtapa>> cargarGastos(int etapa, String indice, int estatus){
        return ierepository.getAllGastos(etapa, indice, estatus);

    }

    public void getReciboGasto(String ciudadInf, VerInformeGasFragment.ListenerResumen listenerM){
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        ps.getCambiosGastos(Constantes.INDICEACTUAL,ciudadInf,listenerM);

    }

    public void actualizarImagen(String dirLog, MutableLiveData<Boolean> verLista){

        DescargaCambiosImagenes descarga=new DescargaCambiosImagenes(dirLog,tablaVersionesRepo,imagenDetRepository,this.usuario ,this.indice , verLista);
        descarga.ejecutar();
    }

    public LiveData<RespInfEtapaResponse> actualizarInformesEtiquetado(String indice,int idInforme){

        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        return ps.getInfEtiquetado(indice,idInforme,idrepository);
    }



    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}