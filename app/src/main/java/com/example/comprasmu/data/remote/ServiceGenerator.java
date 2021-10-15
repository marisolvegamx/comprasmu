package com.example.comprasmu.data.remote;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_URL = "https://api.github.com/";

    private static APIService servicio;

    public static APIService getApiService() {

        // Creamos un interceptor y le indicamos el log level a usar
       /* HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
*/
        // Asociamos el interceptor a las peticiones
    /*    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());*/

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();

        if (servicio == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                 //   .client(httpClient.build()) // <-- usamos el log level
                    .build();
            servicio = retrofit.create(APIService.class);
        }

        return servicio;
    }





}
