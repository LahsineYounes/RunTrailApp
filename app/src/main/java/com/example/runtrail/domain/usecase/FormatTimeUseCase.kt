package com.example.runtrail.domain.usecase

import jakarta.inject.Inject

class FormatTimeUseCase @Inject constructor() {

    /**
     * Converts milliseconds to "HH:MM:SS" display string.
     */
    operator fun invoke(millis: Long): String {
        val totalSeconds = millis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }
    }
}