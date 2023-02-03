package com.example.comprasmu.ui.solcorreccion;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.remote.MuestraCancelada;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class ListaSolsViewModel extends AndroidViewModel {
    private final SolicitudCorRepoImpl repository;
    private  LiveData<Integer> size;
    private  LiveData<Boolean> empty;
    private final static String TAG="ListaSolsViewModel";
    ImagenDetRepositoryImpl imrepo;
    InfEtapaDetRepoImpl etadetRepo;
    InformeComDetRepositoryImpl infcrepo;
    InfEtapaRepositoryImpl infetarepo;


    public ListaSolsViewModel(Application application) {
        super(application);
        repository = new SolicitudCorRepoImpl(application);
        infcrepo=new InformeComDetRepositoryImpl(application);
        infetarepo=new InfEtapaRepositoryImpl(application);

    }

    public LiveData<List<SolicitudCor>>  cargarDetalles(int etapa,String indiceSel, int estatus){
        LiveData<List<SolicitudCor>> listas =repository.getAll(etapa,indiceSel, estatus);
        size = Transformations.map(listas, res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
        return listas;
    }
    public LiveData<List<SolicitudCor>>  cargarDetallesPlan(int etapa,String indiceSel,int plantaSel, int estatus){
        LiveData<List<SolicitudCor>> listas =repository.getAllPlan(etapa,indiceSel,plantaSel, estatus);
        size = Transformations.map(listas, res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
        return listas;
    }
    public LiveData<Integer> getTotalSols(int etapa, String indiceSel, int estatus){
        Log.d(TAG,"wwww"+ Constantes.ETAPAACTUAL+","+Constantes.INDICEACTUAL);

        return repository.totalSols(etapa,indiceSel, estatus);
    }
    public int  getTotalSolsxplanta(int etapa,String indiceSel,int estatus, int planta){

        return repository.totalSolsxPlanta(etapa,indiceSel, estatus, planta);
    }
    public LiveData<SolicitudCor>  getSolicitud(int id,int numfoto){
        LiveData<SolicitudCor> solicitud =repository.find(id,numfoto);
        return solicitud;
    }
    public MutableLiveData<Integer>  getTotalCancel(String indiceSel){
        Log.d(TAG,"wwww*"+ Constantes.ETAPAACTUAL+","+Constantes.INDICEACTUAL);

        return infcrepo.gettotCancelados(indiceSel);
    }
    public void actualizarEstSolicitud(int id,int numfoto, int estatus){
        Log.d(TAG,"actalizando"+id+"--"+estatus);
      repository.actualizarEstatus(id,numfoto,estatus);

    }
    public LiveData<ImagenDetalle> buscarImagenCom(int numfoto){
        imrepo=new ImagenDetRepositoryImpl(getApplication());
        return imrepo.find(numfoto);
    }

    public LiveData<InformeEtapaDet> buscarEtapaDet(int iddet){
        etadetRepo=new InfEtapaDetRepoImpl(getApplication());
        return etadetRepo.find(iddet);
    }

    public LiveData<InformeEtapaDet> buscarFotoEta(int numfoto,int idinf, int etapa){
        etadetRepo=new InfEtapaDetRepoImpl(getApplication());
        return etadetRepo.getBynumfoto(idinf,etapa,numfoto);
    }
    public LiveData<List<ImagenDetalle>> buscarFotosEta(int idinf, int etapa){
        etadetRepo=new InfEtapaDetRepoImpl(getApplication());
        return etadetRepo.getImagenxInf(idinf,etapa);
    }

    public LiveData<Integer> getSize() {
        return size;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }

    public InformeCompraDetalle buscarInformeFoto(int informesId, int numfoto, String indiceactual) {
        return infcrepo.findByInformeFoto(informesId,numfoto);
    }
    public InformeCompraDetalle buscarInformeByFoto(int informesId, int numfoto, int descrId) {
        return infcrepo.getInformeByFoto(informesId,numfoto, descrId);
    }

    public void procesarCanceladas(MuestraCancelada cancelada){
        InformeCompraDetalle det=infcrepo.findsimple(cancelada.getInd_id());

        if(det!=null) {
            if (det.getEstatus() != 2&&det.getEstatus()!=4)//no está cancelada
            {
                String codigo=Constantes.sdfcaducidad.format(det.getCaducidad());
                ListaCompraDetRepositoryImpl lcdrepo = new ListaCompraDetRepositoryImpl(getApplication());
                ListaCompraDetalle compradet = lcdrepo.findsimple(cancelada.getInd_comprasid(), cancelada.getInd_compraddetid());
                if (compradet != null) {
                    //quito la comprada
                    if (compradet.getComprados() > 0) {
                        int cantidad = compradet.getComprados() - 1;
                        lcdrepo.actualizarComprados(compradet.getId(), compradet.getListaId(), cantidad);
                    }
                    //quito en nuevo codigo
                    if (compradet.getNvoCodigo()!=null&&compradet.getNvoCodigo() != "") {
                       // Log.d(TAG,"quitando el codigo"+compradet.getNvoCodigo());
                       // Log.d(TAG,det.getId()+"--"+det.getInformesId()+"--"+codigo);

                        String nuevoscods = compradet.getNvoCodigo().replace(codigo + ";", "");//elimino elcodigo
                        nuevoscods = compradet.getNvoCodigo().replace(codigo, "");//elimino elcodigo

                       // Log.d(TAG,compradet.getId()+"--"+compradet.getListaId()+"--"+nuevoscods);
                        lcdrepo.actualizarNvosCodigos(compradet.getId(), compradet.getListaId(), nuevoscods);
                    }
                }
            }
                det.setMotivoCancel(cancelada.getVas_observaciones());
                det.setFechaCancel(cancelada.getVas_fecha());
                det.setEstatus(2);

                infcrepo.insert(det);
                infcrepo.actualizarEstatus(det.getId(), 2);



            }


    }

    public void procesarCanceladasEta(MuestraCancelada cancelada){
         InformeEtapa det=infetarepo.findsimple(cancelada.getInd_id());

        if(det!=null) {

            det.setMotivoCancel(cancelada.getVas_observaciones());
            det.setFechaCancel(cancelada.getVas_fecha());
            det.setEstatus(2);

            infetarepo.insert(det);
            infetarepo.actualizarEstatus(det.getId(), 2);



        }


    }
}