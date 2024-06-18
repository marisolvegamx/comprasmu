package com.example.comprasmu.ui.correccion;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.comprasmu.data.modelos.CorEtiquetadoCaja;
import com.example.comprasmu.data.modelos.CorEtiquetadoCajaDet;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.remote.CorEtiquetaCajaEnvio;
import com.example.comprasmu.data.repositories.CorEtiqCajaDetRepoImpl;
import com.example.comprasmu.data.repositories.CorEtiqCajaRepoImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NvaCorreECViewModel extends AndroidViewModel {

    private final CorEtiqCajaRepoImpl corecrepository;
    private final CorEtiqCajaDetRepoImpl corecdrepository;
    private final SolicitudCorRepoImpl solRepo;
    private int idNuevo;
    private final ImagenDetRepositoryImpl imagenDetRepository;
    private CorEtiquetadoCaja nvocoreticaja;
    final String TAG="NvaCorreECViewModel";
    Application application;

    public NvaCorreECViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.corecdrepository = new CorEtiqCajaDetRepoImpl(application);
        this.solRepo=new SolicitudCorRepoImpl(application);
        this.corecrepository=new CorEtiqCajaRepoImpl(application);
        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
    }


    public int insertarCorreccionEtiqCaj(int solicitudid,String indice,int numFoto, boolean reubico){
        Log.d(TAG,"guardar correcc "+solicitudid+"--"+numFoto);

        CorEtiquetadoCaja corec=new CorEtiquetadoCaja();
        corec.setIndice(indice);
        corec.setSolicitudId(solicitudid);
        corec.setNumfoto(numFoto);
        corec.setCreatedAt(new Date());
        corec.setReubico(reubico);
        idNuevo=(int)corecrepository.insert(corec);
        corec.setId(idNuevo);
        this.nvocoreticaja =corec;
        return idNuevo;

    }

    public int insertarCorEtiqCajDet(int coretiId,String numFotoant,String ruta1, int numcaja, String descripcion, int descripcionId,String indice){
        Log.d(TAG,"guardar correcc "+coretiId+"--"+ruta1);
        ImagenDetalle foto=new ImagenDetalle();
        foto.setRuta( ruta1);
        foto.setDescripcion(descripcion);
        foto.setEstatus(1);
        foto.setEstatusSync(0);
        foto.setIndice(indice);
        foto.setCreatedAt(new Date());
        int nvoidimagem =(int)imagenDetRepository.insertImg(foto);
        CorEtiquetadoCajaDet corec=new CorEtiquetadoCajaDet();
        corec.setCoretiquetadocId(coretiId);
        corec.setRuta_fotonva(nvoidimagem);

        corec.setNumcaja(numcaja);
        corec.setDescripcion(descripcion);
        corec.setDescripcionId(descripcionId);

        int iddet=(int)corecdrepository.insert(corec);
        corec.setId(idNuevo);
       return iddet;

    }

    public void editarCorreccionEtiq(CorEtiquetadoCaja correccion){
        Log.d(TAG,"editar correcc** ");

        corecrepository.insert(correccion);

        this.nvocoreticaja =correccion;


    }

    public void editarCorreccionEtiqDet(int fotoid,String nvaruta){
        Log.d(TAG,"editar correcc ");
        ImagenDetalle imagen=imagenDetRepository.findsimple(fotoid);
        imagen.setRuta(nvaruta);
      // corecdrepository.insert(correccion);




    }
    public CorEtiquetaCajaEnvio prepararEnvio(CorEtiquetadoCaja nvacorreccion, List<CorEtiquetadoCajaDet> correcciones){
        CorEtiquetaCajaEnvio envio=new CorEtiquetaCajaEnvio();

        envio.setCorreccion(nvacorreccion);
        envio.setCordetalles(correcciones);
        //busco las imagenes
        List<ImagenDetalle> imagenes=new ArrayList<>();
        for (CorEtiquetadoCajaDet detalle:correcciones
             ) {
            ImagenDetalle imagen=this.imagenDetRepository.findsimple(detalle.getRuta_fotonva());
            imagenes.add(imagen);
        }
        envio.setImagenes(imagenes);
        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        envio.setIndice(Constantes.INDICEACTUAL);
        return envio;

    }

    public CorEtiquetadoCaja getCorreccionesxid(int idcor,String indice){
        return corecrepository.findsimple(idcor);
    }
    public CorEtiquetadoCaja getUltCorrecionxSolSimple(int idsol,int numfoto,String indice){
        //Log.d(TAG,"buscandocor cor"+correccion);
        List<CorEtiquetadoCaja> lista=corecrepository.getCorrecxSolSim(idsol,indice,numfoto);
        if(lista!=null&&lista.size()>0)
           // if( lista.get(lista.size()-1).getEstatus()==3) //devuelvo el ultimo pend
                return lista.get(lista.size()-1);
        return null;
    }

    public CorEtiquetadoCajaDet getUltCorDetSimple(int corId,int descripcionId, int numcaja){
        List<CorEtiquetadoCajaDet> lista=corecdrepository.getCorrecxdescSimple(corId,descripcionId, numcaja);
        if(lista!=null&&lista.size()>0)
         //   if( lista.get(lista.size()-1).getEstatus()==3) //devuelvo el ultimo pend
                return lista.get(lista.size()-1);
        return null;
    }
   /* public LiveData<List<CorEtiquetadoCaja>> getCorreccionesxsol(int idsol,String indice){
        return corecrepository.getxsol(idsol,indice);
    }

    public List<CorEtiquetadoCaja> getCorreccionesxsolSimp(int idsol,String indice){
        return corecrepository.getxsolSimp(idsol,indice);
    }
//las que no se han enviado
    public List<CorEtiquetadoCaja> getCorreccionesxsolPendSimp(int idsol,String indice){
        return corecrepository.getxsolPendSimp(idsol,indice,0);
    }



 /*   public LiveData<Correccion> getCorreccion(int id){
        Log.d(TAG,"ESTOY AQUI");
        return corecrepository.find(id);
    }
    public SolicitudCor getSolicitud(int id, int numfoto){

        return solRepo.findsimple(id,numfoto);
    }*/
    public int getIdNuevo() {
        return idNuevo;
    }

    public void setIdNuevo(int idNuevo) {
        this.idNuevo = idNuevo;
    }

    public CorEtiquetadoCaja getNvocoreticaja() {
        return nvocoreticaja;
    }

    public void setNvocoreticaja(CorEtiquetadoCaja nvocoreticaja) {
        this.nvocoreticaja = nvocoreticaja;
    }

    public  List<CorEtiquetadoCajaDet> getCorreccionesDet(int idNuevo) {
        return corecdrepository.getAllByCorId(idNuevo);
    }
}