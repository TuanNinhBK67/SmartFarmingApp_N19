package com.example.smartfarm

import com.google.gson.annotations.SerializedName

data class SensorData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("number_value")
    val value: Double
)
