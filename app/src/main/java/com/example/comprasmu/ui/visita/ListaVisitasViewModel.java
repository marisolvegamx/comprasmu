package com.example.comprasmu.ui.visita;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;

import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;

import java.io.File;
import java.util.List;

public class ListaVisitasViewModel extends AndroidViewModel {
    private final VisitaRepositoryImpl visitaRepository;
    private final ListaCompraDetRepositoryImpl lcRepository;

    private final ImagenDetRepositoryImpl imdRepository;
    private final InformeComDetRepositoryImpl idrepo;
    private final ProductoExhibidoRepositoryImpl prodeRepository;
    private LiveData<List<Visita>> listas;
    private  LiveData<Integer> size;
    private  LiveData<Boolean> empty;
    private final static String TAG="ListaVisitasNewModel";
    private final MutableLiveData<String> mSnackbarText = new MutableLiveData<>();
    private String ciudadSel;
    private String nombreTienda;
    private String indiceSel;
    private Application application;

    public ListaVisitasViewModel(Application application) {
        super(application);
        this.application=application;
        visitaRepository = new VisitaRepositoryImpl(application);
        prodeRepository=new ProductoExhibidoRepositoryImpl(application);
        imdRepository = new ImagenDetRepositoryImpl(application);
        idrepo = new InformeComDetRepositoryImpl(application);
        lcRepository=new ListaCompraDetRepositoryImpl(application);
    }

    public void cargarDetalles(){
        listas =visitaRepository.getSearchResults(indiceSel, nombreTienda,ciudadSel);
        size = Transformations.map(listas, res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
    }

    int i;
    public void eliminarVisita(int id, int banpas){ //banpas indica si se elimina por que es de 1 dia anterior
        //solo puedo eliminar si no tiene informes
        InformeCompraRepositoryImpl infrepo=new InformeCompraRepositoryImpl(application);
        List<InformeCompra> informeCompras=infrepo.getAllByVisitasimple(id);

        //informes.removeObserver(this);
                if(informeCompras.size()>0){
                    if(banpas==1) { //estoy eliminado uno de fecha anterior


                        for (InformeCompra inf : informeCompras) {
                            borrarImagenesxInforme(inf);
                            //borrar el informe
                            infrepo.deleteInformeCompra(inf.getId());
                        }
                    }else
                    //no puedo borrar
                    {     mSnackbarText.setValue("No se puede eliminar");
                    return;}
                }
                   /* Visita im=null;
                    for(i=0;i<listas.getValue().size();i++){
                        im=listas.getValue().get(i);
                        if(im.getId()==id){
                            break;
                        }
                    }
                    Log.d(TAG,"borrando el"+id);*/

                   Visita eliminar= visitaRepository.findsimple(id);

                  if(eliminar!=null) {

                            ImagenDetalle img1=imdRepository.findsimple(eliminar.getFotoFachada());
                           if(img1!=null) {//borro el archivo
                               File fdelete = new File(img1.getRuta());
                               if (fdelete.exists())
                                   fdelete.delete();
                           }
                            //elimino las imagenes
                            imdRepository.deleteById(eliminar.getFotoFachada());
                           List<ProductoExhibido> prods= prodeRepository.getAllByVisitaSimple(eliminar.getId());
                           //elimino prods
                            for (ProductoExhibido prod:prods) {

                                ImagenDetalle img2=imdRepository.findsimple(eliminar.getFotoFachada());
                                if(img2!=null)
                                { //borro el archivo
                                File fdelete2 = new File(img2.getRuta());
                                if (fdelete2.exists())
                                    fdelete2.delete();}
                                imdRepository.deleteById(prod.getImagenId());
                            }
                            prodeRepository.deleteAllByVisita(eliminar.getId());
                          //  eliminar.removeObserver(this);
                            visitaRepository.delete(eliminar);
                            //eliminar producto exhibido
                            mSnackbarText.setValue("Se eliminó correctamente");

                        }

    }
    public void borrarImagenesxInforme(InformeCompra inf){
        ImagenDetalle img1 = imdRepository.findsimple(inf.getTicket_compra());
        if(img1!=null) {//borro el archivo
            File fdelete = new File(img1.getRuta());
            if (fdelete.exists())
                fdelete.delete();
        }
        img1 = imdRepository.findsimple(inf.getCondiciones_traslado());
        //borro el archivo
        if(img1!=null) {
            File fdelete = new File(img1.getRuta());
            if (fdelete.exists())
                fdelete.delete();
        }
        //busco los detalles
        List<InformeCompraDetalle> det=idrepo.getAllSencillo(inf.getId());
        for (InformeCompraDetalle infd : det) {
            List<ImagenDetalle> fotos= imdRepository.getFotosInfDet(infd);
            for(ImagenDetalle img:fotos){
                File fdelete = new File(img.getRuta());
                if (fdelete.exists())
                    fdelete.delete();
            }
            //ajusto cantidades
            ListaCompraDetalle compradet=lcRepository.findsimple(infd.getComprasId(),infd.getId());
            int nvacant=compradet.getCantidad();
            lcRepository.actualizarComprados(infd.getId(),infd.getComprasId(),nvacant);
            //borro los detalles
            idrepo.delete(infd);
        }
    }
    public Visita tieneInforme(Visita visita, LifecycleOwner owner){

        InformeCompraRepositoryImpl infoRepo=new InformeCompraRepositoryImpl(application);
        List<InformeCompra> informeCompras=infoRepo.getAllByVisitasimple(visita.getId());
        if(informeCompras!=null&&informeCompras.size()>0)
                    //ya tiene informe
                    visita.setEstatus(3);


       return visita;

    }


    public LiveData<List<Visita>> getListas() {
        return listas;
    }

    public LiveData<Integer> getSize() {
        return size;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }



    public String getCiudadSel() {
        return ciudadSel;
    }



    public String getIndiceSel() {
        return indiceSel;
    }

    public MutableLiveData<String> getmSnackbarText() {
        return mSnackbarText;
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