package com.example.smartfarm.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.FilterBAndW
import androidx.compose.material.icons.filled.ModeFanOff
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.*
import com.example.smartfarm.RetrofitClient
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun rememberSensorValue(deviceName: String): String {
    var value by remember { mutableStateOf("...") }
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(deviceName) {
        while (true) {
            try {
                val data = RetrofitClient.api.getLatestSensorData(deviceName)
                value = data.value.toString()
            } catch (e: Exception) {
                error = "Error!"
            }
            delay(30_000) // refresh mỗi 30s
        }
    }
    return value
}

@Composable
fun rememberCurrentTime(format: String = "HH:mm:ss"): String {
    var currentTime by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat(format, Locale.getDefault()).format(Date())
            delay(1000) // cập nhật mỗi 1 giây
        }
    }
    return currentTime
}

@Composable
fun DashboardScreen(navController: NavController) {
    val lightValue = rememberSensorValue("cam_bien_anh_sang")
    val tempValue = rememberSensorValue("cam_bien_nhiet_do")
    val soilValue = rememberSensorValue("cam_bien_do_am")
    val airValue = rememberSensorValue("cam_bien_khong_khi")

    val now = rememberCurrentTime("HH:mm:ss")
    val tankUpdated = rememberCurrentTime("HH:mm:ss")
    val systemUpdated = rememberCurrentTime("HH:mm:ss")

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

    Scaffold(
        bottomBar = { BottomNavBar(navController, currentRoute) },
        containerColor = Color(0xFFF1F8F4)
    ) { innerPadding ->
        Column(
            modifier = Modifier
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
                    Text("SmartFarm Control", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(
                        "Remote Farming Management",
                        color = Color(0xFFD0F8CE),
                        fontSize = 13.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Farm Control Dashboard", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text("Monitor and control your farming system", color = Color.Gray, fontSize = 14.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Wifi, null, tint = Color(0xFF4CAF50))
                        Text("Online", color = Color(0xFF43A047), fontSize = 13.sp)
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFEFFBF2))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Live Sensor Readings", color = Color.Gray)
                        Spacer(Modifier.height(8.dp))
                        Row {
                            SensorValue("Light Intensity", "$lightValue lux", now)
                            SensorValue("Temperature", "$tempValue °C", now)
                        }
                        Row {
                            SensorValue("Soil Moisture", "$soilValue %", now)
                            SensorValue("Air Quality", airValue, now)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFEFFBF2))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Device Status", color = Color.Gray)
                        Spacer(Modifier.height(8.dp))
                        SystemStatusRow("Irrigation pump", Icons.Default.WaterDrop, Color(0xFF43A047), "ON", systemUpdated)
                        SystemStatusRow("Door Control", Icons.Default.DoorFront, Color(0xFF8D6E63), "ON", systemUpdated)
                        SystemStatusRow("Ventilation Farm", Icons.Default.Air, Color(0xFFFFD600), "OFF", systemUpdated)
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFEFFBF2))
                ) {
                    //Sensor Trend thì tính sau
                    Column(Modifier.padding(16.dp)) {
                        Text("Sensor Trend", color = Color.Gray)
                        TankLevelBar("Water Tank", 50, tankUpdated, Color(0xFF43A047))
                        TankLevelBar("Herbicide Tank", 45, tankUpdated, Color(0xFF388E3C))
                        TankLevelBar("Pesticide Tank", 72, tankUpdated, Color(0xFFFFD600))
                    }
                }

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun RowScope.SensorValue(label: String, value: String, time: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Text(label, color = Color.Gray, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(time, color = Color(0xFFA0A0A0), fontSize = 12.sp)
    }
}

@Composable
fun SystemStatusRow(name: String, icon: ImageVector, color: Color, status: String, updated: String) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = name, tint = color, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Column {
                Text(name, fontWeight = FontWeight.SemiBold)
                Text("Last update: $updated", color = Color.Gray, fontSize = 12.sp)
            }
        }
        OutlinedCard(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(28.dp)
        ) {
            Box(
                Modifier
                    .padding(horizontal = 13.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(status, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun TankLevelBar(name: String, percent: Int, updated: String, color: Color) {
    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontWeight = FontWeight.Medium)
            Text("$percent%", fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .padding(vertical = 2.dp),
            color = color,
            trackColor = Color(0xFFDADADA)
        )
        Text(
            "Updated: $updated",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 2.dp
    ) {
        Screen.bottomBarItems.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Dashboard.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    screen.icon?.let { icon ->
                        Icon(icon, contentDescription = screen.title)
                    }
                },
                label = { Text(screen.title) }
            )
        }
    }
}
