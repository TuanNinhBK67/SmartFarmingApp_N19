package com.example.smartfarm

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.google.gson.annotations.SerializedName

data class ChangeStatusRequest(
    val deviceName: String,
    val sensorType: String,
    val value: Int // 1: bật, 0: tắt
)

data class ApiResponse(
    val message: String?
)

data class SensorHistoryData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("number_value")
    val value: Double,
    @SerializedName("timestamp")
    val timestamp: String?
)

interface SensorApiService {
    @GET("sensor/get-latest/{deviceName}/{sensorType}")
    suspend fun getLatestSensorData(
        @Path("deviceName") deviceName: String,
        @Path("sensorType") sensorType: String
    ): SensorData

    @GET("sensor-data/history/{deviceName}/{sensorType}")
    suspend fun getSensorHistory(
        @Path("deviceName") deviceName: String,
        @Path("sensorType") sensorType: String
    ): List<SensorHistoryData>

    @POST("sensor/change-status")
    suspend fun changeSensorStatus(@Body body: ChangeStatusRequest): ApiResponse
}
