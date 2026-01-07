package com.example.smartfarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.smartfarm.screen.AppNavHost
import com.example.smartfarm.ui.theme.SmartfarmTheme

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf

import com.example.smartfarm.screen.LightingScreen
import com.example.smartfarm.screen.SoilScreen
import com.example.smartfarm.screen.DoorScreen
import com.example.smartfarm.screen.AirScreen
import com.example.smartfarm.screen.FeedingScreen
import com.example.smartfarm.screen.DashboardScreen
import com.example.smartfarm.ui.theme.SmartfarmTheme

// Lưu log thông báo
val notificationLog = mutableStateListOf<String>()
// Lưu thời điểm thông báo cuối cùng cho từng loại cảnh báo
val lastAlertTime = mutableStateMapOf<String, Long>()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartfarmTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartfarmTheme {
        Greeting("Android")
    }
}