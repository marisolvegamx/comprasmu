package com.example.comprasmu.ui.informe;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;


import com.example.comprasmu.R;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCancelar;
import com.example.comprasmu.data.modelos.InformeCompra;

import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.modelos.VisitaWithInformes;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Event;

import java.util.ArrayList;
import java.util.List;

public class ListaInformesViewModel extends AndroidViewModel {
    private final InformeCompraRepositoryImpl repository;
    private final VisitaRepositoryImpl visitaRepo;
    private final InformeComDetRepositoryImpl detrepository;
    private ImagenDetRepositoryImpl imrepository;

    private LiveData<List<InformeCompraDao.InformeCompravisita>> listas;
     private  LiveData<Integer> size;

    private  LiveData<Boolean> empty;
    private final static String TAG="ListaInformeNewModel";
    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();

    private String clienteSel;
    private String plantaSel;
    private String ciudadSel;
    private String nombreTienda;
    private String indiceSel;




    public ListaInformesViewModel(Application application) {
        super(application);
        repository = new InformeCompraRepositoryImpl(application);
        imrepository=new ImagenDetRepositoryImpl(application);
         detrepository = new InformeComDetRepositoryImpl(application);
        visitaRepo = new VisitaRepositoryImpl(application);


    }


    public void cargarDetalles(){
        listas =repository.getInformesVisitas(indiceSel, nombreTienda,ciudadSel,plantaSel,clienteSel);
        size = Transformations.map(listas, res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
    }

    int i;
    int pos=0;
    public void cancelarInforme(int id){

        VisitaWithInformes im=null;

        //busco cual es en la lista
        for( i=0;i<listas.getValue().size();i++){
             InformeCompraDao.InformeCompravisita info=listas.getValue().get(i);
            if(info.idinforme==id){
                pos=i;
                break;
            }
        }
        //busco la imagen y cambio el estatus
        LiveData<InformeWithDetalle> informe=repository.getDao().getInformeWithDetalleById(id);
        Log.d(TAG,"informe id seleccionado="+id);
        informe.observeForever( new Observer<InformeWithDetalle>() {
            @Override
            public void onChanged(InformeWithDetalle informeWithDetalle) {
                if(informeWithDetalle!=null) {
                    Log.d(TAG,"*informe id seleccionado="+id);
                    informeWithDetalle.informe.setEstatus(0);
                    //si ya se envio hay que actualizar el estatus sync
                    informeWithDetalle.informe.setEstatusSync(0);
                    informe.removeObserver(this);
                    repository.insertInformeCompra(informeWithDetalle.informe);

                    //cancelar detalle compra
                    detrepository.cancelAll(informeWithDetalle.informe.getId());
                    //cancelar imagenes
                    List<ImagenDetalle> fotosinfo= cancelarImagenes(informeWithDetalle.informe,informeWithDetalle.informeDetalle);

                    listas.getValue().remove(pos);

                    //reviso si hay conexion a internet
                    if (ComprasUtils.isOnlineNet()) {     //envio al servidor
                        PeticionesServidor peticion = new PeticionesServidor(Constantes.CLAVEUSUARIO);
                        InformeCancelar informeCan = new InformeCancelar();
                        informeCan.setInforme_compra(informeWithDetalle.informe);
                        informeCan.setDetalle_compra(informeWithDetalle.informeDetalle);
                        informeCan.setImagen_detalle(fotosinfo);
                        //TODO agregar  las imagenes
                        peticion.cancelarInforme(informeWithDetalle.informe.getId(), informeCan);
                    } else {     //notifico al usuario
                        mSnackbarText.setValue(new Event<>(R.string.resp_cancelar_sinconexion));

                    }
                }else
                {
                    Log.e(TAG,"algo salio mal");
                }
            }
        });



    }

    public  List<ImagenDetalle> cancelarImagenes(InformeCompra informe, List<InformeCompraDetalle> detalles){

        List<ImagenDetalle> fotosinfo=new ArrayList<>();
        //las del informe

        LiveData<ImagenDetalle> una=imrepository.find(informe.getCondiciones_traslado());
        una.observeForever(new Observer<ImagenDetalle>() {
            @Override
            public void onChanged(ImagenDetalle imagenDetalle) {
                fotosinfo.add(imagenDetalle);
                una.removeObserver(this);
                imrepository.cancelar(imagenDetalle.getId(),0);

            }
        });
        LiveData<ImagenDetalle> dos=imrepository.find(informe.getCondiciones_traslado());
        dos.observeForever(new Observer<ImagenDetalle>() {
            @Override
            public void onChanged(ImagenDetalle imagenDetalle) {
                fotosinfo.add(imagenDetalle);
                dos.removeObserver(this);
                imrepository.cancelar(imagenDetalle.getId(),0);

            }
        });

        //las del los detalles
        for(InformeCompraDetalle detalle:detalles) {
            List<Integer> fotos=detrepository.getInformesWithImagen(detalle.getId());
            List<ImagenDetalle> imagenDetalles=imrepository.findListsencillo(fotos);
                   for(ImagenDetalle imagendet:imagenDetalles){
                       fotosinfo.add(imagendet);
                       imagendet.setEstatus(0);
                       imagendet.setEstatusSync(0);
                       imrepository.insert(imagendet);
                   }

        }

        return fotosinfo;

    }
    public  LiveData<List<InformeCompra>>  cargarPestañas(String indice, String nombreTienda, String ciudad, String planta,int clienteSel){

            return repository.getPlantasByIndice(Constantes.INDICEACTUAL,nombreTienda,ciudad,planta,clienteSel);


    }
    public MutableLiveData<Event<Integer>> getSnackbarText() {
        return mSnackbarText;
    }

   /* public  LiveData<List<InformeCompra>>  cargarPestañas(){
        return repository.getClientesByIndice(Constantes.INDICEACTUAL, nombreTienda,ciudadSel,plantaSel,clienteSel);


    }*/
   /* public void eliminarVisita(int id){
        //solo puedo eliminar si no tiene informes

        Visita im=null;
        for( i=0;i<listas.getValue().size();i++){
            im=listas.getValue().get(i);
            if(im.getId()==id){
                break;
            }
        }
        //busco la imagen y cambio el estatus
        LiveData<InformeWithDetalle> informe=repository.getInformeWithDetalleById(id);
        Log.d(TAG,"informe id seleccionado="+id);
        informe.observeForever( new Observer<InformeWithDetalle>() {
            @Override
            public void onChanged(InformeWithDetalle informeWithDetalle) {
                if(informeWithDetalle!=null) {
                    informeWithDetalle.informe.setEstatus(0);
                    //si ya se envio hay que actualizar el estatus sync
                    informeWithDetalle.informe.setEstatusSync(0);
                    repository.insertVisita(informeWithDetalle.informe);
                    //cancelar detalle compra
                    detrepository.cancelAll(informeWithDetalle.informe.getId());
                    //cancelar imagenes
                    /*********todo
                     * pendiente
                     */
                    // imrepository.cancelAll(idInforme);*/
               //     listas.getValue().remove(i);
                    //reviso si hay conexion a internet
               /*     if (ComprasUtils.isOnlineNet()) {     //envio al servidor
                        PeticionesServidor peticion = new PeticionesServidor(Constantes.CLAVEUSUARIO);
                        InformeCancelar informeCan = new InformeCancelar();
                        informeCan.setInforme_compra(informeWithDetalle.informe);
                        informeCan.setDetalle_compra(informeWithDetalle.informeDetalle);
                        //TODO agregar  las imagenes
                        peticion.cancelarInforme(informeWithDetalle.informe.getId(), informeCan);
                    } else {     //notifico al usuario
                        mSnackbarText.setValue(new Event<>(R.string.resp_cancelar_sinconexion));

                    }
                }else
                {
                    Log.e(TAG,"algo salio mal");
                }
            }
        });



    }*/

    public LiveData<Integer> getSize() {
        return size;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }

    public void setClienteSel(String clienteSel) {
        this.clienteSel = clienteSel;
    }

    public void setPlantaSel(String plantaSel) {
        this.plantaSel = plantaSel;
    }

    public String getCiudadSel() {
        return ciudadSel;
    }



    public String getIndiceSel() {
        return indiceSel;
    }

    public LiveData<List<InformeCompraDao.InformeCompravisita>> getListas() {
        return listas;
    }

    public String getClienteSel() {
        return clienteSel;
    }

    public String getPlantaSel() {
        return plantaSel;
    }

    public void setCiudadSel(String ciudadSel) {
        this.ciudadSel = ciudadSel;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public void setIndiceSel(String indiceSel) {
        this.indiceSel = indiceSel;
    }
}