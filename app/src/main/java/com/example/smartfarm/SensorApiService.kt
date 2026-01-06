package com.example.smartfarm

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("sensor/change-status")
    suspend fun changeSensorStatus(
        @Body body: ChangeStatusRequest
    ): ChangeStatusResponse

    data class ChangeStatusRequest(
        val deviceName: String,
        val sensorType: String,
        val value: Int
    )

    data class ChangeStatusResponse(
        val message: String? = null,
        val error: String? = null
    )

}
