/*
 * Exercise 3: Retrofit API client
 * Week 5 - Android Networking with Retrofit
 *
 * Starter skeleton. In the real project these correspond to:
 *   android-app/app/src/main/java/com/leafguard/network/ApiService.java
 *   android-app/app/src/main/java/com/leafguard/network/RetrofitClient.java
 * Complete the TODOs (see exercises/android/README.md, Ex 4.1-4.5).
 *
 * Goal: upload a leaf image to the FastAPI backend's /predict endpoint and
 * parse the JSON response into a PredictionResponse model.
 *
 * Key concepts: Retrofit interface, multipart upload, Gson, async enqueue()
 * (NEVER call execute() on the main thread -> NetworkOnMainThreadException).
 *
 * Verification:
 *   [ ] BASE_URL points to your running backend (use 10.0.2.2 for the emulator)
 *   [ ] Image uploads as multipart/form-data with part name "image"
 *   [ ] Success and failure callbacks both handled (no crash on network error)
 */
package com.leafguard.network;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public final class ex03_ApiClient {

    // TODO 1: set this to your backend. Android emulator -> http://10.0.2.2:8000/
    private static final String BASE_URL = "http://10.0.2.2:8000/";

    private ex03_ApiClient() {
    }

    /** The Retrofit service interface for the backend. */
    public interface ApiService {
        // TODO 2: declare the multipart POST call to "predict".
        @Multipart
        @POST("predict")
        Call<PredictionResponse> predict(@Part MultipartBody.Part image);
    }

    /** Build (and ideally cache) a single Retrofit instance. */
    public static ApiService create() {
        // TODO 3: add an OkHttpClient with sensible timeouts and a logging
        //   interceptor while developing.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

    // TODO 4: in your Activity, wrap the image File in a MultipartBody.Part
    //   named "image" and call predict(...).enqueue(callback).
    //   Use MultipartBody.Part.createFormData("image", file.getName(), requestBody).
}
