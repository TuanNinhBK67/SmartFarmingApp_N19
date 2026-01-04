package com.example.smartfarm.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
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
fun NotificationScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Notification.route

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
                        "Notifications",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        "All system warnings & alerts",
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
                Text("Alerts & Warnings", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color(0xFF256029))
                Spacer(Modifier.height(10.dp))

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFEFFBF2))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFFFF9800))
                        Spacer(Modifier.width(10.dp))
                        Text("Soil moisture below threshold! (10:20:32)", fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(Modifier.height(12.dp))
                // Thêm các card cảnh báo khác tại đây
            }
        }
    }
}
