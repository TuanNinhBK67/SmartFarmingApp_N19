package com.example.smartfarm.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String = Screen.Dashboard.route) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.Device.route) { DeviceScreen(navController) }
        composable(Screen.Data.route) { DataScreen(navController) }
        composable(Screen.Notification.route) { NotificationScreen(navController) }
        composable(Screen.Lighting.route) { LightingScreen(navController) }
        composable(Screen.Soil.route) { SoilScreen(navController) }
        composable(Screen.Door.route) { DoorScreen(navController) }
        composable(Screen.Air.route) { AirScreen(navController) }
        composable(Screen.Feeding.route) { FeedingScreen(navController) }
    }
}
