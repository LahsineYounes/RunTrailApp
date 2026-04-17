package com.example.runtrail.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.runtrail.feature.dashboard.DashboardScreen
import com.example.runtrail.feature.history.HistoryScreen
import com.example.runtrail.feature.history.detail.RunDetailScreen
import com.example.runtrail.feature.tracker.ActiveRunScreen

@Composable
fun RunTrailNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onStartRun = {
                    navController.navigate(Screen.ActiveRun.route)
                },
                onRunClick = { runId ->
                    navController.navigate(Screen.RunDetail().createRoute(runId))
                }
            )
        }

        composable(Screen.ActiveRun.route) {
            ActiveRunScreen(
                onRunFinished = {
                    // Pop back to Dashboard — remove ActiveRun from back stack
                    navController.popBackStack(Screen.Dashboard.route, inclusive = false)
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() },
                onRunClick = { runId ->
                    navController.navigate(Screen.RunDetail().createRoute(runId))
                }
            )
        }

        composable(
            route = Screen.RunDetail().route,
            arguments = listOf(navArgument("runId") { type = NavType.StringType })
        ) {
            RunDetailScreen(onBackClick = { navController.popBackStack() })
        }
    }
}