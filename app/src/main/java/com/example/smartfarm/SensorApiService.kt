package com.example.smartfarm

import retrofit2.http.GET
import retrofit2.http.Path

interface SensorApiService {
    @GET("sensor/get-latest/{deviceName}")
    suspend fun getLatestSensorData(
        @Path("deviceName") deviceName: String
    ): SensorData
}
