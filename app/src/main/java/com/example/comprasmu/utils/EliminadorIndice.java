package com.example.comprasmu.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.modelos.VisitaWithInformes;
import com.example.comprasmu.data.remote.TodoEnvio;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.data.repositories.DetalleCajaRepoImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.SolicitudCorRepoImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.services.SubirPendService;
import com.example.comprasmu.ui.informe.PostInformeViewModel;
import com.example.comprasmu.ui.mantenimiento.BorrarActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.net.HttpCookie;
import java.util.List;
import java.util.Observable;

public class EliminadorIndice {
    private final VisitaRepositoryImpl visitaRepository;
    private final ListaCompraDetRepositoryImpl lcRepository;

    private final ImagenDetRepositoryImpl imdRepository;
    private final InformeComDetRepositoryImpl idrepo;
    private final ProductoExhibidoRepositoryImpl prodeRepository;
    private final CorreccionRepoImpl corRepo;
    private final SolicitudCorRepoImpl solRepo;
    private final InfEtapaRepositoryImpl ineRepo;
    private final InfEtapaDetRepoImpl inedRepo;
    private final DetalleCajaRepoImpl cajaRepo;
    ListaCompraRepositoryImpl lcrepo;

    private Snackbar mSnackbarText;
    InformeCompraRepositoryImpl infrepo;

    String indice;
    Activity actividad;
    Context application;
    ComprasLog complog;
    public EliminadorIndice(Context application , String indice) {
        this.visitaRepository = new VisitaRepositoryImpl(application);
        prodeRepository=new ProductoExhibidoRepositoryImpl(application);
        imdRepository = new ImagenDetRepositoryImpl(application);
        idrepo = new InformeComDetRepositoryImpl(application);
        lcRepository=new ListaCompraDetRepositoryImpl(application);
        infrepo=new InformeCompraRepositoryImpl(application);
        this.indice=indice;
        this.application=application;
        corRepo = new CorreccionRepoImpl(application);
        solRepo=new SolicitudCorRepoImpl(application);
        complog=ComprasLog.getSingleton();
        ineRepo=new InfEtapaRepositoryImpl(application);
        inedRepo=new InfEtapaDetRepoImpl(application);
        cajaRepo=new DetalleCajaRepoImpl(application);
    }
    public void eliminarVisitas(){ //banpas indica si se elimina por que es de 1 dia anterior
        //solo puedo eliminar si no tiene informes

        //busco que no haya sin subir
       List<Visita> pends= visitaRepository.getVisitaPendSubir(indice);
       if(pends!=null&pends.size()>0){
           //subo los pendientes
           enviarReporte();
           //no puedo seguir borrando aviso para que lo hagan de nuevo
        //   return;
       }
       //busco que no haya informes o informes detalle o imagenes o productos ex
        List<InformeCompra> pendsc= infrepo.getInformesPendSubir(indice);
        if(pendsc!=null&pendsc.size()>0){
            //subo los pendientes
            enviarReporte();
            //no puedo seguir borrando aviso para que lo hagan de nuevo
        //    return;
        }
        List<InformeCompraDetalle> pendsd= idrepo.getDetallePendSubir(indice);
        if(pendsd!=null&pendsd.size()>0){
            //subo los pendientes
            enviarReporte();
            //no puedo seguir borrando aviso para que lo hagan de nuevo
        //    return;
        }
        List<ProductoExhibido> pendsp= prodeRepository.getProdPendSubir(indice);
        if(pendsp!=null&pendsp.size()>0){
            //subo los pendientes
            enviarReporte();
            //no puedo seguir borrando aviso para que lo hagan de nuevo
         //   return;
        }
        List<ImagenDetalle> pendsi= imdRepository.getImagenPendSyncsimple();
        if(pendsi!=null&pendsi.size()>0){
            //subo los pendientes
            enviarReporte();
            //no puedo seguir borrando aviso para que lo hagan de nuevo
         //   return;
        }

        //antes de borrar la imagenes borro los archivos
        pendsi=imdRepository.getByIndice(indice);
        if(pendsi!=null&&pendsi.size()>0)
            for ( ImagenDetalle img2:pendsi) {
                complog.grabarError("eliminando archivo "+Environment.DIRECTORY_PICTURES+img2.getRuta());
                File dir=application.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                  if(dir!=null) {
                      try {
                          File fdelete2 = new File(dir, img2.getRuta());
                          if (fdelete2.exists()) {
                              boolean resp = fdelete2.delete();
                              complog.grabarError("*eliminando archivo " + Environment.DIRECTORY_PICTURES + img2.getRuta() + "--" + resp);
                              //la tabla la borra en borrar activity
                          }
                      }catch(NullPointerException ex){
                          complog.grabarError("EliminadorIndice","eliminarVisitas", "error al borrar el archivo");
                      }
                  }
            }

        List<Visita> pend = visitaRepository.getVisitasxIndice(indice);
        if(pend!=null&pend.size()>0) {
            borrarDetallesCompra();
            for (Visita vis : pend) {

                //borro el informe
                infrepo.deleteInformeCompraByVisita(vis.getId());
                //borro los prod exhibidos
                prodeRepository.deleteAllByVisita(vis.getId());
            }
        }
            //borro las visitas
            visitaRepository.deleteVisitaByIndice(indice);

    }
    //borra los detalles de compra ¡¡toda la tabla!!
    public void borrarDetallesCompra(){

        idrepo.deleteAll();

    }
    public void enviarReporte() {
        //reviso si tengo conexion
        if(ComprasUtils.isOnlineNet(application)) {
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

    public void eliminarVisitasxIndice(){ //banpas indica si se elimina por que es de 1 dia anterior
        //solo puedo eliminar si no tiene informes


        //antes de borrar la imagenes borro los archivos
        List<ImagenDetalle> pendsi=imdRepository.getByIndice(indice);
        if(pendsi!=null&&pendsi.size()>0)
            for ( ImagenDetalle img2:pendsi) {
                complog.grabarError("eliminando archivo "+Environment.DIRECTORY_PICTURES+img2.getRuta());
                File fdelete2 = new File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES),img2.getRuta());
                if (fdelete2.exists())
                {
                    boolean resp= fdelete2.delete();
                    complog.grabarError("*eliminando archivo "+Environment.DIRECTORY_PICTURES+img2.getRuta()+"--"+resp);

                }
                imdRepository.delete(img2);
            }

        List<VisitaWithInformes> pend = visitaRepository.getVisitaWithInformesByIndice2(indice);
        if(pend!=null&pend.size()>0) {

            for (VisitaWithInformes vis : pend) {

                for (InformeCompra inf : vis.informes) {
                    borrarInfcompxInforme(inf);
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
    //borra los detalles de compra
    public void borrarInfcompxInforme(InformeCompra inf){

        //busco los detalles
        List<InformeCompraDetalle> det=idrepo.getAllSencillo(inf.getId());
        if(det!=null)
            for (InformeCompraDetalle infd : det) {
                //borro los detalles
                idrepo.delete(infd);
            }
    }
    public void eliminarCorrecciones(){
        corRepo.deleteByIndice(indice);
    }
    public void eliminarSolicitudes(){
        solRepo.deleteByIndice(indice);
    }
    public void eliminarTablaVers(){
        TablaVersionesRepImpl tvrepo=new TablaVersionesRepImpl(this.application);
        tvrepo.deleteByIndice();
    }
    public void borrarImagenes(){

        //busco los detalles
       imdRepository.deleteByIndice(indice);
    }

    public void revisionFinal(String indice, LifecycleOwner lo){
        LiveData<List<Correccion>> cors= corRepo.getByIndice(indice);
        cors.observe(lo, new Observer<List<Correccion>>() {
            @Override
            public void onChanged(List<Correccion> correccions) {
                ListaCompraDao dao = ComprasDataBase.getInstance(application).getListaCompraDao();
                lcrepo = ListaCompraRepositoryImpl.getInstance(dao);

                List<ListaCompra> listaCompras= lcrepo.getAllByIndicesimple(indice);
                if(listaCompras!=null&&listaCompras.size()>0){
                    mostrarResultados();
                    return; //todavia hay datos
                }
                if(correccions!=null&&correccions.size()>0) {
                    mostrarResultados();
                    return; //todavia hay datos
                }
                List <ImagenDetalle> imagenes=imdRepository.getByIndice(indice);
                if(imagenes!=null&&imagenes.size()>0){
                    mostrarResultados();
                    return; //todavia hay datos
                }

                List<InformeEtapa> infes=ineRepo.findAllByIndice(indice);
                if(infes!=null&&infes.size()>0){
                    mostrarResultados();
                    return; //todavia hay datos
                }

                LiveData<List<Visita>> vis=visitaRepository.getInformesByIndice(indice);
                vis.observe(lo, new Observer<List<Visita>>() {
                            @Override
                            public void onChanged(List<Visita> visitas) {
                                if (visitas != null & visitas.size() > 0) {
                                    mostrarResultados();
                                    return; //todavia hay datos
                                }
                                LiveData<List<SolicitudCor>> sols=solRepo.getSolicitudesByIndice(indice);
                                sols.observe(lo, new Observer<List<SolicitudCor>>() {
                                    @Override
                                    public void onChanged(List<SolicitudCor> solicitudCors) {
                                        if (solicitudCors != null && solicitudCors.size() > 0) {
                                            mostrarResultados();
                                            return; //todavia hay datos

                                        }
                                        //inf compra
                                        LiveData<List<InformeCompra>> compras=infrepo.getAllInformeCompra();
                                        compras.observe(lo, new Observer<List<InformeCompra>>() {
                                          @Override
                                          public void onChanged(List<InformeCompra> informeCompras) {


                                            if (informeCompras != null && informeCompras.size() > 0) {
                                                mostrarResultados();
                                                return; //todavia hay datos

                                            }
                                              LiveData<List<InformeCompraDetalle>> dets=idrepo.getAll();
                                              dets.observe(lo, new Observer<List<InformeCompraDetalle>>() {
                                                @Override
                                                public void onChanged(List<InformeCompraDetalle> informeCompraDetalles) {

                                                    if (informeCompraDetalles != null && informeCompraDetalles.size() > 0) {
                                                        mostrarResultados();
                                                        return; //todavia hay datos

                                                    }
                                                    //detalle caja
                                                    List<DetalleCaja> cajas=cajaRepo.getAllsimple();
                                                    if (cajas != null && cajas.size() > 0) {
                                                        mostrarResultados();
                                                        return; //todavia hay datos

                                                    }
                                                    //detalle etapa
                                                    List<InformeEtapaDet> etadet=inedRepo.getAllsimple();
                                                    if (etadet != null && etadet.size() > 0) {
                                                        mostrarResultados();
                                                        return; //todavia hay datos

                                                    }
                                                    //detalle listacompra
                                                    List<ListaCompraDetalle> detalle2=lcRepository.getAllSimpl();
                                                    if (detalle2 != null && detalle2.size() > 0) {
                                                        mostrarResultados();
                                                        return; //todavia hay datos

                                                    }
                                                    //prodex
                                                    List<ProductoExhibido> prode=prodeRepository.getAllsimple();
                                                    if (prode != null && prode.size() > 0) {
                                                        mostrarResultados();
                                                        return; //todavia hay datos

                                                    }
                                                }
                                              });
                                                dets.removeObservers(lo);

                                          }
                                      });
                                        compras.removeObservers(lo);

                                    }
                                    });
                                sols.removeObservers(lo);
                             }

                });
                vis.removeObservers(lo);



            }
        });
        cors.removeObservers(lo);

    }

    public void mostrarResultados(){
        //no se borró bien

    }
}
