package com.example.smartfarm.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketManager {
    private var socket: Socket? = null

    fun connectSocket() {
        try {
            socket = IO.socket("https://iot-server-n19.onrender.com")
            socket?.connect()
            Log.d("Socket", "Socket connected!")
        } catch (e: Exception) {
            Log.e("Socket", "Connect error: ${e.message}")
        }
    }

    fun disconnectSocket() {
        socket?.disconnect()
        socket = null
    }

    fun setOnSensorUpdate(listener: (device: String, value: Float, time: String) -> Unit) {
        socket?.on("sensor_update") { args ->
            if (args.isNotEmpty()) {
                val data = args[0] as JSONObject
                val device = data.optString("device")
                val value = data.optDouble("value", 0.0).toFloat()
                val time = data.optString("time")
                listener(device, value, time)
            }
        }
    }
}
