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
import androidx.compose.material.icons.filled.Lightbulb
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

//For sensor value
@Composable
fun rememberSensorValue(deviceName: String, sensorType: String): String {
    var value by remember { mutableStateOf("...") }
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(deviceName) {
        while (true) {
            try {
                val data = RetrofitClient.api.getLatestSensorData(deviceName, sensorType)
                value = data.value.toString()
            } catch (e: Exception) {
                error = "Error!"
            }
            delay(3000) // refresh mỗi 30s
        }
    }
    return value
}
// device status
@Composable
fun rememberDeviceOnOff(deviceName: String, sensorType: String): Boolean? {
    var value by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(deviceName, sensorType) {
        try {
            val data = RetrofitClient.api.getLatestSensorData(deviceName, sensorType)
            value = data.value == 1.0
        } catch (e: Exception) {
            value = null // hoặc giữ trạng thái cũ
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
fun rememberAirQualityStatus(
    co2: String,
    nh3: String,
    alcohol: String,
    no2: String,
    benzene: String
): Pair<String, Color> {
    return remember(co2, nh3, alcohol, no2, benzene) {
        // Chuyển String sang Float (lỗi -> 0)
        val co2Value = co2.toFloatOrNull() ?: 0f
        val nh3Value = nh3.toFloatOrNull() ?: 0f
        val alcoholValue = alcohol.toFloatOrNull() ?: 0f
        val no2Value = no2.toFloatOrNull() ?: 0f
        val benzeneValue = benzene.toFloatOrNull() ?: 0f

        // Đánh giá mức độ xấu trước
        if (co2Value > 1200f || nh3Value > 50f || alcoholValue > 400f || no2Value > 0.2f || benzeneValue > 5f) {
            "Bad" to Color(0xFFD32F2F) // Đỏ
        }
        // Đánh giá mức độ trung bình
        else if (co2Value > 700f || nh3Value > 20f || alcoholValue > 200f || no2Value > 0.1f || benzeneValue > 1f) {
            "Medium" to Color(0xFFFFA000) // Cam
        }
        // Tốt
        else {
            "Good" to Color(0xFF43A047) // Xanh lá
        }
    }
}

fun formatValue(value: String?, suffix: String = ""): String =
    if (!value.isNullOrBlank()) "$value$suffix" else "--"

@Composable
fun GasLevelRow(
    label: String,
    value: String,
    unit: String,
    status: String,
    color: Color
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontWeight = FontWeight.Medium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$value $unit", color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(12.dp))
            Text(status, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

fun evaluateGasLevel(value: Float, good: Float, medium: Float): Pair<String, Color> =
    when {
        value > medium -> "Bad" to Color(0xFFD32F2F)
        value > good -> "Medium" to Color(0xFFFFA000)
        else -> "Good" to Color(0xFF43A047)
    }

@Composable
fun DashboardScreen(navController: NavController) {
    //real time
    val now = rememberCurrentTime("HH:mm:ss")
    val tankUpdated = rememberCurrentTime("HH:mm:ss")
    val systemUpdated = rememberCurrentTime("HH:mm:ss")

    //các giá trị đọc được từ backend
    val lightValue = rememberSensorValue("cam_bien_anh_sang", "light")
    val tempValue = rememberSensorValue("cam_bien_nhiet_do", "temperature")
    val soilValue = rememberSensorValue("cam_bien_do_am", "humidity")
    val airValue_co2 = rememberSensorValue("air_quality", "co2")
    val airValue_nh3 = rememberSensorValue("air_quality", "nh3")
    val airValue_no2 = rememberSensorValue("air_quality", "nox")
    val airValue_alcohol = rememberSensorValue("air_quality", "alcohol")
    val airValue_benzene = rememberSensorValue("air_quality", "benzene")

    //Device status
    val ledOn = rememberDeviceOnOff("den_led", "led")      // LED Light
    val pumpOn = rememberDeviceOnOff("may_bom", "pump")    // Irrigation pump
    val doorOpen = rememberDeviceOnOff("cua_dieu_khien", "door") // Door
    val feedingOn = rememberDeviceOnOff("feeding_device", "motor") // Feeding motor
    val fanOn = rememberDeviceOnOff("quat_gio", "fan")     // Ventilation Fan

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

    //Đánh giá tổng quát
    val (airStatus, airColor) = rememberAirQualityStatus(
        co2 = airValue_co2,
        nh3 = airValue_nh3,
        alcohol = airValue_alcohol,
        no2 = airValue_no2,
        benzene = airValue_benzene
    )

    // Chuyển đổi giá trị từ String -> Float (nếu lỗi thì về 0)
    val co2Val = airValue_co2.toFloatOrNull() ?: 0f
    val nh3Val = airValue_nh3.toFloatOrNull() ?: 0f
    val no2Val = airValue_no2.toFloatOrNull() ?: 0f
    val alcoholVal = airValue_alcohol.toFloatOrNull() ?: 0f
    val benzeneVal = airValue_benzene.toFloatOrNull() ?: 0f


    // Đánh giá từng chỉ số
    val (co2Status, co2Color) = evaluateGasLevel(co2Val, 700f, 1200f)
    val (nh3Status, nh3Color) = evaluateGasLevel(nh3Val, 20f, 50f)
    val (no2Status, no2Color) = evaluateGasLevel(no2Val, 0.1f, 0.2f)
    val (alcoholStatus, alcoholColor) = evaluateGasLevel(alcoholVal, 200f, 400f)
    val (benzeneStatus, benzeneColor) = evaluateGasLevel(benzeneVal, 1f, 5f)

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
                            //SensorValue("Air Quality", airValue_alcohol, now)
                            SensorValue("Air Quality", airStatus, now, valueColor = airColor)
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
                        SystemStatusRow(
                            "LED Light",
                            Icons.Default.Lightbulb,
                            if (ledOn == true) Color(0xFFFFD600) else Color.Gray,
                            if (ledOn == true) "ON" else "OFF",
                            systemUpdated
                        )
                        SystemStatusRow(
                            "Feeding Motor",
                            Icons.Default.Spa, // Hoặc đổi sang icon phù hợp hơn nếu muốn
                            if (feedingOn == true) Color(0xFFFFD600) else Color.Gray,
                            if (feedingOn == true) "ON" else "OFF",
                            systemUpdated
                        )
                        SystemStatusRow(
                            "Irrigation pump",
                            Icons.Default.WaterDrop,
                            if (pumpOn == true) Color(0xFFFFD600) else Color.Gray,
                            if (pumpOn == true) "ON" else "OFF",
                            systemUpdated
                        )
                        SystemStatusRow(
                            "Door Control",
                            Icons.Default.DoorFront,
                            if (doorOpen == true) Color(0xFFFFD600) else Color.Gray,
                            if (doorOpen == true) "ON" else "OFF",
                            systemUpdated
                        )
                        SystemStatusRow("Ventilation Farm",
                            Icons.Default.Air,
                            if (fanOn == true) Color(0xFFFFD600) else Color.Gray,
                            if (fanOn == true) "ON" else "OFF",
                            systemUpdated)
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
                        Text("Air Quality Details", color = Color.Gray)
                        Spacer(Modifier.height(8.dp))
                        GasLevelRow("CO₂", airValue_co2, "ppm", co2Status, co2Color)
                        GasLevelRow("NH₃", airValue_nh3, "ppm", nh3Status, nh3Color)
                        GasLevelRow("NO₂", airValue_no2, "ppm", no2Status, no2Color)
                        GasLevelRow("Alcohol", airValue_alcohol, "ppm", alcoholStatus, alcoholColor)
                        GasLevelRow("Benzene", airValue_benzene, "ppm", benzeneStatus, benzeneColor)
                        // Hiển thị tổng thể (optional):
                        Spacer(Modifier.height(8.dp))
                        Text("Overall Air Quality: $airStatus", color = airColor, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun RowScope.SensorValue(label: String, value: String, time: String, valueColor: Color = Color.Unspecified) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Text(label, color = Color.Gray, fontSize = 13.sp)
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = if (valueColor != Color.Unspecified) valueColor else Color.Black // Sử dụng màu ở đây
        )
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
