package com.example.runtrail.navigation

// Sealed class models every navigable destination.
// Arguments are encoded in the route string for type safety.
sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object ActiveRun : Screen("active_run")
    data object History   : Screen("history")
    data class RunDetail(val runId: String = "{runId}") :
        Screen("run_detail/{runId}") {
        fun createRoute(id: String) = "run_detail/$id"
    }
}