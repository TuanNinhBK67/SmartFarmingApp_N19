package com.example.smartfarm

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: SensorApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://iot-server-n19.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SensorApiService::class.java)
    }
}
