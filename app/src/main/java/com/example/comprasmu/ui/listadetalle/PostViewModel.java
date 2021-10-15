package com.example.comprasmu.ui.listadetalle;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.databinding.ObservableField;

import com.example.comprasmu.data.remote.APIService;
import com.example.comprasmu.data.modelos.Post;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewModel extends ViewModel {
    private APIService mAPIService;
    Post post;
    public final ObservableField<String> mensaje = new ObservableField<>();
    private String TAG="Ejemplo";

    public void sendPost(String title, String body) {
       /* mAPIService.savePost(title, body, 1).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(response.isSuccessful()) {
                    mensaje.set(response.body().toString());

                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });*/
    }
}
