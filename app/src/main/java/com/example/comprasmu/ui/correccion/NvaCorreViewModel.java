package com.example.comprasmu.ui.correccion;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.dao.CorEtiquetadoCajaDao;
import com.example.comprasmu.data.modelos.CorEtiquetadoCaja;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.SolicitudCor;

import com.example.comprasmu.data.remote.CorreccionEnvio;
import com.example.comprasmu.data.repositories.CorEtiqCajaRepoImpl;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NvaCorreViewModel extends AndroidViewModel {

    private final CorreccionRepoImpl correpository;

    private final SolicitudCorRepoImpl solRepo;
    private int idNuevo;
    private Correccion nvocorreccion;
    private CorEtiquetadoCaja nvocoreticaja;
    final String TAG="NvaCorreViewModel";
    Application application;
    private CorEtiqCajaRepoImpl cocajaRepo;


    public NvaCorreViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.correpository = new CorreccionRepoImpl(application);
        this.solRepo=new SolicitudCorRepoImpl(application);
        this.cocajaRepo=new CorEtiqCajaRepoImpl(application);

    }

    public int insertarCorreccion(int solicitudid,String indice,int numFoto,String ruta1, String ruta2,String ruta3,String ruta4){
        Correccion correccion=new Correccion();
        correccion.setSolicitudId(solicitudid);
        correccion.setRuta_foto1(ruta1);
        correccion.setRuta_foto2(ruta2);
        correccion.setRuta_foto3(ruta3);
        correccion.setRuta_foto4(ruta4);
        correccion.setEstatusSync(0);
        correccion.setEstatus(1);
        correccion.setIndice(indice);
        correccion.setNumfoto(numFoto);

        correccion.setCreatedAt(new Date());
        idNuevo=(int)correpository.insert(correccion);
        correccion.setId(idNuevo);
        this.nvocorreccion =correccion;
        return idNuevo;

    }

    public Correccion insertarCorreccion2(int solicitudid,String indice,int numFoto,String ruta1, String ruta2,String ruta3){
        Correccion correccion=new Correccion();
        correccion.setSolicitudId(solicitudid);
        correccion.setRuta_foto1(ruta1);
        correccion.setRuta_foto2(ruta2);
        correccion.setRuta_foto3(ruta3);
        correccion.setEstatusSync(0);
        correccion.setEstatus(1);
        correccion.setIndice(indice);
        correccion.setNumfoto(numFoto);

        correccion.setCreatedAt(new Date());
        idNuevo=(int)correpository.insert(correccion);
        correccion.setId(idNuevo);
        this.nvocorreccion =correccion;
        return correccion;

    }
    public int insertarCorreccionEtiq(int solicitudid,String indice,int numFoto,String ruta1, String ruta2,String ruta3, String dato1){
        Log.d(TAG,"guardar correcc "+solicitudid+"--"+numFoto+"--"+ruta1);

        Correccion correccion=new Correccion();
        correccion.setSolicitudId(solicitudid);
        correccion.setRuta_foto1(ruta1);

        correccion.setEstatusSync(0);
        correccion.setEstatus(1);
        correccion.setIndice(indice);
        correccion.setNumfoto(numFoto);
        correccion.setDato1(dato1);
        correccion.setCreatedAt(new Date());
        idNuevo=(int)correpository.insert(correccion);
        correccion.setId(idNuevo);
        this.nvocorreccion =correccion;
        return idNuevo;

    }

   public void editarCorreccionEtiq(Correccion correccion){
        Log.d(TAG,"editar correcc ");

        correpository.insert(correccion);

        this.nvocorreccion =correccion;


    }
    public CorreccionEnvio prepararEnvio(Correccion nvacorreccion){
        CorreccionEnvio envio=new CorreccionEnvio();

        envio.setCorreccion(nvacorreccion);
        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        return envio;

    }
    public CorreccionEnvio prepararEnvioVar(List<Correccion> nvacorreccion){
        CorreccionEnvio envio=new CorreccionEnvio();

        envio.setCorrecciones(nvacorreccion);
        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        return envio;

    }
    public Correccion getCorreccionesxid(int idcor,String indice, int etapa){
        return correpository.findsimple(idcor);
    }

    public LiveData<List<Correccion>> getCorreccionesxsol(int idsol,String indice){
        return correpository.getxsol(idsol,indice);
    }

    public List<Correccion> getCorreccionesxsolSimp(int idsol,String indice){
        return correpository.getxsolSimp(idsol,indice);
    }
//las que no se han enviado
    public List<Correccion> getCorreccionesxsolPendSimp(int idsol,String indice){
        return correpository.getxsolPendSimp(idsol,indice,0);
    }

    public Correccion getUltCorrecionxSolSimple(int idsol,int numfoto,String indice){
       List<Correccion> lista=correpository.getCorrecxSolSim(idsol,numfoto,indice);
       if(lista!=null&&lista.size()>0)
         //  if( lista.get(lista.size()-1).getEstatus()==3) //devuelvo el ultimo pend
                return lista.get(lista.size()-1);
        return null;
    }
    public Correccion getCorrecionxSolSimple(int idsol,int numfoto,String indice){
        List<Correccion> lista=correpository.getCorrecxSolSim(idsol,numfoto,indice);
        if(lista!=null&&lista.size()>0)
            return lista.get(lista.size()-1);
        return null;
    }
    public List<CorreccionWithSol> getCorreccionesxEtaPlan(int etapa, String indice, int plantaSel){
        List<CorreccionWithSol> resp=new ArrayList<>();

        List<Correccion> listacor=correpository.getAllxEtaPlan(plantaSel,indice,etapa);
        CorreccionWithSol solwcor=null;
        if(listacor!=null)
            for (Correccion cor:listacor) {
               //busco la correccion
                SolicitudCor sol= solRepo.findsimple(cor.getSolicitudId(), cor.getNumfoto());
                solwcor=new CorreccionWithSol();
                solwcor.solicitud=sol;
                solwcor.correccion=cor;
                resp.add(solwcor);
            }
        return resp;
    }
    public List<CorreccionWithSol> getCorreccionesAll( String indice){
        List<CorreccionWithSol> resp=new ArrayList<>();

        List<Correccion> listacor=correpository.getAll(indice);
        CorreccionWithSol solwcor=null;
        if(listacor!=null)
            for (Correccion cor:listacor) {
                //busco la correccion
                SolicitudCor sol= solRepo.findsimple(cor.getSolicitudId(), cor.getNumfoto());
                solwcor=new CorreccionWithSol();
                solwcor.solicitud=sol;
                solwcor.correccion=cor;
                resp.add(solwcor);
            }
        return resp;
    }
    public List<CorreccionWithSol> getCorreccionesxEta(int etapa, String indice, int plantaSel){

        List<CorreccionWithSol> resp=new ArrayList<>();

        List<Correccion> listacor=correpository.getAllxEta(indice,etapa);
        CorreccionWithSol solwcor=null;
        if(listacor!=null)
            for (Correccion cor:listacor) {
                //busco la correccion
                SolicitudCor sol= solRepo.findsimple(cor.getSolicitudId(), cor.getNumfoto());
                solwcor=new CorreccionWithSol();
                solwcor.solicitud=sol;
                solwcor.correccion=cor;
                resp.add(solwcor);
            }

        return resp;
    }
    public List<CorreccionWithSol> getCorCajaAll( String indice){
        //paso de correccioncaja a correccion normal
        List<CorreccionWithSol> resp=new ArrayList<>();

        List<CorEtiquetadoCaja> listacor=cocajaRepo.getAllSim(indice);
        CorreccionWithSol solwcor=null;
        Correccion corTemp=null;
        if(listacor!=null)
            for (CorEtiquetadoCaja cor:listacor) {
                //busco la correccion
                SolicitudCor sol= solRepo.findsimple(cor.getSolicitudId(), cor.getNumfoto());
                solwcor=new CorreccionWithSol();
                solwcor.solicitud=sol;
                corTemp=new Correccion();
                corTemp.setId(cor.getId());
                corTemp.setCreatedAt(cor.getCreatedAt());
                corTemp.setEstatus(cor.getEstatus());
                corTemp.setEstatusSync(cor.getEstatusSync());
                corTemp.setIndice(cor.getIndice());
                corTemp.setNumfoto(cor.getNumfoto());
                corTemp.setSolicitudId(cor.getSolicitudId());

                solwcor.correccion=corTemp;
                resp.add(solwcor);
            }
        return resp;
    }
    public LiveData<Correccion> getCorreccion(int id){
        Log.d(TAG,"ESTOY AQUI");
        return correpository.find(id);
    }

    public SolicitudCor getSolicitud(int id, int numfoto){

        return solRepo.findsimple(id,numfoto);
    }
    public LiveData<CorEtiquetadoCaja> getCorreccionCaja(int id){
        Log.d(TAG,"ESTOY AQUI");
        return cocajaRepo.find(id);
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