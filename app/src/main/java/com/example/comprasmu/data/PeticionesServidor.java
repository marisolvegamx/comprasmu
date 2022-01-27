package com.example.comprasmu.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.comprasmu.DescargasIniAsyncTask;
import com.example.comprasmu.data.modelos.Atributo;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.InformeCancelar;
import com.example.comprasmu.data.modelos.LoggedInUser;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.data.remote.CatalogosResponse;
import com.example.comprasmu.data.remote.GenericResponse;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.remote.PostResponse;
import com.example.comprasmu.data.remote.ServiceGenerator;
import com.example.comprasmu.data.remote.TiendasResponse;
import com.example.comprasmu.data.remote.UltimoInfResponse;
import com.example.comprasmu.data.remote.UltimosIdsResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;

import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.ui.login.LoginActivity;
import com.example.comprasmu.utils.Constantes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeticionesServidor {

    ServiceGenerator apiClient;
    String usuario;
    static final String TABLA_LISTA="pr_listacompra";
    static final String TAG="PeticionesServidor";
    static final String TABLA_DETALLE="pr_listacompradetalle";
    MutableLiveData<List<Tienda>> lista;
    public PeticionesServidor(String usuario ) {
        this.usuario = usuario;
        lista=new MutableLiveData<>();
    }

    public void getCatalogos(CatalogoDetalleRepositoryImpl catRep,TablaVersionesRepImpl trepo,AtributoRepositoryImpl atRepo) {

        final Call<CatalogosResponse> batch = apiClient.getApiService().getCatalogosNuevoInforme(usuario);

        batch.enqueue(new Callback<CatalogosResponse>() {
            @Override
            public void onResponse(@Nullable Call<CatalogosResponse> call, @Nullable Response<CatalogosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CatalogosResponse respuestaCats = response.body();
                    Log.d("PeticionesServidor","leyendo cats "+respuestaCats.getCatalogos().size());
                    insertarCatalogos(respuestaCats,catRep,trepo,atRepo);

                }else
                    Log.e("PeticionesServidor", "algo salio mal en peticio catalogo");

            }

            @Override
            public void onFailure(@Nullable Call<CatalogosResponse> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e("PeticionesServidor", "algo salio mal en peticio catalogo"+t.getMessage());

                }
            }
        });
    }

    public  void getTiendas(String ciudad, String tipo, String nombre ) {

        final Call<TiendasResponse> batch = apiClient.getApiService().getTiendas(ciudad,tipo,nombre,usuario);

        batch.enqueue(new Callback<TiendasResponse>() {
            @Override
            public void onResponse(@Nullable Call<TiendasResponse> call, @Nullable Response<TiendasResponse> response) {
               Log.d(TAG,"llego algo"+response.body());
                if (response.isSuccessful() && response.body() != null) {
                    TiendasResponse respuestaTiendas = response.body();

                      lista.setValue(respuestaTiendas.getTiendas());
                   // lista.setValue(respuestaTiendas);

                    Log.d(TAG,"ya lo asigné"+respuestaTiendas.getTiendas().get(0).getUne_descripcion());
                //  return lista;


                }
            }

            @Override
            public void onFailure(@Nullable Call<TiendasResponse> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e(Constantes.TAG, t.getMessage());

                }
            }
        });

    }
    MutableLiveData<Boolean> resp;
    public  void getUltimoInforme(String indice,int plantaSel, NuevoinformeViewModel.EnvioListener listener ) {
        resp=new  MutableLiveData<Boolean>();
        Log.d(TAG,"haciendo peticion"+plantaSel);
        final Call<UltimoInfResponse> batch = apiClient.getApiService().getUltimosIdsI(indice,plantaSel,usuario);

        batch.enqueue(new Callback<UltimoInfResponse>() {
            @Override
            public void onResponse(@Nullable Call<UltimoInfResponse> call, @Nullable Response<UltimoInfResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    UltimoInfResponse respuesta = response.body();
                    Log.d(TAG,"ultimos ids llego algo"+response.body().getInforme());
                    //guardo en propertis
                    resp=listener.guardarRespuestaInf(respuesta);


                    // lista.setValue(respuestaTiendas);

                 //   Log.d(TAG,"ya lo asigné"+respuestaTiendas.getTiendas().get(0).getUne_descripcion());
                    //  return lista;


                }
            }

            @Override
            public void onFailure(@Nullable Call<UltimoInfResponse> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e(Constantes.TAG, t.getMessage());

                }
            }
        });

    }

    public  UltimoInfResponse getUltimoInformex(String indice,int plantaSel, NuevoinformeViewModel.EnvioListener listener ) {

        Log.d(TAG,"haciendo peticion"+plantaSel);
        final Call<UltimoInfResponse> batch = apiClient.getApiService().getUltimosIdsI(indice,plantaSel,usuario);

        UltimoInfResponse respuesta = null;
        try {
            respuesta = batch.execute().body();

        if (respuesta != null) {
        Log.d(TAG,"ultimos ids llego algo"+respuesta.getInforme());
                    //guardo en propertis

        return respuesta;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

                    Log.e(TAG, "error al hacer peticion");
        return null;



    }

    public  MutableLiveData<Boolean> getUltimaVisita(String indice, NuevoinformeViewModel.EnvioListener listener ) {
        resp=new  MutableLiveData<Boolean>();
        final Call<UltimosIdsResponse> batch = apiClient.getApiService().getUltimosIdsV(indice,usuario);

        batch.enqueue(new Callback<UltimosIdsResponse>() {
            @Override
            public void onResponse(@Nullable Call<UltimosIdsResponse> call, @Nullable Response<UltimosIdsResponse> response) {
                Log.d(TAG,"ultimos ids llego algo"+response.body());
                if (response.isSuccessful() && response.body() != null) {
                    UltimosIdsResponse respuesta = response.body();
                    //guardo en propertis
                    resp=listener.guardarRespuestaVis(respuesta);


                    // lista.setValue(respuestaTiendas);

                    //   Log.d(TAG,"ya lo asigné"+respuestaTiendas.getTiendas().get(0).getUne_descripcion());
                    //  return lista;
                 }
            }

            @Override
            public void onFailure(@Nullable Call<UltimosIdsResponse> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e(Constantes.TAG, t.getMessage());

                }
            }
        });
        return resp;
    }


    public void getListasdeCompra(TablaVersiones comp, TablaVersiones version2, String indice, DescargasIniAsyncTask.DescargaIniListener listener){
        //busco la version de la app
         SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");

        PeticionLista peticion = new PeticionLista();
        if (comp != null && comp.getVersion() != null) {
             peticion.version_detalle = sdf.format(version2.getVersion());
             peticion.version_lista = sdf.format(comp.getVersion());
            peticion.indice = indice;
            peticion.usuario = usuario;
            //


        }else //es la 1a vez
        {
            peticion.version_detalle ="1999-09-09"; //una fecha muy antigua

            peticion.version_lista = "1999-09-09";
            peticion.indice = indice;
            peticion.usuario = usuario;

        }
       // hago la peticion
        pedirLista(peticion, listener);



    }

    public void pedirLista(PeticionLista peticion, DescargasIniAsyncTask.DescargaIniListener listener){

        Log.d("PeticionesServidor","haciendo petición");

        final Call<ListaCompraResponse> batch = apiClient.getApiService().getListasCompra(peticion.indice,peticion.usuario,peticion.version_lista,peticion.version_detalle);

        batch.enqueue(new Callback<ListaCompraResponse>() {
            @Override
            public void onResponse(@Nullable Call<ListaCompraResponse> call, @Nullable Response<ListaCompraResponse> response) {
                if (response.isSuccessful() && response.body() != null) {


                    ListaCompraResponse compraResp = response.body();
                    //reviso si está actualizado
                    if(compraResp.getStatus()==null||!compraResp.getStatus().equals("error")) //falta actualizar
                    {
                        Log.d("PeticionesServidor","resp>>"+compraResp.getStatus());

                        listener.actualizar(compraResp);
                        /*lcrepo.insertAll(compraResp.getCompras()); //inserto blblbl
                        lcdrepo.insertAll(compraResp.getDetalles());
                        //actualizar version en tabla
                        tvrepo.insertAll(compraResp.getVersiones());
*/
                    }
                    else //aviso al usuario //solo si esta desde descargar lista
                    {
                        Log.d("PeticionesServidor","lista compras descarga "+compraResp.getData());
                        listener.noactualizar(compraResp.getData());
                    }

                }
            }

            @Override
            public void onFailure(@Nullable Call<ListaCompraResponse> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e(Constantes.TAG, t.getMessage());

                }
            }
        });
    }

    public void cancelarInforme(int informeId,InformeCancelar informe){



        Call<ResponseBody> respuesta= apiClient.getApiService().cancelarInforme(informeId,informe);

        respuesta.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@Nullable Call<ResponseBody> call, @Nullable Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //actualizo su estatus

                }
            }

            @Override
            public void onFailure(@Nullable Call<ResponseBody> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e(Constantes.TAG, t.getMessage());

                }
            }
        });
    }

    public void autenticar(String username, String password, LoginActivity.LoginListener listener){

        Log.d("PeticionesServidor","haciendo petición logeo "+new String(android.util.Base64.encode(username.getBytes(), Base64.NO_WRAP))+"--"+new String(Base64.encode(password.getBytes(),Base64.NO_WRAP)));

        final Call<PostResponse>  batch = apiClient.getApiService().autenticarUser(new String(android.util.Base64.encode(username.getBytes(), Base64.NO_WRAP)), new String(Base64.encode(password.getBytes(),Base64.NO_WRAP)));
       // final Call<PostResponse>  batch = apiClient.getApiService().autenticarUser(username, password);


        batch.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(@Nullable Call<PostResponse> call, @Nullable Response<PostResponse> response) {
                Log.i(TAG,"respuesta"+response.message());
                if(response.code()==500){
                    listener.incorrecto("Usuario o contraseña incorrectos");
                }else

                if (response.isSuccessful() && response.body() != null) {


                    PostResponse logResp = response.body();
                    Log.i(TAG,"respuesta"+logResp.getData());
                    //reviso si está actualizado
                    if(logResp.getStatus().equals("ok")) //correcto
                    {

                        listener.correcto(logResp.getData());
                        //guardar cveuser


                    }
                    else //aviso al usuario //solo si esta desde descargar lista
                    {
                        listener.incorrecto(logResp.getData());
                    }

                }else{
                  //  PostResponse logResp = response.body();
                    listener.incorrecto("Error de conexión, intente nuevamete");
                }
            }

            @Override
            public void onFailure(@Nullable Call<PostResponse> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e("Peticiones servidor", t.getMessage());
                    listener.incorrecto(t.getMessage());
                }
            }
        });
    }

    public void insertarCatalogos(CatalogosResponse respuestaCats, CatalogoDetalleRepositoryImpl catRep, TablaVersionesRepImpl trepo, AtributoRepositoryImpl atrRepo){
        List<CatalogoDetalle> lista=respuestaCats.getCatalogos();
        //borro los catalogos que traigo
        catRep.deletexIdCat(2);
        catRep.deletexIdCat(8);
        catRep.deletexIdCat(15);
        catRep.deletexIdCat(100); //causas
        catRep.insertAll(lista);

        catRep.insertAll(respuestaCats.getCausas());
        List<Atributo> lista2=respuestaCats.getAtributos();
        //borro los catalogos que traigo
       // atrRepo.delete();
        atrRepo.insertAll(lista2);
        //actualizo tabla versiones para hacerlo 1 vez al dia
        TablaVersiones tv=new TablaVersiones();
        tv.setNombreTabla("catalogos");
        tv.setTipo("C");
        tv.setVersion(new Date());
        trepo.insertUpdate(tv);
        TablaVersiones tv2=new TablaVersiones();
        tv2.setNombreTabla("atributos");
        tv2.setTipo("C");
        tv2.setVersion(new Date());
        trepo.insertUpdate(tv2);
    }

    public MutableLiveData<List<Tienda>> getLista() {
        return lista;
    }

    public void setLista(MutableLiveData<List<Tienda>> lista) {
        this.lista = lista;
    }

    public class PeticionLista{
        String indice;
        String usuario;
        String version_lista;
        String version_detalle;

       public PeticionLista() {
       }

       public PeticionLista(String version_lista, String version_detalle) {
            this.version_lista = version_lista;
            this.version_detalle = version_detalle;
        }
    }


}
