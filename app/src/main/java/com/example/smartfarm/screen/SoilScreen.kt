package com.example.smartfarm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SoilScreen(navController: NavController) {
    var soil by remember { mutableStateOf(35) }
    var pumpOn by remember { mutableStateOf(false) }
    var thresholdLow by remember { mutableStateOf(30) }
    var thresholdHigh by remember { mutableStateOf(60) }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Quay lại")
        }
        Text("KIỂM SOÁT ĐỘ ẨM ĐẤT", style = MaterialTheme.typography.headlineSmall)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Độ ẩm đất: $soil %")
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Bơm tưới")
                    Switch(checked = pumpOn, onCheckedChange = { pumpOn = it })
                }
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Ngưỡng bơm tưới tự động:")
                Row {
                    Text("Dưới")
                    Spacer(Modifier.width(8.dp))
                    Slider(
                        value = thresholdLow.toFloat(),
                        onValueChange = { thresholdLow = it.toInt() },
                        valueRange = 10f..90f
                    )
                    Text("$thresholdLow%")
                }
                Row {
                    Text("Trên")
                    Spacer(Modifier.width(8.dp))
                    Slider(
                        value = thresholdHigh.toFloat(),
                        onValueChange = { thresholdHigh = it.toInt() },
                        valueRange = 10f..90f
                    )
                    Text("$thresholdHigh%")
                }
            }
        }
        Button(onClick = { soil = (20..80).random() }) {
            Text("Giả lập cảm biến độ ẩm")
        }
    }
}
