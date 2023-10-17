package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.ImagenDetalle;

import retrofit2.Response;

public interface ImageUploadCallback {
    void onProgressUpdate(int percentage);
    void onError(Response<PostResponse> response);
    void onSuccess(Response<PostResponse> response);
    void onErrorGen(Response<PostResponse> response);
    void onSuccessGen(Response<PostResponse> response);
}
