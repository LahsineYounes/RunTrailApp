package com.example.runtrail.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

private val DarkColorScheme = darkColorScheme(
    primary = RunTrailBlueLight,
    onPrimary = Color(0xFF0A2A43),
    primaryContainer = RunTrailBlueDarker,
    onPrimaryContainer = RunTrailBlueLight,
    secondary = RunTrailBlueLight,
    onSecondary = Color.Black,
    background = Color(0xFF121417),
    onBackground = Color(0xFFE2E6EA),
    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E6EA),
    outline = Color(0xFF3A3F45), // Added comma
    surfaceVariant = Color(0xFF2A2F35)
)

private val LightColorScheme = lightColorScheme(
    primary = RunTrailBlue,
    onPrimary = Color.White,
    primaryContainer = RunTrailBlueLight,
    onPrimaryContainer = Color(0xFF0A2A43),
    secondary = RunTrailBlue,
    onSecondary = Color.White,
    background = Color(0xFFF7F9FC),
    onBackground = Color(0xFF1A1C1E),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1E),
    outline = Color(0xFFB0B8C1), // Added comma
    surfaceVariant = Color(0xFFE3E8EF)
)

// Define typography outside the composable to avoid recreation on recomposition
private val RunTrailTypography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal
    )
)

@Composable
fun RunTrailTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = RunTrailTypography,
        content = content
    )
}