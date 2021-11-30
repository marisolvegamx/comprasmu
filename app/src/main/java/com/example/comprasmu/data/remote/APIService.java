package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCancelar;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Tienda;


import java.util.ArrayList;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface APIService {
    @POST("informe")
    @FormUrlEncoded
    Call<InformeWithDetalle> saveInforme(@Body InformeCompra item);

    @POST("informe/create")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<InformeEnvio> saveInformeEnvio(@Body InformeEnvio item);

    @POST("informes/create")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<PostResponse> saveInformesPend(@Body TodoEnvio item);

    @POST("imagenes")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<ImagenDetalle> saveImagen(@Body ImagenDetalle item);

    @Multipart
    @POST("fileUpload.php")
    Single<ResponseBody> onFileUpload(@Part("email") RequestBody mEmail,
                                      @Part MultipartBody.Part file);

    @GET("tiendas")
    Call<TiendasResponse> getTiendas(@Query("ciudad") String ciudad, @Query("tipo") String tipo, @Query("nombre") String nombre, @Query("usuario") String usuario);

    @GET("catalogos")
    Call<CatalogosResponse> getCatalogosNuevoInforme(@Query("usuario") String usuario);

    @GET("listacompras")
    Call<ListaCompraResponse> getListasCompra( @Query("indice") String indice, @Query("usuario") String usuario, @Query("version_lista") String version_lista, @Query("version_detalle") String version_detalle);
    
    @POST("cancelarInforme/{id}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<ResponseBody> cancelarInforme(@Path("id") int informeId, @Body InformeCancelar item);

    @FormUrlEncoded
    @POST("login")
    Call<PostResponse> autenticarUser(@Field("user") String user, @Field("pass") String pass);



}
