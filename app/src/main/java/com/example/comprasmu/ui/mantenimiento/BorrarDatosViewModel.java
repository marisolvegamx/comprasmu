package com.example.comprasmu.ui.mantenimiento;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.modelos.VisitaWithInformes;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class BorrarDatosViewModel extends ViewModel {

    InformeComDetRepositoryImpl icdrepo;
    InformeCompraRepositoryImpl icrepo;
    ImagenDetRepositoryImpl imrepo;
    ListaCompraRepositoryImpl lcrepo;
    ListaCompraDetRepositoryImpl lcdrepo;
    ProductoExhibidoRepositoryImpl prorepo;
    VisitaRepositoryImpl visitarepo;

    Context context;
    File carpeta;

    public void setCarpeta(File carpeta) {
        this.carpeta = carpeta;
    }

    public void borrarInformes(String indice){
        visitarepo=new VisitaRepositoryImpl(context);
        icrepo=new InformeCompraRepositoryImpl(context);
        icdrepo=new InformeComDetRepositoryImpl(context);
        imrepo=new ImagenDetRepositoryImpl(context);
        List<VisitaWithInformes> listavisitas= visitarepo.getVisitaWithInformesByIndice(indice);

        for(VisitaWithInformes visita: listavisitas){
            if(visita.informes!=null&&visita.informes.size()>0){

            for(InformeCompra informe: visita.informes){

                //las del los detalles
                List<InformeCompraDetalle> detalles=  icdrepo.getAllSencillo(informe.getId());
                for(InformeCompraDetalle detalle:detalles) {
                    List<Integer> fotos=icdrepo.getInformesWithImagen(detalle.getId());
                    List<ImagenDetalle> imagenes=imrepo.findListsencillo(fotos);
                    for(ImagenDetalle imagen:imagenes){
                        //borro el archivo
                        eliminarPorNombre(carpeta,imagen.getRuta());
                    }
                    imrepo.deleteList(fotos);
                }
                //borro los detalle
                    icdrepo.deleteByInforme(informe.getId());
                //busco imagenes del informe
                borrarImagenes(visita.visita,informe);
                    icrepo.deleteInformeCompra(informe.getId());
                    } //borre el informe
                }
            //reviso las fotos de producto
           LiveData<List<ImagenDetalle>> fotos= prorepo.getImagenByVisita(visita.visita.getId());
            fotos.observeForever(new Observer<List<ImagenDetalle>>() {
                @Override
                public void onChanged(List<ImagenDetalle> imagenDetalles) {
                    for(ImagenDetalle foto:imagenDetalles)
                        imrepo.delete(foto);
                    fotos.removeObserver(this);
                }
            });
            //borro las otras
            prorepo.deleteByVisita(visita.visita.getId());
            //borro la visita
            visitarepo.delete(visita.visita);
            }


    }



    private void puedoBorrarInforme(InformeCompra informe){

        icdrepo.getAll(informe.getId()).observeForever(new Observer<List<InformeCompraDetalle>>() {
               @Override
               public void onChanged(List<InformeCompraDetalle> informeCompraDetalles) {
                   if(informeCompraDetalles==null||informeCompraDetalles.size()==0){
                    //TODO hay que cambiar el proceso
                       //reviso la imagenes
                     /*  imrepo.getAllByInforme(informe.getId()).observeForever(new Observer<List<ImagenDetalle>>() {
                                  @Override
                                  public void onChanged(List<ImagenDetalle> imagenDetalles) {
                                      if(informeCompraDetalles==null||informeCompraDetalles.size()==0){
                                            //puedo borrar
                                         icrepo.deleteInformeCompra(informe.getId());

                                      }

                                  }
                              }
                       );*/
                   }

               }
           }
        );

    }

    private void puedoBorrarLista(ListaCompra compra){

        List<ListaCompraDetalle> compraDetalles= lcdrepo.getAllByListasimple(compra.getId());

            if(compraDetalles==null||compraDetalles.size()==0){
                           //puedo borrar
                           lcrepo.delete(compra);

                       }
                       else
                           //borro los detalles
            {
                lcdrepo.deleteByLista(compra.getId());
                puedoBorrarLista(compra);

                    }

    }


    public void borrarListasCompra(String indice){

        lcrepo=new ListaCompraRepositoryImpl();
        lcdrepo=new ListaCompraDetRepositoryImpl(context);
        List<ListaCompra> listaCompras= lcrepo.getAllByIndicesimple(indice);

                //para cada detalle lo elimino
                if(listaCompras!=null&&listaCompras.size()>0){
                    for(ListaCompra compra:listaCompras){

                        puedoBorrarLista(compra);
                    }
                }

            }
    public void borrarImagenes(Visita visita, InformeCompra informe){
        //todas las fotos aqui
        List<Integer> fotosinfo=new ArrayList<>();
        //la visita
        imrepo.deleteById(visita.getFotoFachada());

        //las del informe
        List<Integer> arrFotos=new ArrayList<>();
        imrepo.deleteById(informe.getCondiciones_traslado());
        imrepo.deleteById(informe.getTicket_compra());



    }

    public  void eliminarPorNombre(File dir,String path){
        File fdelete = new File( dir, path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + path); }
            else { Log.e("BorrarDatosFregment","No se pudo borrar el archivo "+path); } }
    }

}