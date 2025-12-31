package com.example.smartfarm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FeedingScreen(navController: NavController) {
    var lastFeed by remember { mutableStateOf("Chưa cấp") }
    var lastWater by remember { mutableStateOf("Chưa cấp") }
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nút Back
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Quay lại")
        }
        Text("CẤP THỨC ĂN / NƯỚC", style = MaterialTheme.typography.headlineSmall)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Thức ăn: $lastFeed")
                Button(onClick = {
                    lastFeed = "Lúc " + java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Date())
                }) { Text("Cấp thức ăn") }
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Nước uống: $lastWater")
                Button(onClick = {
                    lastWater = "Lúc " + java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Date())
                }) { Text("Cấp nước") }
            }
        }
    }
}