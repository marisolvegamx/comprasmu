package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCancelar;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.Sustitucion;
import com.example.comprasmu.data.modelos.Tienda;

import java.util.ArrayList;
import java.util.List;

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
    Call<PostResponse> saveInformeEnvio(@Body InformeEnvio item);

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

    @GET("catalogos")
    Call<CatalogosResponse> getCatalogosNuevoInforme(@Query("usuario") String usuario);
    @GET("sustitucion")
    Call<List<Sustitucion>> getSustitucion(@Query("usuario") String usuario);

    @GET("listacompras")
    Call<ListaCompraResponse> getListasCompra( @Query("indice") String indice, @Query("usuario") String usuario, @Query("version_lista") String version_lista, @Query("version_detalle") String version_detalle);

    @GET("ultids")
    Call<UltimosIdsResponse> getUltimosIdsV( @Query("indice") String indice, @Query("usuario") String usuario);

    @GET("ultids")
    Call<UltimoInfResponse> getUltimosIdsI( @Query("indice") String indice,@Query("planta") int plantasel, @Query("usuario") String usuario);

    @GET("plantapen")
    Call<PlantaResponse> getPlantaPeniafiel( @Query("siglas") String indice, @Query("usuario") String usuario);

    @POST("cancelarInforme/{id}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<ResponseBody> cancelarInforme(@Path("id") int informeId, @Body InformeCancelar item);

    @FormUrlEncoded
    @POST("login")
    Call<PostResponse> autenticarUser(@Field("user") String user, @Field("pass") String pass);

    @GET("tiendas")
    Call<TiendasResponse> getTiendas(@Query("pais") String pais,@Query("ciudad") String ciudad, @Query("plan") int planta,  @Query("cli") int cliente,@Query("fini") String fechaini,@Query("ffin") String fechafin,@Query("tipo") String tipo, @Query("nombre") String nombre, @Query("usuario") String usuario);

    @GET("tienda/cancel")
    Call<PostResponse> tiendaCancel(@Query("id") int idtienda, @Query("usuario") String usuario);

    @GET("geocercas")
    Call<TiendasResponse> getGeocercas(@Query("indice") String indice, @Query("usuario") String usuario);

    @GET("descresp")
    Call<RespInformesResponse> getRespaldoInf( @Query("indice") String indice, @Query("usuario") String usuario);
    @GET("descresp2")
    Call<RespInfEtapaResponse> getRespaldoInf2( @Query("indice") String indice, @Query("usuario") String usuario);

    @POST("infetapa/create")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<PostResponse> saveInformeEtapa(@Body InformeEtapaEnv item);
    @POST("informeseta/create")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<PostResponse> saveInformesEtaPend(@Body TodoEnvio item);

    @GET("solcorreccion")
    Call<SolCorreResponse> getSolicitudCorre(@Query("indice") String indice, @Query("usuario") String usuario, @Query("etapa") int etapa,@Query("version") String version);

    @POST("correccion/create")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<PostResponse> saveCorreccion(@Body CorreccionEnvio item);

    @POST("correcciones/create")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<PostResponse> saveCorreccionPend(@Body List<CorreccionEnvio> item);
}
