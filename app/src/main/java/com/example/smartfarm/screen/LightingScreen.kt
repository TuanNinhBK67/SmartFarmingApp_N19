package com.example.smartfarm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LightingScreen() {
    var lux by remember { mutableStateOf(120.0) }
    var ledOn by remember { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("ĐIỀU KHIỂN ÁNH SÁNG", style = MaterialTheme.typography.headlineSmall)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Cường độ sáng: ${lux.toInt()} lux")
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Đèn LED")
                    Switch(checked = ledOn, onCheckedChange = { ledOn = it })
                }
            }
        }
        Button(onClick = { lux = (50..250).random().toDouble() }) {
            Text("Giả lập cảm biến Lux")
        }
    }
}
