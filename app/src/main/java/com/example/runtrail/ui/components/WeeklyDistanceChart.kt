package com.example.runtrail.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.runtrail.ui.theme.RunTrailBlue
import com.example.runtrail.ui.theme.RunTrailLightGray

@Composable
fun WeeklyDistanceChart(
    weeklyDistanceKm: List<Pair<String, Float>>,  // ("Mon", 5.2f)
    modifier: Modifier = Modifier
) {
    val maxDistance = weeklyDistanceKm.maxOfOrNull { it.second } ?: 1f
    val barColor = RunTrailBlue
    val labelColor = RunTrailLightGray

    Canvas(modifier = modifier) {
        val barWidth = size.width / (weeklyDistanceKm.size * 2f)
        val maxBarHeight = size.height * 0.7f
        val labelHeight = size.height * 0.15f
        val bottomPadding = labelHeight + 8.dp.toPx()

        weeklyDistanceKm.forEachIndexed { i, (day, km) ->
            val x = i * (size.width / weeklyDistanceKm.size) + barWidth / 2
            val barHeight = if (maxDistance > 0) (km / maxDistance) * maxBarHeight else 0f

            // Bar
            drawRoundRect(
                color = if (km > 0) barColor else barColor.copy(alpha = 0.2f),
                topLeft = Offset(x, size.height - bottomPadding - barHeight),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx())
            )

            // Day label below bar — drawn via drawContext
            drawContext.canvas.nativeCanvas.drawText(
                day,
                x + barWidth / 2,
                size.height - 4.dp.toPx(),
                Paint().apply {
                    color = labelColor.toArgb()
                    textSize = 10.sp.toPx()
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
            )
        }
    }
}