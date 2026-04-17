package com.example.runtrail.domain.usecase

import jakarta.inject.Inject

// Pure function — no Android dependencies, trivially unit-testable
class CalculatePaceUseCase @Inject constructor() {

    /**
     * Calculates pace in minutes-per-kilometer.
     *
     * @param distanceMeters Total distance covered
     * @param durationMillis Total elapsed time
     * @return Pace as min/km, or 0f if not enough data
     */
    operator fun invoke(distanceMeters: Float, durationMillis: Long): Float {
        if (distanceMeters < 10f || durationMillis <= 0L) return 0f
        val durationMinutes = durationMillis / 1000f / 60f
        val distanceKm = distanceMeters / 1000f
        return durationMinutes / distanceKm
    }
}