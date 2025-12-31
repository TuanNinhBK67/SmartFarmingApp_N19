package com.example.smartfarm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DashboardScreen(navController: NavController) {
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("SMART FARM DASHBOARD", style = MaterialTheme.typography.headlineSmall)
        Button(
            onClick = { navController.navigate(Screen.Lighting.name) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Điều khiển ánh sáng") }

        Button(
            onClick = { navController.navigate(Screen.Soil.name) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Kiểm soát độ ẩm đất") }

        Button(
            onClick = { navController.navigate(Screen.Door.name) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Điều khiển cửa") }

        Button(
            onClick = { navController.navigate(Screen.Air.name) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Chất lượng không khí") }

        Button(
            onClick = { navController.navigate(Screen.Feeding.name) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cấp thức ăn/nước") }
    }
}

