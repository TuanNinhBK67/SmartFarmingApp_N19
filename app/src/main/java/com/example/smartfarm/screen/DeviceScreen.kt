package com.example.smartfarm.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LineStyle
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun DeviceScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Device.route

    // Fake state để test giao diện
    var ledOn by remember { mutableStateOf(false) }
    var pumpOn by remember { mutableStateOf(false) }
    var doorOpen by remember { mutableStateOf(false) }
    var fanOn by remember { mutableStateOf(false) }
    var feedingOn by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavBar(navController, currentRoute) },
        containerColor = Color(0xFFF1F8F4)
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF1F8F4))
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF256029))
                    .padding(vertical = 14.dp)
            ) {
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        "Device Management",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        "Control all devices remotely",
                        color = Color(0xFFD0F8CE),
                        fontSize = 13.sp
                    )
                }
            }
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
            ){
                Spacer(Modifier.height(14.dp))
                Text("Device Control", style = MaterialTheme.typography.titleLarge, color = Color(0xFF256029))
                Spacer(Modifier.height(10.dp))

                // LED Light Control
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFEFFBF2))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lightbulb, contentDescription = "LED", tint = if (ledOn) Color(0xFFFFD600) else Color.Gray)
                            Spacer(Modifier.width(12.dp))
                            Text("LED Light", fontWeight = FontWeight.Bold)
                        }
                        Switch(checked = ledOn, onCheckedChange = { ledOn = it })
                    }
                }
                Spacer(Modifier.height(12.dp))

                // Pump Control
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFEFFBF2))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WaterDrop, contentDescription = "Pump", tint = if (pumpOn) Color(0xFF43A047) else Color.Gray)
                            Spacer(Modifier.width(12.dp))
                            Text("Irrigation Pump", fontWeight = FontWeight.Bold)
                        }
                        Switch(checked = pumpOn, onCheckedChange = { pumpOn = it })
                    }
                }
                Spacer(Modifier.height(12.dp))

                // Door Control
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFEFFBF2))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DoorFront, contentDescription = "Door", tint = if (doorOpen) Color(0xFF388E3C) else Color.Gray)
                            Spacer(Modifier.width(12.dp))
                            Text("Automatic Door", fontWeight = FontWeight.Bold)
                        }
                        Switch(checked = doorOpen, onCheckedChange = { doorOpen = it })
                    }
                }
                Spacer(Modifier.height(12.dp))

                // Fan Control (nếu có, có thể ẩn nếu farm bạn chưa lắp)
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFEFFBF2))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Air, contentDescription = "Fan", tint = if (fanOn) Color(0xFF4FC3F7) else Color.Gray)
                            Spacer(Modifier.width(12.dp))
                            Text("Ventilation Fan", fontWeight = FontWeight.Bold)
                        }
                        Switch(checked = fanOn, onCheckedChange = { fanOn = it })
                    }
                }
                Spacer(Modifier.height(12.dp))

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFEFFBF2))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LineStyle, contentDescription = "Rotate Motor", tint = if (feedingOn) Color(0xFF4FC3F7) else Color.Gray)
                            Spacer(Modifier.width(12.dp))
                            Text("Feeding System", fontWeight = FontWeight.Bold)
                        }
                        Switch(checked = feedingOn, onCheckedChange = { feedingOn = it })
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}
