package com.example.smartfarm.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun DataScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Data.route

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

            // --- Nội dung chính (placeholder) ---
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(18.dp))
                Text("Historical Data", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color(0xFF256029))
                Spacer(Modifier.height(10.dp))
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFEFFBF2))
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sensor data chart or logs will appear here.", color = Color.Gray)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
