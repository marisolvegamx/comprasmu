package com.example.comprasmu.data.remote;

import android.os.Build;

import com.example.comprasmu.utils.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/******* conexion de retrofit que se usa para las peticiones de texto  servidor
 * los archivos de fotos se suben con servicegeneratorim********/
public class ServiceGenerator {

    //  private static final String BASE_URL = "http://192.168.1.79/comprasv1/api/public/";
    private static APIService servicio = null;
    private static OkHttpClient httpClient = null;
    // synchronized:Esto evita que si tu app hace dos peticiones muy rápidas al mismo tiempo, se intenten crear dos clientes a la vez.

    public static synchronized APIService getApiService() {

        // Creamos un interceptor y le indicamos el log level a usar
      /* proximamente cambiaremos este codigo para cambiar el tiempo de conexion para las imagenes */
        /*
        // ... dentro de getApiService()
if (httpClient == null) {
    httpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                okhttp3.Request request = chain.request();
                // Leemos el header "Custom-Timeout"
                String timeoutHeader = request.header("Custom-Timeout");

                if (timeoutHeader != null) {
                    int newTimeout = Integer.parseInt(timeoutHeader);
                    return chain
                            .withConnectTimeout(15, TimeUnit.SECONDS) // El connect suele ser igual
                            .withWriteTimeout(newTimeout, TimeUnit.SECONDS)
                            .withReadTimeout(newTimeout, TimeUnit.SECONDS)
                            .proceed(request);
                }

                return chain.proceed(request);
            })
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)  // Timeout por defecto para texto
            .writeTimeout(30, TimeUnit.SECONDS) // Timeout por defecto para texto
            .retryOnConnectionFailure(true)
            .build();
}

y en el api service
 public interface APIService {
    // Petición normal (usará los 30s por defecto)
    @GET("usuarios/perfil")
    Call<Usuario> getPerfil();

    // Petición de subida (le pasamos el Header para que el interceptor lo cambie a 150s)
    @Headers("Custom-Timeout: 150")
    @Multipart
    @POST("compras/subir-imagen")
    Call<ResponseBody> subirImagen(@Part MultipartBody.Part file);
}
        *
        *
        *
        * */
        if (servicio == null) {
        String BASE_URL;
        if (Build.PRODUCT.contains ("sdk")||Build.MODEL.contains (Constantes.modelo)){//pruebas y el lenovo
            //nam

           BASE_URL = Constantes.URLPRUEBAS1+ "api/public/";

       }else
        {
            BASE_URL = Constantes.URLSERV+"api/public/";

        }

        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .readTimeout(50, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(35, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true) // Agregado para estabilidad
                    .build();
        }



        //todo aqui hay que crear el typadapter de json para serealizar y deserealizar los gastos detalle y el boolean https://stackoverflow.com/questions/59513826/how-to-register-custom-typeadapter-or-jsondeserializer-with-gson-in-retrofit
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient) // <-- usamos el log level
                    .build();
            servicio = retrofit.create(APIService.class);
        }
       // Log.d("NUEVA URL",servicio.);
        return servicio;
    }

}
