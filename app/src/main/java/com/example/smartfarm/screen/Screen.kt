package com.example.smartfarm.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {
    Dashboard(route = "dashboard", title = "Dashboard", icon = Icons.Default.Dashboard),
    Device(route = "device", title = "Device", icon = Icons.Default.Sensors),
    Data(route = "data", title = "Data", icon = Icons.Default.ShowChart),
    Notification(route = "notification", title = "Notification", icon = Icons.Default.Notifications),
    Lighting(route = "lighting", title = "Lighting"),
    Soil(route = "soil", title = "Soil"),
    Door(route = "door", title = "Door"),
    Air(route = "air", title = "Air"),
    Feeding(route = "feeding", title = "Feeding");

    companion object {
        val bottomBarItems = listOf(Dashboard, Device, Data, Notification)
    }
}
