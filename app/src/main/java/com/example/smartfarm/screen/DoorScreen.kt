package com.example.smartfarm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DoorScreen(navController: NavController) {
    var isOpen by remember { mutableStateOf(false) }
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
        Text("CỬA ĐIỀU KHIỂN TỪ XA", style = MaterialTheme.typography.headlineSmall)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Trạng thái: ${if (isOpen) "Đang mở" else "Đã đóng"}")
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { isOpen = true }, enabled = !isOpen) { Text("Mở cửa") }
                    Button(onClick = { isOpen = false }, enabled = isOpen) { Text("Đóng cửa") }
                }
            }
        }
    }
}
