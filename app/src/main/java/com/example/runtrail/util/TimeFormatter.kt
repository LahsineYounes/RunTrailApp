package com.example.runtrail.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object RunFormatter {
    fun formatRunDate(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy • HH:mm", Locale.getDefault())
        return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    }

    fun formatPace(paceMinPerKm: Float): String {
        val minutes = paceMinPerKm.toInt()
        val seconds = ((paceMinPerKm - minutes) * 60).toInt()
        return "%d:%02d".format(minutes, seconds)
    }

    fun formatDistance(meters: Int): String {
        return "%.2f km".format(meters / 1000f)
    }
}