package com.example.comprasmu.data.remote;

import retrofit2.Response;

public interface ImageUploadCallback {
    void onProgressUpdate(int percentage);
    void onError(Response<PostResponse> response);
    void onSuccess(Response<PostResponse> response);
}
