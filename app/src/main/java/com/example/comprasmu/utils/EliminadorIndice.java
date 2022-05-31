package com.example.comprasmu.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.modelos.VisitaWithInformes;
import com.example.comprasmu.data.remote.TodoEnvio;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.services.SubirPendService;
import com.example.comprasmu.ui.informe.PostInformeViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.net.HttpCookie;
import java.util.List;

public class EliminadorIndice {
    private final VisitaRepositoryImpl visitaRepository;
    private final ListaCompraDetRepositoryImpl lcRepository;

    private final ImagenDetRepositoryImpl imdRepository;
    private final InformeComDetRepositoryImpl idrepo;
    private final ProductoExhibidoRepositoryImpl prodeRepository;
    private Snackbar mSnackbarText;
    InformeCompraRepositoryImpl infrepo;
    String indice;
    Activity actividad;
    Context application;

    public EliminadorIndice(Context application , String indice) {
        this.visitaRepository = new VisitaRepositoryImpl(application);
        prodeRepository=new ProductoExhibidoRepositoryImpl(application);
        imdRepository = new ImagenDetRepositoryImpl(application);
        idrepo = new InformeComDetRepositoryImpl(application);
        lcRepository=new ListaCompraDetRepositoryImpl(application);
        infrepo=new InformeCompraRepositoryImpl(application);
        this.indice=indice;
        this.application=application;
    }
    public void eliminarVisitas(){ //banpas indica si se elimina por que es de 1 dia anterior
        //solo puedo eliminar si no tiene informes

        //busco que no haya sin subir
       List<Visita> pends= visitaRepository.getVisitaPendSubir(indice);
       if(pends!=null&pends.size()>0){
           //subo los pendientes
           enviarReporte();
           //no puedo seguir borrando aviso para que lo hagan de nuevo
           return;
       }
       //busco que no haya informes o informes detalle o imagenes o productos ex
        List<InformeCompra> pendsc= infrepo.getInformesPendSubir(indice);
        if(pendsc!=null&pendsc.size()>0){
            //subo los pendientes
            enviarReporte();
            //no puedo seguir borrando aviso para que lo hagan de nuevo
            return;
        }
        List<InformeCompraDetalle> pendsd= idrepo.getDetallePendSubir(indice);
        if(pendsd!=null&pendsd.size()>0){
            //subo los pendientes
            enviarReporte();
            //no puedo seguir borrando aviso para que lo hagan de nuevo
            return;
        }
        List<ProductoExhibido> pendsp= prodeRepository.getProdPendSubir(indice);
        if(pendsp!=null&pendsp.size()>0){
            //subo los pendientes
            enviarReporte();
            //no puedo seguir borrando aviso para que lo hagan de nuevo
            return;
        }
        List<ImagenDetalle> pendsi= imdRepository.getImagenPendSyncsimple();
        if(pendsi!=null&pendsi.size()>0){
            //subo los pendientes
            enviarReporte();
            //no puedo seguir borrando aviso para que lo hagan de nuevo
            return;
        }

        //antes de borrar la imagenes borro los archivos
        pendsi=imdRepository.getByIndice(indice);
        if(pendsi!=null&&pendsi.size()>0)
            for ( ImagenDetalle img2:pendsi) {
                    File fdelete2 = new File(img2.getRuta());
                    if (fdelete2.exists())
                        fdelete2.delete();}

        List<VisitaWithInformes> pend = visitaRepository.getVisitaWithInformesByIndice(indice);
        if(pend!=null&pend.size()>0) {

            for (VisitaWithInformes vis : pend) {

                for (InformeCompra inf : vis.informes) {
                    borrarImagenesxInforme(inf);
                    //borro los detalles

                }
                //borro el informe
                infrepo.deleteInformeCompraByVisita(vis.visita.getId());
                //borro los prod exhibidos
                prodeRepository.deleteAllByVisita(vis.visita.getId());
            }
        }
            //borro las visitas
            visitaRepository.deleteVisitaByIndice(indice);

            //eliminar producto exhibido
          //  mSnackbarText.setValue("Se eliminó correctamente");


    }
    public void borrarImagenesxInforme(InformeCompra inf){

        //busco los detalles
        List<InformeCompraDetalle> det=idrepo.getAllSencillo(inf.getId());
        if(det!=null)
            for (InformeCompraDetalle infd : det) {
                //borro los detalles
                idrepo.delete(infd);
            }
    }
    public void enviarReporte() {
        //reviso si tengo conexion
        if(ComprasUtils.isOnlineNet()) {
            //busco los pendientes

            PostInformeViewModel postviewModel = new PostInformeViewModel(application);
            postviewModel.iniciarConexiones();
            //hago el post
            TodoEnvio envio= postviewModel.prepararInformes();
            if((envio.getProductosEx()!=null&&envio.getProductosEx().size()>0)||(envio.getInformeCompraDetalles()!=null&&envio.getInformeCompraDetalles().size()>0)
                    ||(envio.getInformeCompra()!=null&&envio.getInformeCompra().size()>0)
                    ||(envio.getVisita()!=null&&envio.getVisita().size()>0)
                    ||(envio.getImagenDetalles()!=null&&envio.getImagenDetalles().size()>0))
              //  postviewModel.sendTodo(envio,listener);
                Log.d("hola","eliminando");

        }
    }
}
