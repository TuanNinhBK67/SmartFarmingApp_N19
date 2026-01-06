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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.smartfarm.RetrofitClient
import com.example.smartfarm.SensorHistoryData

@Composable
fun rememberSensorHistory(deviceName: String, sensorType: String): List<SensorHistoryData>? {
    var history by remember { mutableStateOf<List<SensorHistoryData>?>(null) }

    LaunchedEffect(deviceName, sensorType) {
        try {
            history = RetrofitClient.api.getSensorHistory(deviceName, sensorType)
        } catch (e: Exception) {
            println("Failed to fetch history for $deviceName/$sensorType: ${e.message}")
            history = emptyList() // Set to empty on error
        }
    }
    return history
}

@Composable
fun SensorHistoryList(
    history: List<SensorHistoryData>?,
    title: String,
    valueUnit: String
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp)
    ) {
        Text(
            title,
            color = Color(0xFF256029),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(6.dp))
        if (history == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (history.isEmpty()) {
            Text("No data available.", color = Color.Gray)
        } else {
            // Show max 30 latest records
            history.takeLast(30).reversed().forEach { item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val timeStr = item.timestamp?.replace("T", " ")?.take(19) ?: ""
                    Text(timeStr, fontSize = 14.sp, color = Color(0xFF1976D2))
                    Text("${item.value} $valueUnit", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Divider()
            }
        }
    }
}


@Composable
fun DataScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Data.route

    // Fetching data for all sensors
    val soilHistory = rememberSensorHistory("cam_bien_do_am", "humidity")
    val tempHistory = rememberSensorHistory("cam_bien_nhiet_do", "temperature")
    val lightHistory = rememberSensorHistory("cam_bien_anh_sang", "light")
    val airquality_CO2 = rememberSensorHistory("air_quality", "co2")
    val airquality_NH3 = rememberSensorHistory("air_quality", "nh3")
    val airquality_NO2 = rememberSensorHistory("air_quality", "nox")
    val airquality_Alcohol = rememberSensorHistory("air_quality", "alcohol")
    val airquality_Benzene = rememberSensorHistory("air_quality", "benzene")

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
            // --- Header ---
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF256029))
                    .padding(vertical = 14.dp)
            ) {
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        "Data History",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        "View sensor and device logs",
                        color = Color(0xFFD0F8CE),
                        fontSize = 13.sp
                    )
                }
            }

            // --- Main Content ---
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(18.dp))
                Text("Historical Data", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color(0xFF256029))
                Spacer(Modifier.height(10.dp))

                // Soil Moisture Card
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    SensorHistoryList(
                        history = soilHistory,
                        title = "Soil Moisture History",
                        valueUnit = "%"
                    )
                }
                Spacer(Modifier.height(16.dp))

                // Temperature Card
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    SensorHistoryList(
                        history = tempHistory,
                        title = "Temperature History",
                        valueUnit = "°C"
                    )
                }
                Spacer(Modifier.height(16.dp))

                // Light Intensity Card
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    SensorHistoryList(
                        history = lightHistory,
                        title = "Light Intensity History",
                        valueUnit = "lux"
                    )
                }
                Spacer(Modifier.height(24.dp))

                //CO2
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    SensorHistoryList(
                        history = airquality_CO2,
                        title = "CO2 Intensity History",
                        valueUnit = "lux"
                    )
                }
                Spacer(Modifier.height(24.dp))

                //NH3
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    SensorHistoryList(
                        history = airquality_NH3,
                        title = "NH3 Intensity History",
                        valueUnit = "lux"
                    )
                }
                Spacer(Modifier.height(24.dp))

                //NO2
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    SensorHistoryList(
                        history = airquality_NO2,
                        title = "NO2 Intensity History",
                        valueUnit = "lux"
                    )
                }
                Spacer(Modifier.height(24.dp))

                //Benzene
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    SensorHistoryList(
                        history = airquality_Benzene,
                        title = "Benzene Intensity History",
                        valueUnit = "lux"
                    )
                }
                Spacer(Modifier.height(24.dp))

                //Alcohol
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    SensorHistoryList(
                        history = airquality_Alcohol,
                        title = "Alcohol Intensity History",
                        valueUnit = "lux"
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
