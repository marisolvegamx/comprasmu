package com.example.comprasmu.ui.informe;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.comprasmu.data.modelos.CorEtiquetadoCaja;
import com.example.comprasmu.data.modelos.CorEtiquetadoCajaDet;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.modelos.VisitaWithInformes;
import com.example.comprasmu.data.remote.CorEtiquetaCajaEnvio;
import com.example.comprasmu.data.remote.CorreccionEnvio;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.data.remote.InformeEtapaEnv;
import com.example.comprasmu.data.remote.PostResponse;
import com.example.comprasmu.data.remote.ServiceGenerator;
import com.example.comprasmu.data.remote.TodoEnvio;
import com.example.comprasmu.data.repositories.CorEtiqCajaDetRepoImpl;
import com.example.comprasmu.data.repositories.CorEtiqCajaRepoImpl;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.data.repositories.DetalleCajaRepoImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.services.SubirPendService;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostInformeViewModel {
    private ServiceGenerator apiClient;

    public  String mensaje;
    private final String TAG="PostInformeViewModel";
   InformeCompraRepositoryImpl infoRepo;
   ImagenDetRepositoryImpl imagenRepo;
   InformeComDetRepositoryImpl infoDetRepo;
   VisitaRepositoryImpl visitaRepo;
   ProductoExhibidoRepositoryImpl prodeRepo;
   InfEtapaRepositoryImpl etapaRepo;
   InfEtapaDetRepoImpl etapadetRepo;
   Context context;
   CorreccionRepoImpl correccionRepo;
   CorEtiqCajaRepoImpl corecRepo;
   CorEtiqCajaDetRepoImpl corecdRepo;

    public PostInformeViewModel(@NonNull Context context) {

        this.context=context;


    }


    public  void sendInforme(InformeEnvio informeCompra) {

       // Log.d("Informe", "kkkkkkkkkk"+informeCompra.toJson(informeCompra));
        ServiceGenerator.getApiService().saveInformeEnvio(informeCompra).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

              //  { "status": "ok",
                //        "data": "Informe dado de alta correctamente."}
                if(response.isSuccessful()&&response.body().getStatus().equals("ok")) {

                    mensaje=response.body().getData();
                    Log.d("POstInformeVM", "jjjjjjjjj"+mensaje);
                    //actualizo el estatus
                    iniciarConexiones();
                   actualizarEstatusInforme(informeCompra);

                  //listo para subir fotos

                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                mensaje="No se pudo subir";
                Log.e(TAG, "Unable to submit post to API.");
            }


        });
    }

    public  void sendTodo(TodoEnvio informes, SubirPendService.SubirTodoListener listen) {

        Log.d("InformePend", "sendtodo "+informes.toJson(informes));
        ServiceGenerator.getApiService().saveInformesPend(informes).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                //  { "status": "ok",
                //        "data": "Informe dado de alta correctamente."}
                if(response.isSuccessful()&&response.body().getStatus().equals("ok")) {

                    mensaje=response.body().getData();
                    Log.d("POstInformeVM", "jjjjjjjjj"+mensaje);
                    //actualizo el estatus
                    iniciarConexiones();
                    actualizarEstatusTodo(informes);
                    listen.onSuccess(mensaje);
                    //listo para subir fotos

                }
                listen.onSuccess(mensaje);
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
    public void iniciarBD(){
       etapaRepo=new InfEtapaRepositoryImpl(context);
       etapadetRepo=new InfEtapaDetRepoImpl(context);
    }
    public void actualizarEstatusInforme(InformeEnvio informe){
        if(informe.getVisita()!=null)
        //actualizo la visita
        {
            visitaRepo.actualizarEstatusSync(informe.getVisita().getId(), Constantes.ENVIADO);
            //producto exhibido
            prodeRepo.actualizarEstatusSyncxVisita(informe.getVisita().getId(), Constantes.ENVIADO);
        }
        //actualizo informe
       // infoRepo.actualizarEstatusSync(informe.getInformeCompra().getId(),Constantes.ENVIADO);
        //actualizo detalles
        infoDetRepo.actualizarEstatusSyncxInfo(informe.getInformeCompra().getId(),Constantes.ENVIADO);
        //imagenes se actualiza en enviar foto
       /* if(informe.getImagenDetalles()!=null)
        for(ImagenDetalle imagen: informe.getImagenDetalles())
        imagenRepo.actualizarEstatusSync(imagen.getId(),Constantes.ENVIADO);*/

    }

    public void actualizarEstatuscoloInf(int idinfo){
       //todo pensar en como actualizar solo el informe que subi
         infoRepo.actualizarEstatusSyncAll(Constantes.ENVIADO);

    }
    public void actualizarEstatusTodo(TodoEnvio informes){
        //actualizo la visita
        if(informes.getVisita()!=null)
        for(Visita visita:informes.getVisita())
            visitaRepo.actualizarEstatusSync(visita.getId(), Constantes.ENVIADO);
        //actualizo informe
        if(informes.getInformeCompra()!=null)
        for(InformeCompra informe:informes.getInformeCompra())
            infoRepo.actualizarEstatusSync(informe.getId(),Constantes.ENVIADO);
        //actualizo detalles
        if(informes.getInformeCompraDetalles()!=null)
        for(InformeCompraDetalle detalle:informes.getInformeCompraDetalles())
            infoDetRepo.actualizarEstatusSyncxInfo(detalle.getId(),Constantes.ENVIADO);
        //producto exhibido
        if(informes.getProductosEx()!=null)
        for(ProductoExhibido prod:informes.getProductosEx())
            prodeRepo.actualizarEstatusSyncxVisita(prod.getId(), Constantes.ENVIADO);


    }


    public void actualizarEstatusFoto(ImagenDetalle imagen){
           imagenRepo.actualizarEstatusSync(imagen.getId(),Constantes.ENVIADO);
    }
    /**para envio de pendientes***/
    public TodoEnvio prepararInformesPen(){

        TodoEnvio envio=new TodoEnvio();
        List<Visita> visitaenv=new ArrayList<>();
        List<InformeCompra> informeCompraenv=new ArrayList<>();
        List<InformeCompraDetalle> informeCompraDetallesenv=new ArrayList<>();
        List<ImagenDetalle> imagenDetallesenv=new ArrayList<>();
        List<ProductoExhibido> productosExenv=new ArrayList<>();
        List<VisitaWithInformes> visitas= visitaRepo.getVisitaWithInformesByIndicePend(Constantes.INDICEACTUAL);
        Calendar calhoy = Calendar.getInstance(); // Obtenga un calendario utilizando la zona horaria y la configuración regional predeterminadas
        calhoy.setTime(new Date());
        calhoy.set(Calendar.HOUR_OF_DAY, -2);
        calhoy.set(Calendar.MINUTE, 0);
        calhoy.set(Calendar.SECOND, 0);
        calhoy.set(Calendar.MILLISECOND, 0);
        for(VisitaWithInformes visitapend:visitas) {
            if (visitapend.visita.getEstatusSync() == 0 && visitapend.visita.getEstatus() == 2)//finalizado
            {
                visitaenv.add(visitapend.visita);
                Log.d(TAG, "buscando prodexh" + visitapend.visita.getId());
                List<ProductoExhibido> productoExhibidos = prodeRepo.getAllsimple(visitapend.visita.getId());

                for (ProductoExhibido produc : productoExhibidos) {
                    if (produc.getEstatusSync() == 0)
                        productosExenv.add(produc);
                }
            }
            for (InformeCompra informe : visitapend.informes) {
                if (informe.getEstatusSync() == 0 && informe.getEstatus() == 2) {//finalizados y sin enviar
                    informeCompraenv.add(informe);
                    //busco los detalles
                    List<InformeCompraDetalle> detalles = infoDetRepo.getAllSencillo(informe.getId());
                    informeCompraDetallesenv.addAll(detalles);
                    //   envio.setImagenDetalles(buscarImagenes(visita, informe, detalles));
                    imagenDetallesenv = imagenRepo.getImagenPendSyncsimple2(calhoy.getTime().getTime());

                }
            }


           }

        if(imagenDetallesenv.size()>0)
            envio.setImagenDetalles(imagenDetallesenv);
        if(informeCompraenv.size()>0)
            envio.setInformeCompra(informeCompraenv);
        if(informeCompraDetallesenv.size()>0)
            envio.setInformeCompraDetalles(informeCompraDetallesenv);
        if(productosExenv.size()>0)
            envio.setProductosEx(productosExenv);
        if(visitaenv.size()>0)
            envio.setVisita(visitaenv);
        envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
        //TODO talvez sacar el de la visita
        envio.setIndice(Constantes.INDICEACTUAL);
        return envio;
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
            if (visitapend.visita.getEstatusSync() == 0&&visitapend.visita.getEstatus()==2)//finalizado
                visitaenv.add(visitapend.visita);
            Log.d(TAG,"buscando prodexh"+visitapend.visita.getId());
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
        if(imagenDetallesenv.size()>0)
       envio.setImagenDetalles(imagenDetallesenv);
        if(informeCompraenv.size()>0)
       envio.setInformeCompra(informeCompraenv);
        if(informeCompraDetallesenv.size()>0)
       envio.setInformeCompraDetalles(informeCompraDetallesenv);
        if(productosExenv.size()>0)
       envio.setProductosEx(productosExenv);
        if(visitaenv.size()>0)
       envio.setVisita(visitaenv);
       envio.setClaveUsuario(Constantes.CLAVEUSUARIO);
       //TODO talvez sacar el de la visita
       envio.setIndice(Constantes.INDICEACTUAL);
        return envio;
    }

    public  void sendInformeEta(InformeEtapaEnv informeEtapa) {
        Log.d("sendInformeEta", informeEtapa.toJson(informeEtapa));
         ServiceGenerator.getApiService().saveInformeEtapa(informeEtapa).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                //  { "status": "ok",
                //        "data": "Informe dado de alta correctamente."}
                if(response.isSuccessful()&&response.body().getStatus().equals("ok")) {

                    mensaje=response.body().getData();
                    Log.d("sendInformeEta", ""+mensaje);
                    //actualizo el estatus
                    iniciarBD();
                    actEstatusInfEtapa(informeEtapa);

                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                mensaje="No se pudo subir";
                Log.e(TAG, "Unable to submit post to API.");
            }


        });
    }

    public void actEstatusInfEtapa(InformeEtapaEnv informe){

        //actualizo informe
        etapaRepo.actualizarEstatusSync(informe.getInformeEtapa().getId(),Constantes.ENVIADO);
        //actualizo detalles
        etapadetRepo.actEstatusSyncxInfo(informe.getInformeEtapa().getId(),Constantes.ENVIADO);
        //actualizo detalle caja
        DetalleCajaRepoImpl dcrepo=new DetalleCajaRepoImpl(context);
        if(informe.getDetalleCaja()!=null&&informe.getDetalleCaja().size()>0){
            dcrepo.actualizarEstSyncxInf(informe.getInformeEtapa().getId(),Constantes.ENVIADO);
        }

    }



    public  void sendCorreccion(CorreccionEnvio correccion) {

        Log.d("Correccion", "enviando correccion"+correccion.toJson(correccion));

        ServiceGenerator.getApiService().saveCorreccion(correccion).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                //  { "status": "ok",
                //        "data": "Informe dado de alta correctamente."}
                if(response.isSuccessful()&&response.body().getStatus().equals("ok")) {

                    mensaje=response.body().getData();
                    Log.d("POstInformeVM", "jjjjjjjjj"+mensaje);
                    //actualizo el estatus
                    iniciarDBCor();
                    if(correccion.getCorrecciones()!=null&&correccion.getCorrecciones().size()>0){
                     //actualizo cada uno
                        for (Correccion corre:correccion.getCorrecciones()
                             ) {
                            actEstatusCorreccion(corre);
                        }
                    }else
                        if(correccion.getCorreccion()!=null)
                    actEstatusCorreccion(correccion.getCorreccion());

                    //listo para subir fotos

                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                mensaje="No se pudo subir";
                Log.e(TAG, "Unable to submit post to API.");
            }


        });
    }

    public  void sendCorreccionEC(CorEtiquetaCajaEnvio correccion) {

        Log.d(TAG+"sendCorreccionEC", "enviando correccion"+correccion.toJson(correccion));

        ServiceGenerator.getApiService().saveCorreccionEC(correccion).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                //  { "status": "ok",
                //        "data": "Informe dado de alta correctamente."}
                if(response.isSuccessful()&&response.body().getStatus().equals("ok")) {

                    mensaje=response.body().getData();
                    Log.d(TAG, "jjjjjjjjj"+mensaje);
                    //actualizo el estatus
                    actEstatusCorreccionEC(correccion.getCorreccion());
                        //actualizo cada uno
                        for (CorEtiquetadoCajaDet corre:correccion.getCorrecciones()
                        ) {
                            actEstatusCorreccionECD(corre);
                        }


                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                mensaje="No se pudo subir";
                Log.e(TAG, "Unable to submit post to API.");
            }


        });
    }

    public  void actInformeEtiq(InformeEtapaEnv informeEtapa) {
        Log.d(TAG+"actInformeEta", informeEtapa.toJson(informeEtapa));
        ServiceGenerator.getApiService().actInformeEtiq(informeEtapa).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                // respuesta serv:
                // { "status": "ok",
                //        "data": "Informe dado de alta correctamente."}
                if(response.isSuccessful()&&response.body().getStatus().equals("ok")) {

                    mensaje=response.body().getData();
                    Log.d(TAG+"actInformeEtiq", ""+mensaje);
                    //actualizo el estatus
                    iniciarBD();
                    actEstatusInfEtapa(informeEtapa);
                    for (InformeEtapaDet det:informeEtapa.getInformeEtapaDet()) {
                        actEstatusNotifEtiq(det.getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                mensaje="No se pudo subir";
                t.printStackTrace();
                Log.e(TAG, "actInformeEtiq Unable to submit post to API.");
            }


        });
    }

    public void iniciarDBCor(){
        correccionRepo=new CorreccionRepoImpl(context);
    }


    public void actEstatusCorreccion(Correccion correccion){

        correccionRepo.actualizarEstatusSync(correccion.getId(),2);
    }
    public void actEstatusCorreccionEC(CorEtiquetadoCaja correccion){
        corecRepo=new CorEtiqCajaRepoImpl(context);
        corecRepo.actualizarEstatusSync(correccion.getId(),2);
    }
    public void actEstatusCorreccionECD(CorEtiquetadoCajaDet correccion){
        corecdRepo=new CorEtiqCajaDetRepoImpl(context);
        correccionRepo.actualizarEstatusSync(correccion.getId(),2);
    }
    public void actEstatusNotifEtiq(int iddet){

        //actualizo detalles
        etapadetRepo.actEstatus(iddet,3); //corregido

    }
    public String getMensaje() {
        return mensaje;
    }
}
