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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LineStyle
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartfarm.ChangeStatusRequest
import com.example.smartfarm.RetrofitClient
import kotlinx.coroutines.delay

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberDeviceState(deviceName: String, sensorType: String): Boolean? {
    // Sử dụng mutableStateOf để lưu trữ trạng thái có thể thay đổi
    val state = remember { mutableStateOf<Boolean?>(null) }

    // LaunchedEffect sẽ chạy khi Composable xuất hiện và chạy lại nếu key thay đổi
    // Vòng lặp while(true) bên trong đảm bảo nó liên tục cập nhật.
    LaunchedEffect(deviceName, sensorType) {
        while (true) {
            try {
                val data = RetrofitClient.api.getLatestSensorData(deviceName, sensorType)
                state.value = data.value == 1.0
            } catch (e: Exception) {
                // Nếu có lỗi, đặt state là null để giao diện biết và vô hiệu hóa switch
                state.value = null
            }
            // Đợi 5 giây trước khi lấy lại trạng thái lần nữa
            delay(5000) // 5000ms = 5 giây
        }
    }
    // Trả về giá trị state hiện tại
    return state.value
}

suspend fun setDeviceStatus(deviceName: String, sensorType: String, isOn: Boolean) {
    val body = ChangeStatusRequest(deviceName, sensorType, if (isOn) 1 else 0)
    RetrofitClient.api.changeSensorStatus(body)
}

fun saveLastUpdate(context: Context, key: String, value: String) {
    val prefs = context.getSharedPreferences("device_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString(key, value).apply()
}

fun getLastUpdate(context: Context, key: String): String {
    val prefs = context.getSharedPreferences("device_prefs", Context.MODE_PRIVATE)
    return prefs.getString(key, "--:--:--") ?: "--:--:--"
}

@Composable
fun rememberLastUpdateState(context: Context, key: String): MutableState<String> {
    return remember(key) { mutableStateOf(getLastUpdate(context, key)) }
}

@Composable
fun DeviceScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Device.route

    // Trạng thái switch (lấy từ API thực tế nếu muốn)
    val ledSwitchState = rememberDeviceState("den_led", "led")
    val pumpSwitchState = rememberDeviceState("may_bom", "pump")
    val doorSwitchState = rememberDeviceState("cung_cap_thuc_an", "servo")
    val fanSwitchState = rememberDeviceState("quat", "fan")
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    //Update time
    val ledLastUpdate = rememberLastUpdateState(context, "led_last_update")
    val pumpLastUpdate = rememberLastUpdateState(context, "pump_last_update")
    val doorLastUpdate = rememberLastUpdateState(context, "door_last_update")
    val fanLastUpdate = rememberLastUpdateState(context, "fan_last_update")

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
            // Header
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF256029))
                    .padding(vertical = 14.dp)
            ) {
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        "SmartFarm Control",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        "Remote Farming Management",
                        color = Color(0xFFD0F8CE),
                        fontSize = 13.sp
                    )
                }
            }

            // Title
            Column(Modifier.padding(horizontal = 16.dp)) {
                Spacer(Modifier.height(18.dp))
                Text("System Controls", color = Color(0xFF256029), fontWeight = FontWeight.Bold, fontSize = 21.sp)
                Text(
                    "Connected – Real-time control active",
                    color = Color(0xFF21916C),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Card 1: LED Light
                DeviceControlPanel(
                    icon = Icons.Default.Lightbulb,
                    iconColor = Color(0xFFFFD600),
                    title = "LED Light",
                    status = if (ledSwitchState == true) "ON" else "OFF",
                    powerOn = ledSwitchState == true,
                    onPowerChange = { newStatus ->
                        scope.launch {
                            setDeviceStatus("den_led", "led", newStatus)
                            // 3. Cập nhật thời gian và lưu vào SharedPreferences
                            val newTime = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()).format(Date())
                            saveLastUpdate(context, "led_last_update", newTime)
                            ledLastUpdate.value = newTime // Cập nhật UI ngay lập tức
                        }
                    },
                    intensityLabel = "Light Intensity",
                    intensityValue = 90,
                    durationLabel = "Duration",
                    durationValue = 120,
                    lastUpdate = ledLastUpdate.value
                )

                // Card 2: Irrigation System
                DeviceControlPanel(
                    icon = Icons.Default.WaterDrop,
                    iconColor = Color(0xFF4FC3F7),
                    title = "Irrigation Pump",
                    status = if (pumpSwitchState == true) "ON" else "OFF",
                    powerOn = pumpSwitchState == true,
                    onPowerChange = { newStatus ->
                        scope.launch {
                            setDeviceStatus("may_bom", "pump", newStatus)
                            val newTime = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()).format(Date())
                            saveLastUpdate(context, "pump_last_update", newTime)
                            pumpLastUpdate.value = newTime
                        }
                    },
                    intensityLabel = "Flow Intensity",
                    intensityValue = 75,
                    durationLabel = "Duration",
                    durationValue = 120,
                    lastUpdate = pumpLastUpdate.value
                )

                // Card 3: Door Control
                DeviceControlPanel(
                    icon = Icons.Default.DoorFront,
                    iconColor = Color(0xFF8D6E63),
                    title = "Door Control",
                    status = if (doorSwitchState == true) "ON" else "OFF",
                    powerOn = doorSwitchState == true,
                    onPowerChange = { newStatus ->
                        scope.launch {
                            setDeviceStatus("cung_cap_thuc_an", "servo", newStatus)
                            val newTime = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()).format(Date())
                            saveLastUpdate(context, "door_last_update", newTime)
                            doorLastUpdate.value = newTime
                        }
                    },
                    intensityLabel = "Door Duration",
                    intensityValue = 90,
                    durationLabel = "Duration",
                    durationValue = 120,
                    lastUpdate = doorLastUpdate.value
                )

                // Card 4: Ventilation Farm
                DeviceControlPanel(
                    icon = Icons.Default.Air,
                    iconColor = Color(0xFF81D4FA),
                    title = "Ventilation Farm",
                    status = if (fanSwitchState == true) "ON" else "OFF",
                    powerOn = fanSwitchState == true,
                    onPowerChange = { newStatus ->
                        scope.launch {
                            setDeviceStatus("quat", "fan", newStatus)
                            val newTime = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()).format(Date())
                            saveLastUpdate(context, "fan_last_update", newTime)
                            fanLastUpdate.value = newTime
                        }
                    },
                    intensityLabel = "Fan Speed",
                    intensityValue = 90,
                    durationLabel = "Duration",
                    durationValue = 120,
                    lastUpdate = fanLastUpdate.value
                )

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun DeviceControlPanel(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    status: String,
    powerOn: Boolean,
    onPowerChange: (Boolean) -> Unit,
    intensityLabel: String,
    intensityValue: Int,
    durationLabel: String,
    durationValue: Int,
    lastUpdate: String
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFF5FFF7))
    ) {
        Column(Modifier.padding(18.dp)) {
            // Header line: icon, title, status
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
                Box(
                    Modifier
                        .background(Color(0xFFE3F6E8), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 3.dp)
                ) {
                    Text(status, color = Color(0xFF256029), fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(13.dp))

            // Power switch
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("System Power", fontSize = 15.sp)
                Switch(checked = powerOn, onCheckedChange = onPowerChange, enabled = true)
            }
            Spacer(Modifier.height(12.dp))

            // Intensity slider (mô phỏng)
            Text(intensityLabel, fontSize = 14.sp, color = Color(0xFF3E6055))
            SliderBar(intensityValue)
            Spacer(Modifier.height(7.dp))

            // Duration
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(durationLabel, fontSize = 14.sp, color = Color(0xFF3E6055))
                Text("$durationValue min", fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(14.dp))

            // Nút bấm và thời gian
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Row {
//                    OutlinedButton(
//                        onClick = { /* Start action */ },
//                        shape = RoundedCornerShape(50),
//                        colors = ButtonDefaults.outlinedButtonColors(
//                            contentColor = Color.White,
//                            containerColor = Color(0xFF256029)
//                        ),
//                        modifier = Modifier.padding(end = 8.dp)
//                    ) {
//                        Text("Start")
//                    }
//                    OutlinedButton(
//                        onClick = { /* Emergency action */ },
//                        shape = RoundedCornerShape(50),
//                        colors = ButtonDefaults.outlinedButtonColors(
//                            contentColor = Color.White,
//                            containerColor = Color(0xFFD32F2F)
//                        )
//                    ) {
//                        Text("Emergency Stop")
//                    }
//                }
                Text("Last update: $lastUpdate", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun SliderBar(value: Int) {
    // Thanh slider giả lập (tuỳ bạn muốn code slider thực sự thì thêm state và onValueChange)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(Color(0xFFD8F2E0), RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = value / 100f)
                .height(6.dp)
                .background(Color(0xFF43A047), RoundedCornerShape(8.dp))
        )
    }
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text("$value%", fontSize = 13.sp, color = Color(0xFF256029))
    }
}

