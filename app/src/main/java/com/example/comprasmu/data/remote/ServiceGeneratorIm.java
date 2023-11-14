package com.example.comprasmu.data.remote;

import android.os.Build;

import com.example.comprasmu.utils.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGeneratorIm {
    private static  String BASE_URL ;

  //  private static final String BASE_URL = "http://192.168.1.79/comprasv1/api/public/";

    private static APIService servicio;

    public static APIService getApiService() {


        if (Build.PRODUCT.contains ("sdk")||Build.PRODUCT.contains ("A2016b30")){//pruebas y el lenovo
            //nam
            BASE_URL = Constantes.URLPRUEBAS1+ "api/public/";

        }else {
            BASE_URL = Constantes.URLSERV + "api/public/";
        }


        OkHttpClient httpClient =new OkHttpClient.Builder()
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        if (servicio == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient) // <-- usamos el log level
                    .build();
            servicio = retrofit.create(APIService.class);
        }

        return servicio;
    }

}
