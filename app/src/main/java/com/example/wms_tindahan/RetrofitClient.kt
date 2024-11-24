package com.example.wms_tindahan

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Method to initialize Retrofit using the base URL loaded from assets
    fun getInstance(context: Context): WmsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(readBaseUrl(context))  // Dynamically load base URL
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(WmsApiService::class.java)
    }

    // Helper method to read base URL from assets
    private fun readBaseUrl(context: Context): String {
        val baseUrl = StringBuilder()

        try {
            val inputStream = context.assets.open("base_url.txt")
            val reader = inputStream.bufferedReader()
            reader.forEachLine {
                baseUrl.append(it.trim())
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return baseUrl.toString()
    }
}