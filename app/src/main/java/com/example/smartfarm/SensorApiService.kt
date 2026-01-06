package com.example.smartfarm

import retrofit2.http.GET
import retrofit2.http.Path

interface SensorApiService {
    @GET("sensor/get-latest/{deviceName}/{sensorType}")
    suspend fun getLatestSensorData(
        @Path("deviceName") deviceName: String,
        @Path("sensorType") sensorType: String
    ): SensorData

    @GET("sensor-data/history/{deviceName}/{gasType}")
    suspend fun getSensorHistory(
        @Path("deviceName") deviceName: String,
        @Path("gasType") gasType: String
    ): List<SensorData>
}
