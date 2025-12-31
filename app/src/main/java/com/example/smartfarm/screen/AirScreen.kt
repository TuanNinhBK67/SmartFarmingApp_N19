package com.example.smartfarm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AirScreen(navController: NavController) {
    var co2 by remember { mutableStateOf(510) }
    var nh3 by remember { mutableStateOf(14) }
    var h2s by remember { mutableStateOf(4) }
    var fanOn by remember { mutableStateOf(false) }
    var mistOn by remember { mutableStateOf(false) }

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
        Text("CHẤT LƯỢNG KHÔNG KHÍ", style = MaterialTheme.typography.headlineSmall)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("CO2: $co2 ppm")
                Text("NH3: $nh3 ppm")
                Text("H2S: $h2s ppm")
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Quạt hút khí")
            Switch(checked = fanOn, onCheckedChange = { fanOn = it })
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Phun sương khử mùi")
            Switch(checked = mistOn, onCheckedChange = { mistOn = it })
        }
        Button(onClick = {
            co2 = (400..1000).random()
            nh3 = (0..40).random()
            h2s = (0..10).random()
        }) { Text("Giả lập cảm biến khí") }
    }
}
