package com.leafguard.network

import com.leafguard.BuildConfig
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Kotlin twin of RetrofitClient.java.
 *
 * A Java final class with private constructor + static methods becomes a
 * Kotlin `object` singleton. Same default base URL (10.0.2.2 reaches the
 * host machine from the emulator), same 30s timeouts, same logging levels.
 */
object RetrofitClient {

    private const val DEFAULT_BASE_URL = "http://10.0.2.2:8000/"

    private var retrofit: Retrofit? = null
    private var activeBaseUrl: String = DEFAULT_BASE_URL

    @Synchronized
    fun getInstance(baseUrl: String = DEFAULT_BASE_URL): Retrofit {
        val current = retrofit
        if (current == null || activeBaseUrl != baseUrl) {
            activeBaseUrl = baseUrl

            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.BASIC
                }
            }

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(activeBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    fun getApiService(): ApiService = getInstance().create(ApiService::class.java)
}
