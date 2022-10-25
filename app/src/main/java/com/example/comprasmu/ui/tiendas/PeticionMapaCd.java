package com.example.comprasmu.ui.tiendas;

import android.util.Log;

import androidx.annotation.Nullable;

import androidx.lifecycle.MutableLiveData;

import com.example.comprasmu.DescargasIniAsyncTask;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.Tienda;
import com.example.comprasmu.data.remote.PostResponse;
import com.example.comprasmu.data.remote.ServiceGenerator;
import com.example.comprasmu.data.remote.TiendasResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeticionMapaCd {

    ServiceGenerator apiClient;
    String usuario;

    static final String TAG="PeticionMapaCd";

    MutableLiveData<List<Tienda>> listatiendas;
    MutableLiveData<List<Geocerca>> listageocercas;

    public PeticionMapaCd(String usuario ) {
        this.usuario = usuario;
        listatiendas=new MutableLiveData<>();
        listageocercas=new MutableLiveData<>();
    }


    public  void getTiendas(String pais, String ciudad,int planta,int cliente, String fechaini,String fechafin, String tipo, String nombre ) {
        Log.d(TAG,"haciendo petición "+nombre+"--"+tipo);

        final Call<TiendasResponse> batch = apiClient.getApiService().getTiendas(pais, ciudad, planta, cliente, fechaini,fechafin,tipo,nombre,usuario);

        batch.enqueue(new Callback<TiendasResponse>() {
            @Override
            public void onResponse(@Nullable Call<TiendasResponse> call, @Nullable Response<TiendasResponse> response) {
//               Log.d(TAG,"llego algo"+response.body().toString());
                if (response.isSuccessful() && response.body() != null) {
                    TiendasResponse respuestaTiendas = response.body();
                    if(respuestaTiendas!=null) {
                        listatiendas.setValue(respuestaTiendas.getTiendas());
                        listageocercas.setValue(respuestaTiendas.getGeocercas());

                    }
                    //  return lista;


                }
            }

            @Override
            public void onFailure(@Nullable Call<TiendasResponse> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e(TAG, t.getMessage());

                }
            }
        });

    }

    public  void getZonas(String pais, String indice, DescargasIniAsyncTask.DescargaIniListener listener) {

        final Call<TiendasResponse> batch = apiClient.getApiService().getGeocercas(indice,usuario);

        batch.enqueue(new Callback<TiendasResponse>() {
            @Override
            public void onResponse(@Nullable Call<TiendasResponse> call, @Nullable Response<TiendasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG,"getZonas llego algo"+response.body().toString());

                    TiendasResponse respuestaTiendas = response.body();
                    if(respuestaTiendas!=null) {
                      //  listatiendas.setValue(respuestaTiendas.getTiendas());
                        listener.insertarZonas(respuestaTiendas.getGeocercas());

                        // lista.setValue(respuestaTiendas);

                        Log.d(TAG, "getZonas ya lo asigné" + respuestaTiendas.getTiendas());
                    }
                    //  return lista;


                }
            }

            @Override
            public void onFailure(@Nullable Call<TiendasResponse> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e(TAG, t.getMessage());

                }
                listener.noactualizar2("fin");
            }
        });

    }

    public  void cancelTienda( int idtienda ) {
        Log.d(TAG,"haciendo petición borrar"+idtienda);

        final Call<PostResponse> batch = apiClient.getApiService().tiendaCancel(idtienda,usuario);

        batch.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(@Nullable Call<PostResponse> call, @Nullable Response<PostResponse> response) {
//               Log.d(TAG,"llego algo"+response.body().toString());
                if (response.isSuccessful() && response.body() != null) {
                    PostResponse respuestaCancel = response.body();
                    if(respuestaCancel!=null&&respuestaCancel.getStatus().equals("ok")) {

                         //vuelvo a cargar el mapa

                    }
                    //  return lista;


                }
            }

            @Override
            public void onFailure(@Nullable Call<PostResponse> call, @Nullable Throwable t) {
                if (t != null) {
                    Log.e(TAG, t.getMessage());

                }
            }
        });

    }


    public MutableLiveData<List<Tienda>> getListatiendas() {
        return listatiendas;
    }

    public MutableLiveData<List<Geocerca>> getListageocercas() {
        return listageocercas;
    }
}
