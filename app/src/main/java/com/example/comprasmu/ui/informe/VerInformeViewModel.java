package com.example.comprasmu.ui.informe;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.dao.ProductoExhibidoDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;

import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class VerInformeViewModel extends AndroidViewModel {
    LiveData<InformeWithDetalle> informeCompraSel;
    LiveData<Visita> visita;
    LiveData<List<ImagenDetalle>> imagenDetalles;

    LiveData<List<ProductoExhibidoDao.ProductoExhibidoFoto>> productoExhib;
    InformeCompraRepositoryImpl icrepo;
    InformeComDetRepositoryImpl detrepo;
    VisitaRepositoryImpl visitaRepo;
    String fotoTicket;
    LiveData<ImagenDetalle> fotocondiciones;
    LiveData<ImagenDetalle> fotoFachada;
    Application context;
    private ImagenDetRepositoryImpl imagenDetRepository;
    private ProductoExhibidoRepositoryImpl prodRepo;
    private static String TAG="VerInformeViewModel";



    public VerInformeViewModel(@NonNull Application application) {
        super(application);
        context=application;
        icrepo=new InformeCompraRepositoryImpl(context);
        detrepo=new InformeComDetRepositoryImpl(context);
        imagenDetRepository=new ImagenDetRepositoryImpl(context);


    }

    public void buscarInforme(int idInformeNuevo){
        visitaRepo=new VisitaRepositoryImpl(context);
        prodRepo=new ProductoExhibidoRepositoryImpl(context);
        //busco toda la info
        informeCompraSel=icrepo.getInformeWithDetalleById(idInformeNuevo,0);
        informeCompraSel.observeForever(new Observer<InformeWithDetalle>() {
            @Override
            public void onChanged(InformeWithDetalle informeWithDetalle) {
                visita=visitaRepo.find(informeWithDetalle.informe.getVisitasId());

                getFotoVisita();

                getRutaFotos(informeWithDetalle.informe);
                Log.d(TAG,"ya tengo el informe"+informeWithDetalle.informe.getId());
            }
        });




    }


    public void getRutaFotos(InformeCompra informe){

        fotocondiciones=imagenDetRepository.find(informe.getCondiciones_traslado());
        LiveData<ImagenDetalle> ifotoTicket=imagenDetRepository.find(informe.getTicket_compra());
        ifotoTicket.observeForever(new Observer<ImagenDetalle>() {
            @Override
            public void onChanged(ImagenDetalle imagenDetalle) {
              //  fotoTicket=new MutableLiveData<>();
                fotoTicket=imagenDetalle.getRuta();
                Log.d(TAG,"*ya tengo las fotos informe "+imagenDetalle.getRuta());
            }
        });
        Log.d(TAG,"ya tengo las fotos informe");
       /* imagenDetRepository.find(informe.getFotoFachada())
                .observeForever(new Observer<ImagenDetalle>() {
                                    @Override
                                    public void onChanged(ImagenDetalle imagenDetalle) {
                                        fotoFachada=imagenDetalle.getRuta();
                                    }
                                }
                );*/
    }
    public void getFotoVisita(){

            visita.observeForever(new Observer<Visita>() {
                @Override
                public void onChanged(Visita visita) {

                    fotoFachada=imagenDetRepository.find(visita.getFotoFachada());

                    //fotos prod exhibido
                    productoExhib=prodRepo.getAllByVisita(visita.getId());
                }
            });


    }



    public  void cargarImagenesDet( InformeCompraDetalle detalle){

        List<ImagenDetalle> fotosinfo=new ArrayList<>();
        //las del informe

            List<Integer> fotos=detrepo.getInformesWithImagen(detalle.getId());
           imagenDetalles=imagenDetRepository.findList(fotos);



    }



    public LiveData<List<ImagenDetalle>> getImagenDetalles() {
        return imagenDetalles;
    }

    public void setImagenDetalles(LiveData<List<ImagenDetalle>> imagenDetalles) {
        this.imagenDetalles = imagenDetalles;
    }



    public LiveData<ImagenDetalle> getFotocondiciones() {
        return fotocondiciones;
    }

    public void setFotocondiciones(LiveData<ImagenDetalle> fotocondiciones) {
        this.fotocondiciones = fotocondiciones;
    }

    public LiveData<ImagenDetalle> getFotoFachada() {
        return fotoFachada;
    }

    public void setFotoFachada(LiveData<ImagenDetalle> fotoFachada) {
        this.fotoFachada = fotoFachada;
    }

    public LiveData<InformeWithDetalle> getInformeCompraSel() {
        return informeCompraSel;
    }

    public void setInformeCompraSel(LiveData<InformeWithDetalle> informeCompraSel) {
        this.informeCompraSel = informeCompraSel;
    }

    public String getFotoTicket() {
        return fotoTicket;
    }

    public void setFotoTicket(String fotoTicket) {
        this.fotoTicket = fotoTicket;
    }

    public LiveData<Visita> getVisita() {
        return visita;
    }

    public void setVisita(LiveData<Visita> visita) {
        this.visita = visita;
    }

    public LiveData<List<ProductoExhibidoDao.ProductoExhibidoFoto>> getProductoExhib() {
        return productoExhib;
    }

    public void setProductoExhib(LiveData<List<ProductoExhibidoDao.ProductoExhibidoFoto>> productoExhib) {
        this.productoExhib = productoExhib;
    }
}