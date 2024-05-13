package com.example.comprasmu.data.remote;

import android.os.Build;
import android.util.Log;

import com.example.comprasmu.utils.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static  String BASE_URL ;

  //  private static final String BASE_URL = "http://192.168.1.79/comprasv1/api/public/";

    private static APIService servicio;

    public static APIService getApiService() {
        servicio=null;
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

       // Log.e("mi disp ",Build.PRODUCT);
        if (Build.PRODUCT.contains ("sdk")||Build.MODEL.contains ("2006C3MG1")){//pruebas y el lenovo
            //nam
           BASE_URL = "http://192.168.1.84/comprasv1/api/public/";
           BASE_URL = Constantes.URLPRUEBAS1+ "api/public/";

       }else
        {
            BASE_URL = Constantes.URLSERV+"api/public/";

          //  BASE_URL = "http://192.168.1.84/comprasv1/pruebas/public/";
         //   BASE_URL = "https://muesmerc.mx/comprasv1/pruebas/public/";
        }


        OkHttpClient httpClient =new OkHttpClient.Builder()
                .readTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
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
       // Log.d("NUEVA URL",servicio.);
        return servicio;
    }

}
