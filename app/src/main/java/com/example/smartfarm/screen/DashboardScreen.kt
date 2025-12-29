package com.example.smartfarm.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("SMART FARM DASHBOARD", style = MaterialTheme.typography.headlineSmall)
        // Các Card tổng quan hoặc nút điều hướng tới từng màn hình trên
    }
}
