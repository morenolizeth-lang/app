package com.example.applicacion.Interfaces

import com.example.applicacion.network.ApiService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.20.36:8080/"

    val api: ApiService by lazy {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")  // ✅ evita que Gson interprete fechas como objetos
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}