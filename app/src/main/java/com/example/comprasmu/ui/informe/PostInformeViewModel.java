package com.example.comprasmu.ui.informe;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.Post;
import com.example.comprasmu.data.remote.APIService;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.data.remote.ServiceGenerator;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.services.SubirFotoService;
import com.example.comprasmu.utils.Constantes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostInformeViewModel {
    private ServiceGenerator apiClient;;
    Post post;
    public final ObservableField<String> mensaje = new ObservableField<>();
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


    public void sendInforme(InformeEnvio informeCompra) {
        //TODO manejar el response
        Log.d("Informe", "kkkkkkkkkk"+informeCompra.toJson(informeCompra));
        apiClient.getApiService().saveInformeEnvio(informeCompra).enqueue(new Callback<InformeEnvio>() {
            @Override
            public void onResponse(Call<InformeEnvio> call, Response<InformeEnvio> response) {
                Log.d("POstInformeVM", "jjjjjjjjj"+response.body());
              //  { "status": "ok",
                //        "data": "Informe dado de alta correctamente."}
                if(response.isSuccessful()) {

                    mensaje.set(response.body().toString());
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

    public void actualizarEstatusFoto(ImagenDetalle imagen){
           imagenRepo.actualizarEstatusSync(imagen.getId(),Constantes.ENVIADO);
    }

    public ObservableField<String> getMensaje() {
        return mensaje;
    }
}
