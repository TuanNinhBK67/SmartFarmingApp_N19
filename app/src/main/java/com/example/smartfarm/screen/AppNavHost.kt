package com.example.smartfarm.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String = Screen.Dashboard.name) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Dashboard.name) { DashboardScreen(navController) }
        composable(Screen.Lighting.name) { LightingScreen(navController) }
        composable(Screen.Soil.name) { SoilScreen(navController) }
        composable(Screen.Door.name) { DoorScreen(navController) }
        composable(Screen.Air.name) { AirScreen(navController) }
        composable(Screen.Feeding.name) { FeedingScreen(navController) }
    }
}
