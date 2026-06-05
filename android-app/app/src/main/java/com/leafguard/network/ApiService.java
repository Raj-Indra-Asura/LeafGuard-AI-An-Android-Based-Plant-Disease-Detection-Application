package com.leafguard.network;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("predict")
    Call<PredictionResponse> uploadImage(@Part MultipartBody.Part image);
}
