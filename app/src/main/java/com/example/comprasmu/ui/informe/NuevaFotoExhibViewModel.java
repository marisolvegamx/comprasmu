package com.example.comprasmu.ui.informe;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.comprasmu.R;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.ProductoExhibidoDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.Event;

import java.util.Date;
import java.util.List;

public class NuevaFotoExhibViewModel extends AndroidViewModel {

    private final ProductoExhibidoRepositoryImpl repository;

    private ImagenDetRepositoryImpl imagenDetRepository;
    private final MutableLiveData<Event<Integer>> mSnackbarText = new MutableLiveData<>();
    private int prefimagen;


    public NuevaFotoExhibViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ProductoExhibidoRepositoryImpl(application);
        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
    }

    public void guardarFoto(String rutaFoto, int idcliente,String cliente,int visitasId,Activity actividad,NuevoinformeViewModel ninfvm) {
        //guardo la foto
        try {
            //busco el ultimo id
            int nvoidimagem=(int)imagenDetRepository.getUltimo();
         //   if(nvoidimagem==0) {

             /*   PeticionesServidor ps = new PeticionesServidor(Constantes.CLAVEUSUARIO);
                NuevoinformeViewModel.EnvioListener listener = ninfvm.crearEnvioListener(actividad);
                ps.getUltimaVisita(Constantes.INDICEACTUAL,  listener);
                recuperarIds(actividad);
                nvoidimagem=prefimagen;
                if(prefimagen==0){
                    nvoidimagem=nvoidimagem+1;
                }*/

          //  }else
            nvoidimagem=nvoidimagem+1;

            ImagenDetalle foto = new ImagenDetalle();
            foto.setId(nvoidimagem);
            foto.setRuta(rutaFoto);
            foto.setEstatusSync(0);
            foto.setEstatus(0);
            foto.setDescripcion("foto producto exhibido");
            foto.setIndice(Constantes.INDICEACTUAL);
            foto.setCreatedAt(new Date());
            int idfoto = (int) imagenDetRepository.insert(foto);
            Log.e("NuevaFotoExhibViewModel",idfoto+"--"+idcliente);
            ProductoExhibido prod1 = new ProductoExhibido();
            prod1.setClienteId(idcliente);
            prod1.setVisitasId(visitasId);
            prod1.setEstatusSync(0);
            prod1.setImagenId(idfoto);
            prod1.setNombreCliente(cliente);
            repository.insert(prod1);
        }catch (Exception ex){
            Log.e("NuevaFotoExhibViewModel",ex.getMessage());
            mSnackbarText.setValue(new Event<>(R.string.error_imagen));
        }


    }
    public void guardarsinFoto(int idcliente,String cliente,int visitasId) {
        //guardo la foto
        try {
            //busco el ultimo id
            int nvoidimagem=(int)imagenDetRepository.getUltimo();

            nvoidimagem=nvoidimagem+1;


            ProductoExhibido prod1 = new ProductoExhibido();
            prod1.setClienteId(idcliente);
            prod1.setVisitasId(visitasId);
            prod1.setEstatusSync(0);
            prod1.setImagenId(0); //no se pudo tomar
            prod1.setNombreCliente(cliente);
            repository.insert(prod1);
        }catch (Exception ex){
            Log.e("NuevaFotoExhibViewModel",ex.getMessage());
            mSnackbarText.setValue(new Event<>(R.string.error_imagen));
        }


    }
    public void recuperarIds(Activity actividad) {

        SharedPreferences prefe=actividad.getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);

        prefimagen=prefe.getInt("ultimagen", 0);


    }
    public void eliminarFoto(ProductoExhibidoDao.ProductoExhibidoFoto productoExhibido){
        //la busco en imagen detalle
     //   repository.find(idfoto).observeForever(new Observer<ProductoExhibido>() {
        //    @Override
       //     public void onChanged(ProductoExhibido productoExhibido) {
        Log.d("NuevaFotoVM","sigo borrando"+productoExhibido.imagenId);
        ImagenDetalle imagenDetalle=null;
                if(productoExhibido!=null)
                     imagenDetalle=imagenDetRepository.findsimple(productoExhibido.imagenId);
                if(imagenDetalle!=null)
                imagenDetRepository.delete(imagenDetalle);


                //la elimino de foto exhibicion
                repository.deleteById(productoExhibido.idprodex);

                return;



    }
    public LiveData<List<ProductoExhibidoDao.ProductoExhibidoFoto>> cargarfotos(int idvisita){
        //la busco en imagen detalle
        return repository.getAllByVisita(idvisita);
    }
    public List<ProductoExhibidoDao.ProductoExhibidoFoto> cargarfotosSimpl(int idvisita){
        //la busco en imagen detalle
        return repository.getAllFotoByVisitaSimpl(idvisita);
    }
}