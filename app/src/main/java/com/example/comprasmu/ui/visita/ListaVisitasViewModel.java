package com.example.comprasmu.ui.visita;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.dao.ReactivoDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;

import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeTempRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.utils.Constantes;

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
    private final Application application;
   String directorio;
    public ListaVisitasViewModel(Application application) {
        super(application);
        this.application=application;
        visitaRepository = new VisitaRepositoryImpl(application);
        prodeRepository=new ProductoExhibidoRepositoryImpl(application);
        ImagenDetalleDao imagenDetalleDao= ComprasDataBase.getInstance(application).getImagenDetalleDao();
        this.imdRepository= ImagenDetRepositoryImpl.getInstance(imagenDetalleDao);
        idrepo = new InformeComDetRepositoryImpl(application);
        lcRepository=new ListaCompraDetRepositoryImpl(application);
        directorio=application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/";
    }

    public void cargarDetalles(){
        listas =visitaRepository.getSearchResults(indiceSel, nombreTienda,ciudadSel);
        size = Transformations.map(listas, res->{ return listas.getValue().size();});
        empty = Transformations.map(listas, res->{return listas.getValue().isEmpty();});
    }

    public void eliminarVisita(int id, int banpas){ //banpas indica si se elimina por que es de 1 dia anterior
        //solo puedo eliminar si no tiene informes
        InformeCompraRepositoryImpl infrepo=new InformeCompraRepositoryImpl(application);
        List<InformeCompra> informeCompras=infrepo.getAllByVisitasimple(id);

        //informes.removeObserver(this);
        if(informeCompras!=null&&informeCompras.size()>0){ //tengo informes
            if(banpas==1) { //estoy eliminado uno de fecha anterior


                for (InformeCompra inf : informeCompras) {

                    if (inf.getEstatus() == 2&&inf.getEstatusSync()==2) //ya está finalizado
                    {
                        visitaRepository.actualizarEstatus(id, 2);
                        mSnackbarText.setValue("No se puede eliminar, solo puede finalizar el informe por que ya fue enviado");
                        return;
                    }

                }
            }else
                    //no puedo borrar
            {
                //reviso que no esten finalizados
                for (InformeCompra inf : informeCompras) {
                    if (inf.getEstatus() == 2) //ya está finalizado
                    {
                        mSnackbarText.setValue("No se puede eliminar");
                        return;
                    }
                }


            }
            for (InformeCompra inf : informeCompras) {

                infrepo.deleteInformeCompra(inf.getId());
                borrarImagenesxInforme(inf);
            }

        }

        //si llegó hasta aqui no tiene informes y puedo eliminar

        Visita eliminar= visitaRepository.findsimple(id);

        if(eliminar!=null) {

            ImagenDetalle img1=imdRepository.findsimple(eliminar.getFotoFachada());
            if(img1!=null) {//borro el archivo
                File fdelete = new File(directorio+img1.getRuta());
                if (fdelete.exists())
                        fdelete.delete();
            }
                            //elimino las imagenes
            imdRepository.deleteById(eliminar.getFotoFachada());
            List<ProductoExhibido> prods= prodeRepository.getAllByVisitaSimple(eliminar.getId());

                           //elimino prods
                    if(prods!=null&&prods.size()>0)
                        for (ProductoExhibido prod:prods) {

                            ImagenDetalle img2=imdRepository.findsimple(eliminar.getFotoFachada());
                            if(img2!=null)
                            { //borro el archivo
                                 File fdelete2 = new File(directorio+img2.getRuta());
                                if (fdelete2.exists())
                                    fdelete2.delete();}
                                imdRepository.deleteById(prod.getImagenId());
                            }
                            prodeRepository.deleteAllByVisita(eliminar.getId());
                          //  eliminar.removeObserver(this);
                            visitaRepository.delete(eliminar);
                            //eliminar producto exhibido

                   }
           mSnackbarText.setValue("Se eliminó correctamente");
    }
    public void borrarImagenesxInforme(InformeCompra inf){
        ImagenDetalle img1 = imdRepository.findsimple(inf.getTicket_compra());
        if(img1!=null) {//borro el archivo
            File fdelete = new File(directorio+img1.getRuta());
            if (fdelete.exists())
                fdelete.delete();
        }
        img1 = imdRepository.findsimple(inf.getCondiciones_traslado());
        //borro el archivo
        if(img1!=null) {
            File fdelete = new File(directorio+img1.getRuta());
            if (fdelete.exists())
                fdelete.delete();
        }
        //busco los detalles
        List<InformeCompraDetalle> det=idrepo.getAllSencillo(inf.getId());
        if(det!=null)
        for (InformeCompraDetalle infd : det) {
            List<ImagenDetalle> fotos= imdRepository.getFotosInfDet(infd);
            if(fotos!=null)
            for(ImagenDetalle img:fotos){
                if(img!=null) {
                    File fdelete = new File(directorio+img.getRuta());
                    if (fdelete.exists())
                        fdelete.delete();
                }
            }
            Log.d(TAG,"www"+infd.getComprasId()+"--"+infd.getComprasDetId());
            //ajusto cantidades
            //solo si es normal
         //   if(infd.getTipoMuestra()!=3) {
                ListaCompraDetalle compradet = lcRepository.findsimple(infd.getComprasId(), infd.getComprasDetId());
                if (compradet != null && compradet.getComprados() > 0) {
                    int nvacant = compradet.getComprados() - 1;
                    lcRepository.actualizarComprados(infd.getComprasDetId(), infd.getComprasId(), nvacant);
                    //quito el codigo

                }
            //quito en nuevo codigo
            String codigo= Constantes.sdfcaducidad.format(infd.getCaducidad());
            if (compradet.getNvoCodigo()!=null&&compradet.getNvoCodigo() != "") {
                Log.i(TAG,"borrarImagenesxInforme quitando el codigo"+compradet.getNvoCodigo());
                String nuevoscods = compradet.getNvoCodigo().replace(codigo + ";", "");//elimino elcodigo
              //  nuevoscods = compradet.getNvoCodigo().replace(codigo, "");//elimino elcodigo

                //Log.d(TAG,compradet.getId()+"--"+compradet.getListaId()+"--"+nuevoscods);
                lcRepository.actualizarNvosCodigos(compradet.getId(), compradet.getListaId(), nuevoscods);
            }
           // }
            //borro los detalles
            idrepo.delete(infd);
        }
    }
    public Visita tieneInforme(Visita visita){

        InformeCompraRepositoryImpl infoRepo=new InformeCompraRepositoryImpl(application);
        List<InformeCompra> informeCompras=infoRepo.getAllByVisitasimple(visita.getId());
        if(informeCompras!=null&&informeCompras.size()>0)
                    //ya tiene informe
                    visita.setEstatus(3);


       return visita;

    }

    public List<ProductoExhibido> buscarProdExhiPend(int visitaid){
        List<ProductoExhibido> prods= prodeRepository.getAllByVisitaSimple(visitaid);

       return prods;

    }
    public List<InformeCompra> tieneInformePend(int idvisita){

        InformeCompraRepositoryImpl infoRepo=new InformeCompraRepositoryImpl(application);
        return infoRepo.getByVisPend(idvisita,0);


    }
    public boolean hayInfDetalleTemp(){
        //reivos si hay respuestas temporales si no devuelvo null
        InformeTempRepositoryImpl itemprepo=new InformeTempRepositoryImpl(application);
        List<InformeTemp> temps=itemprepo.getAllByTabla("ID");
        return temps != null && temps.size() > 0;
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