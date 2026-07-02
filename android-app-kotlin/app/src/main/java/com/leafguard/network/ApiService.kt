package com.leafguard.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Kotlin twin of ApiService.java.
 *
 * Identical Retrofit contract: multipart POST to "predict". The Call-based
 * signature is kept (rather than suspend) so the enqueue/Callback flow in
 * MainActivity matches the Java app's networking behavior exactly.
 */
interface ApiService {

    @Multipart
    @POST("predict")
    fun uploadImage(@Part image: MultipartBody.Part): Call<PredictionResponse>
}
