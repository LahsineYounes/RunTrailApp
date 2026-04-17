package com.example.runtrail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.runtrail.navigation.RunTrailNavGraph
import com.example.runtrail.navigation.Screen
import com.example.runtrail.ui.theme.RunTrailDarkGray
import com.example.runtrail.ui.theme.RunTrailTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RunTrailTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        RunTrailBottomNav(navController)
                    }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        RunTrailNavGraph(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun RunTrailBottomNav(navController: NavHostController) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    NavigationBar(containerColor = RunTrailDarkGray) {
        NavigationBarItem(
            selected = currentRoute == Screen.Dashboard.route,
            onClick = { navController.navigate(Screen.Dashboard.route) },
            icon = { Icon(Icons.Default.Home, "Dashboard") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == Screen.History.route,
            onClick = { navController.navigate(Screen.History.route) },
            icon = { Icon(Icons.Default.List, "History") },
            label = { Text("History") }
        )
    }
}