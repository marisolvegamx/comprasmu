package com.example.comprasmu.ui.solcorreccion;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEnvioPaq;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.remote.MuestraCancelada;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InfGastoDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeEnvioRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
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
    InformeCompraRepositoryImpl infrepo;
    InformeEnvioRepositoryImpl infenvrepo;
    ListaCompraRepositoryImpl lcrepo;
    ListaCompraDetRepositoryImpl lcdrepo;

    private final InfGastoDetRepositoryImpl gasdetrepo;

    public ListaSolsViewModel(Application application) {
        super(application);
        repository = new SolicitudCorRepoImpl(application);
        infcrepo=new InformeComDetRepositoryImpl(application);
        infetarepo=new InfEtapaRepositoryImpl(application);
        infrepo=new InformeCompraRepositoryImpl(application);
        infenvrepo=new InformeEnvioRepositoryImpl(application);
        ListaCompraDao dao= ComprasDataBase.getInstance(application).getListaCompraDao();
        lcrepo=ListaCompraRepositoryImpl.getInstance(dao);
        this.gasdetrepo = new InfGastoDetRepositoryImpl(application);
        lcdrepo=new ListaCompraDetRepositoryImpl(application);
    }

    public LiveData<List<SolicitudCor>>  cargarDetalles(int etapa,String indiceSel, int estatus){
        LiveData<List<SolicitudCor>> listas =repository.getAll(etapa,indiceSel, estatus);
        size = Transformations.map(listas, res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
        return listas;
    }
    public LiveData<List<SolicitudCor>>  cargarDetallesAll(String indiceSel){
        LiveData<List<SolicitudCor>> listas =repository.getSolicitudPendAll(indiceSel);
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
    public LiveData<List<SolicitudCor>>  cargarDetallesVis(int etapa,String indiceSel, int estatus){
        LiveData<List<SolicitudCor>> listas =repository.getAllVisita(etapa,indiceSel, estatus);
        size = Transformations.map(listas, res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
        return listas;
    }
    public LiveData<Integer> getTotalSols(int etapa, String indiceSel, int estatus){

        return repository.totalSols(etapa,indiceSel, estatus);
    }
    public int getTotalSolsGen( String indiceSel, int estatus){

        return repository.totalSolsGen(indiceSel, estatus);
    }


    public LiveData<Integer> getTotalSolsxCd(int etapa, String indiceSel, int estatus, String ciudad){
      //  Log.d(TAG,"wwww"+ Constantes.ETAPAACTUAL+","+Constantes.INDICEACTUAL);

        return repository.totalSolsxPlantaxCd(etapa,indiceSel, estatus,ciudad);
    }


    public LiveData<Integer> getTotSolsEtiqxCd(int etapa, String indiceSel, int estatus, String ciudad){

        return repository.getTotSolsEtiqxCd(etapa,indiceSel, estatus,ciudad);
    }
    public int  getTotalSolsxplanta(int etapa,String indiceSel,int estatus, int planta){
        Log.d(TAG,"getTotalSolsxplanta"+etapa+"--"+indiceSel+"--"+estatus+"--"+planta);
        return repository.totalSolsxPlanta(etapa,indiceSel, estatus, planta);
    }
    public int  getTotalSolsxplantaAll(String indiceSel,int estatus, int planta){
        Log.d(TAG,"getTotalSolsxplantaAll"+indiceSel+"--"+estatus+"--"+planta);
        return repository.totSolsxPlantaAll(indiceSel, estatus, planta);
    }
    public int  getTotSolsVis(String indiceSel,int estatus, String ciudad){
        Log.d(TAG,"getTotalSolsxplanta"+indiceSel+"--"+estatus+"--");
        return repository.getTotSolsVis(2,indiceSel, estatus, ciudad);
    }
    public LiveData<SolicitudCor>  getSolicitud(int id,int numfoto){
        LiveData<SolicitudCor> solicitud =repository.find(id,numfoto);
        return solicitud;
    }
    /*public MutableLiveData<Integer>  getTotalCancel(String indiceSel){
        Log.d(TAG,"wwww*"+ Constantes.ETAPAACTUAL+","+Constantes.INDICEACTUAL);

        return infcrepo.gettotCancelados(indiceSel);
    }*/

    public List<InformeCompraDetalle> getTotalCancel(String indiceSel ){
        return infcrepo.getCanceladosSim(indiceSel);

    }
    //por ahora aplica preparacion y etiq donde se cancela todo el info
    public LiveData<List<InformeEtapa>> getTotalCancelEta(String indiceSel ){
        return infetarepo.getInformesxEstatusAll(indiceSel,0);

    }

    //por ahora aplica preparacion y etiq donde se cancela todo el info
   /* public LiveData<List<InformeEtapa>> getTotalCancelEtaxCd(String indiceSel, int etapa ,String cd){
        return infetarepo.getInformesxEstatus(indiceSel,etapa,0);

    }*/

    public void actualizarEstSolicitud(int id,int numfoto, int estatus){
        Log.d(TAG,"actalizando"+id+"--"+numfoto);
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
    //el estatus viene como 2 pero en la app 0 es cancelado y 2 es finalizado
    public void procesarCanceladasEta(MuestraCancelada cancelada){
         InformeEtapa det=infetarepo.findsimple(cancelada.getInf_id());

        if(det!=null) {
            Log.e(TAG,"cancele");
            det.setMotivoCancel(cancelada.getVas_observaciones());
            det.setFechaCancel(cancelada.getVas_fecha());
            det.setEstatus(0);

            infetarepo.insert(det);
            infetarepo.actualizarEstatus(det.getId(), 0);



        }


    }
    //para buscar si hay un inf de etiquetado reabierto
    public List<InformeEtapa> getInfEtapaxEstatusSim(String indiceSel, int etapa, int estatus ){

        return infetarepo.getInformesxEstatusSim(indiceSel,etapa,estatus);

    }
    public  List<ListaCompra>  cargarClientesSimplxet(String ciudadSel, int etapa){

        return lcrepo.getClientesByIndiceCiudadSimplxet(Constantes.INDICEACTUAL,ciudadSel,etapa);


    }
    public  List<ListaCompra>  cargarClientesSimplxetReac(String ciudadSel, int etapa, int reactivado){

        return lcrepo.getClieByIndiceCiudadSimplxetReac(Constantes.INDICEACTUAL,ciudadSel,etapa,  reactivado);


    }
    public List<ListaCompraDetalle> getProductosPend(int idlista) {
        return lcdrepo.getPendientes(idlista);
    }
    public List<InformeEtapa> getEtiquetadoAdicional(String indiceSel ){

        return infetarepo.getInformesxEstatusSim(indiceSel,3,4);

    }
    public InformeCompra getInformeSol(int informesId) {
        return infrepo.findSimple(informesId);
    }

    public InformeEnvioPaq getInformeEnvSol(int informesId) {
        return infenvrepo.findInfsimple(informesId);
    }
    public List<InformeGastoDet> getGastoDetalles(int id){
        return gasdetrepo.getAllSencillo(id);
    }
}