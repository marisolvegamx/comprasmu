package com.example.comprasmu.data;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.InformeCancelar;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.data.remote.GenericResponse;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.remote.ServiceGenerator;
import com.example.comprasmu.data.remote.TiendasResponse;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;

import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeticionesServidor {

    ServiceGenerator apiClient;
    String usuario;
    static final String TABLA_LISTA="pr_listacompra";
    static final String TABLA_DETALLE="pr_listacompradetalle";
    LiveData<ArrayList<Tienda>> lista;
    public PeticionesServidor(String usuario) {
        this.usuario = usuario;

    }

    public void getCatalogos(CatalogoDetalleRepositoryImpl catRep) {

        final Call<GenericResponse<CatalogoDetalle>> batch = apiClient.getApiService().getCatalogosNuevoInforme(usuario);

        batch.enqueue(new Callback<GenericResponse<CatalogoDetalle>>() {
            @Override
            public void onResponse(@Nullable Call<GenericResponse<CatalogoDetalle>> call, @Nullable Response<GenericResponse<CatalogoDetalle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GenericResponse<CatalogoDetalle> respuestaCats = response.body();
                    List<CatalogoDetalle> lista=respuestaCats.getDatos();
                    catRep.insertAll(lista);


                }
            }

            @Override
            public void onFailure(@Nullable Call<GenericResponse<CatalogoDetalle>> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e(Constantes.TAG, t.getMessage());

                }
            }
        });
    }

    public   LiveData<ArrayList<Tienda>> getTiendas(String ciudad, String tipo, String nombre ) {

        final Call<TiendasResponse> batch = apiClient.getApiService().getTiendas(ciudad,tipo,nombre,usuario);

        batch.enqueue(new Callback<TiendasResponse>() {
            @Override
            public void onResponse(@Nullable Call<TiendasResponse> call, @Nullable Response<TiendasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TiendasResponse respuestaTiendas = response.body();
                     lista=respuestaTiendas.getTiendas();
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
        return lista;
    }

    public void getListasdeCompra(TablaVersionesRepImpl tvrepo, String indice, ListaCompraDetRepositoryImpl lcdrepo){
        //busco la version de la app
         LiveData<TablaVersiones> version=tvrepo.getVersionByNombreTabla("pr_listacompradetalle");
         SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
         version.observeForever(new Observer<TablaVersiones>() {
                                    @Override
                                    public void onChanged(TablaVersiones tablaVersiones) {
                                        PeticionLista peticion = new PeticionLista();
                                        if (tablaVersiones != null && tablaVersiones.getVersion() != null) {


                                            peticion.version_detalle = sdf.format(tablaVersiones.getVersion());
                                            LiveData<TablaVersiones> version2 = tvrepo.getVersionByNombreTabla(TABLA_LISTA);

                                            version2.observeForever(new Observer<TablaVersiones>() {
                                                @Override
                                                public void onChanged(TablaVersiones tablaVersiones) {

                                                    peticion.version_lista = sdf.format(tablaVersiones.getVersion());
                                                    peticion.indice = indice;
                                                    peticion.usuario = usuario;
                                                    //hago la peticion
                                                    pedirLista(peticion, lcdrepo, tvrepo);
                                                }
                                            });//fin version2
                                        }else //el la 1a vez
                                        {
                                            peticion.version_detalle ="";

                                            peticion.version_lista = "";
                                            peticion.indice = indice;
                                            peticion.usuario = usuario;
                                            //hago la peticion
                                            pedirLista(peticion, lcdrepo, tvrepo);
                                        }
                                    }
                                });


    }

    public void pedirLista(PeticionLista peticion,ListaCompraDetRepositoryImpl lcdrepo,TablaVersionesRepImpl tvrepo){

        ListaCompraRepositoryImpl lcrepo=new ListaCompraRepositoryImpl();

        final Call<ListaCompraResponse> batch = apiClient.getApiService().getListasCompra(peticion);

        batch.enqueue(new Callback<ListaCompraResponse>() {
            @Override
            public void onResponse(@Nullable Call<ListaCompraResponse> call, @Nullable Response<ListaCompraResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ListaCompraResponse compraResp = response.body();
                   //reviso si está actualizado
                    if(compraResp.getActualizacion().equals("0")) //falta actualizar
                    {
                        lcrepo.insertAll(compraResp.getCompras()); //inserto blblbl
                        lcdrepo.insertAll(compraResp.getDetalles());
                        //actualizar version en tabla
                        tvrepo.insertAll(compraResp.getVersiones());

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
