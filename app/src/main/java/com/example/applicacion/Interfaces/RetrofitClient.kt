package com.example.applicacion.Interfaces


import com.example.applicacion.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // ✅ Cambia esta URL por la de tu backend
    // Emulador Android -> usa 10.0.2.2 en vez de localhost
    // Dispositivo físico -> usa la IP de tu PC ej: 192.168.1.10
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}