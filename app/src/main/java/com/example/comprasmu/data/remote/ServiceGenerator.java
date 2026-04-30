package com.example.comprasmu.data.remote;

import android.os.Build;

import com.example.comprasmu.utils.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    //  private static final String BASE_URL = "http://192.168.1.79/comprasv1/api/public/";

    public static APIService getApiService() {
        APIService servicio = null;
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
        String BASE_URL;
        if (Build.PRODUCT.contains ("sdk")||Build.MODEL.contains (Constantes.modelo)){//pruebas y el lenovo
            //nam

           BASE_URL = Constantes.URLPRUEBAS1+ "api/public/";

       }else
        {
            BASE_URL = Constantes.URLSERV+"api/public/";

        }

        OkHttpClient.Builder okbuilder=new OkHttpClient.Builder();
        OkHttpClient httpClient;

             httpClient = okbuilder
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .build();


       //todo aqui hay que crear el typadapter de json para serealizar y deserealizar los gastos detalle y el boolean https://stackoverflow.com/questions/59513826/how-to-register-custom-typeadapter-or-jsondeserializer-with-gson-in-retrofit
        Gson gson = new GsonBuilder()
                .setLenient()
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
