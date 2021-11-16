package com.example.comprasmu.ui.informe;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.SubirInformeTask;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.Post;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.modelos.VisitaWithInformes;
import com.example.comprasmu.data.remote.APIService;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.data.remote.PostResponse;
import com.example.comprasmu.data.remote.ServiceGenerator;
import com.example.comprasmu.data.remote.TodoEnvio;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.services.SubirPendService;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostInformeViewModel {
    private ServiceGenerator apiClient;;
    Post post;
    public  String mensaje;
    private String TAG="Ejemplo";
   InformeCompraRepositoryImpl infoRepo;
   ImagenDetRepositoryImpl imagenRepo;
   InformeComDetRepositoryImpl infoDetRepo;
   VisitaRepositoryImpl visitaRepo;
   ProductoExhibidoRepositoryImpl prodeRepo;
   Context context;

    public PostInformeViewModel(@NonNull Context context) {

        this.context=context;


    }


    public  void sendInforme(InformeEnvio informeCompra) {
        //TODO manejar el response
        Log.d("Informe", "kkkkkkkkkk"+informeCompra.toJson(informeCompra));
        apiClient.getApiService().saveInformeEnvio(informeCompra).enqueue(new Callback<InformeEnvio>() {
            @Override
            public void onResponse(Call<InformeEnvio> call, Response<InformeEnvio> response) {
                Log.d("POstInformeVM", "jjjjjjjjj"+response.body());
              //  { "status": "ok",
                //        "data": "Informe dado de alta correctamente."}
                if(response.isSuccessful()) {

                    mensaje=response.body().toString();
                    //actualizo el estatus
                    iniciarConexiones();
                   actualizarEstatusInforme(informeCompra);

                  //listo para subir fotos

                }
            }

            @Override
            public void onFailure(Call<InformeEnvio> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    public  void sendTodo(TodoEnvio informes, SubirPendService.SubirTodoListener listen) {
        //TODO manejar el response
        Log.d("Informe", "kkkkkkkkkk"+informes.toJson(informes));
        apiClient.getApiService().saveInformesPend(informes).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                Log.d("POstInformeVM", "jjjjjjjjj"+response.body());
                //  { "status": "ok",
                //        "data": "Informe dado de alta correctamente."}
                if(response.isSuccessful()) {

                    mensaje=response.body().toString();
                    //actualizo el estatus
                    iniciarConexiones();
                    actualizarEstatusTodo(informes);
                    listen.onSuccess(mensaje);
                    //listo para subir fotos

                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }
    public void iniciarConexiones(){
        infoRepo=new InformeCompraRepositoryImpl(context);
         imagenRepo=new ImagenDetRepositoryImpl(context);
         infoDetRepo=new InformeComDetRepositoryImpl(context);
         visitaRepo=new VisitaRepositoryImpl(context);
         prodeRepo=new ProductoExhibidoRepositoryImpl(context);
    }
    public void actualizarEstatusInforme(InformeEnvio informe){
        //actualizo la visita
        visitaRepo.actualizarEstatusSync(informe.getVisita().getId(), Constantes.ENVIADO);
        //actualizo informe
        infoRepo.actualizarEstatusSync(informe.getInformeCompra().getId(),Constantes.ENVIADO);
        //actualizo detalles
        infoDetRepo.actualizarEstatusSyncxInfo(informe.getInformeCompra().getId(),Constantes.ENVIADO);
        //producto exhibido
        prodeRepo.actualizarEstatusSyncxVisita(informe.getVisita().getId(), Constantes.ENVIADO);


    }
    public void actualizarEstatusTodo(TodoEnvio informes){
        //actualizo la visita
        for(Visita visita:informes.getVisita())
            visitaRepo.actualizarEstatusSync(visita.getId(), Constantes.ENVIADO);
        //actualizo informe
        for(InformeCompra informe:informes.getInformeCompra())
            infoRepo.actualizarEstatusSync(informe.getId(),Constantes.ENVIADO);
        //actualizo detalles
        for(InformeCompraDetalle detalle:informes.getInformeCompraDetalles())
            infoDetRepo.actualizarEstatusSyncxInfo(detalle.getId(),Constantes.ENVIADO);
        //producto exhibido
        for(ProductoExhibido prod:informes.getProductosEx())
            prodeRepo.actualizarEstatusSyncxVisita(prod.getId(), Constantes.ENVIADO);


    }


    public void actualizarEstatusFoto(ImagenDetalle imagen){
           imagenRepo.actualizarEstatusSync(imagen.getId(),Constantes.ENVIADO);
    }

    /**para envio***/
    public TodoEnvio prepararInformes(){

        TodoEnvio envio=new TodoEnvio();
        List<Visita> visitaenv=new ArrayList<>();
        List<InformeCompra> informeCompraenv=new ArrayList<>();
        List<InformeCompraDetalle> informeCompraDetallesenv=new ArrayList<>();
        List<ImagenDetalle> imagenDetallesenv=new ArrayList<>();
        List<ProductoExhibido> productosExenv=new ArrayList<>();
        List<VisitaWithInformes> visitas= visitaRepo.getVisitaWithInformesByIndice(Constantes.INDICEACTUAL);
        for(VisitaWithInformes visitapend:visitas) {
            if (visitapend.visita.getEstatusSync() == 0)
                visitaenv.add(visitapend.visita);
            List<ProductoExhibido> productoExhibidos=prodeRepo.getAllsimple(visitapend.visita.getId());
            for(ProductoExhibido produc:productoExhibidos){
                if(produc.getEstatusSync()==0)
                    productosExenv.add(produc);
            }
            for (InformeCompra informe:visitapend.informes){
                if(informe.getEstatusSync()==0) {
                    informeCompraenv.add(informe);
                    //busco los detalles
                    List<InformeCompraDetalle> detalles = infoDetRepo.getAllSencillo(informe.getId());
                    informeCompraDetallesenv.addAll(detalles);
                 //   envio.setImagenDetalles(buscarImagenes(visita, informe, detalles));
                }
            }
        }
        imagenDetallesenv=imagenRepo.getImagenPendSyncsimple();
       envio.setImagenDetalles(imagenDetallesenv);
       envio.setInformeCompra(informeCompraenv);
       envio.setInformeCompraDetalles(informeCompraDetallesenv);
       envio.setProductosEx(productosExenv);
       envio.setVisita(visitaenv);
       envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
       //TODO talvez sacar el de la visita
       envio.setIndice(Constantes.INDICEACTUAL);
        return envio;
    }


    public String getMensaje() {
        return mensaje;
    }
}
