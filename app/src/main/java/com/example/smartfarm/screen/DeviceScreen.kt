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
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LineStyle
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.smartfarm.RetrofitClient
import kotlinx.coroutines.delay

/**
 * Composable này sẽ liên tục lấy trạng thái Bật/Tắt của một thiết bị.
 * Nó trả về:
 * - true: nếu thiết bị đang Bật (value == 1.0)
 * - false: nếu thiết bị đang Tắt
 * - null: nếu đang tải hoặc có lỗi xảy ra.
 */
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


@Composable
fun DeviceScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Device.route

    // Sử dụng hàm đã sửa để lấy trạng thái
    val ledSwitchState = rememberDeviceState("den_led", "led")
    val pumpSwitchState = rememberDeviceState("may_bom", "pump")
    val doorSwitchState = rememberDeviceState("cua_dieu_khien", "door")
    val fanSwitchState = rememberDeviceState("quat_gio", "fan")
    val feedingSwitchState = rememberDeviceState("feeding_device", "motor")

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

                // === Thẻ điều khiển được tái cấu trúc để dễ quản lý ===
                DeviceControlCard(
                    label = "LED Light",
                    icon = Icons.Default.Lightbulb,
                    switchState = ledSwitchState,
                    activeColor = Color(0xFFFFD600),
                    onCheckedChange = { /* Logic điều khiển sẽ thêm ở đây */ }
                )

                DeviceControlCard(
                    label = "Irrigation Pump",
                    icon = Icons.Default.WaterDrop,
                    switchState = pumpSwitchState, // Đã sửa
                    activeColor = Color(0xFF43A047),
                    onCheckedChange = { /* Logic điều khiển sẽ thêm ở đây */ }
                )

                DeviceControlCard(
                    label = "Automatic Door",
                    icon = Icons.Default.DoorFront,
                    switchState = doorSwitchState,
                    activeColor = Color(0xFF388E3C),
                    onCheckedChange = { /* Logic điều khiển sẽ thêm ở đây */ }
                )

                DeviceControlCard(
                    label = "Ventilation Fan",
                    icon = Icons.Default.Air,
                    switchState = fanSwitchState,
                    activeColor = Color(0xFF4FC3F7),
                    onCheckedChange = { /* Logic điều khiển sẽ thêm ở đây */ }
                )

                DeviceControlCard(
                    label = "Feeding System",
                    icon = Icons.Default.LineStyle,
                    switchState = feedingSwitchState,
                    activeColor = Color(0xFF4FC3F7),
                    onCheckedChange = { /* Logic điều khiển sẽ thêm ở đây */ }
                )

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

/**
 * Composable tái sử dụng để hiển thị một thẻ điều khiển thiết bị.
 * Giúp mã nguồn gọn gàng và dễ bảo trì hơn.
 */
@Composable
private fun DeviceControlCard(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    switchState: Boolean?, // null: loading/error, true: on, false: off
    activeColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp), // Thêm padding để các thẻ không dính vào nhau
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
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (switchState == true) activeColor else Color.Gray
                )
                Spacer(Modifier.width(12.dp))
                Text(label, fontWeight = FontWeight.Bold)
            }
            Switch(
                checked = switchState ?: false,
                // Vô hiệu hóa Switch nếu trạng thái là null (đang tải hoặc lỗi)
                enabled = switchState != null,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
