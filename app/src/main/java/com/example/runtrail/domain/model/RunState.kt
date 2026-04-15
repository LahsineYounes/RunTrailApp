package com.example.runtrail.domain.model

// Sealed interface models every possible state of an active run session.
// The UI renders exclusively based on this state — no boolean flags needed.
sealed interface RunState {
    data object Idle : RunState
    data class Running(
        val durationMillis: Long,
        val distanceMeters: Float,
        val currentPaceMinPerKm: Float,
        val locationPoints: List<LocationPoint>
    ) : RunState
    data class Paused(
        val durationMillis: Long,
        val distanceMeters: Float,
        val locationPoints: List<LocationPoint>
    ) : RunState
    data class Finished(val run: Run) : RunState
}