package com.example.runtrail.feature.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.runtrail.domain.model.RunState
import com.example.runtrail.feature.tracker.composables.LiveMapView
import com.example.runtrail.ui.theme.RunTrailBlack
import com.example.runtrail.ui.theme.RunTrailBlue
import com.example.runtrail.ui.theme.RunTrailGray
import com.example.runtrail.ui.theme.RunTrailLightGray
import com.example.runtrail.ui.theme.RunTrailRed
import com.example.runtrail.ui.theme.RunTrailWhite
import com.example.runtrail.util.RunFormatter.formatPace

@Composable
fun ActiveRunScreen(
    viewModel: ActiveRunViewModel = hiltViewModel(),
    onRunFinished: () -> Unit
) {
    val context = LocalContext.current
    val runState by viewModel.runState.collectAsStateWithLifecycle()
    val formattedTime by viewModel.formattedTime.collectAsStateWithLifecycle()
    val distanceKm by viewModel.distanceKm.collectAsStateWithLifecycle()

    // Navigate away when run completes
    LaunchedEffect(runState) {
        if (runState is RunState.Finished) onRunFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RunTrailBlack)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        // Live Map View
        val locationPoints = when (val s = runState) {
            is RunState.Running -> s.locationPoints
            is RunState.Paused  -> s.locationPoints
            else -> emptyList()
        }
        LiveMapView(
            locationPoints = locationPoints,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(32.dp))

        // Giant timer — designed for glanceability mid-run
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 72.sp,
                fontWeight = FontWeight.Black,
                color = RunTrailWhite,
                letterSpacing = (-2).sp
            )
        )

        Text(
            text = "DURATION",
            style = MaterialTheme.typography.labelSmall.copy(
                color = RunTrailLightGray,
                letterSpacing = 3.sp
            )
        )

        Spacer(Modifier.height(24.dp))

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatDisplay(label = "DISTANCE", value = distanceKm)
            StatDisplay(
                label = "PACE",
                value = when (val s = runState) {
                    is RunState.Running -> formatPace(s.currentPaceMinPerKm)
                    else -> "--:--"
                }
            )
        }

        Spacer(Modifier.weight(1f))

        // Control FABs
        RunControlButtons(
            runState = runState,
            onPause = { viewModel.pauseRun(context) },
            onResume = { viewModel.resumeRun(context) },
            onStop = { viewModel.finishRun(context) }
        )

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun StatDisplay(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = RunTrailWhite,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = RunTrailLightGray,
                letterSpacing = 2.sp
            )
        )
    }
}

@Composable
private fun RunControlButtons(
    runState: RunState,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (runState) {
            is RunState.Running -> {
                // Stop button
                FloatingActionButton(
                    onClick = onStop,
                    containerColor = RunTrailRed,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(Icons.Default.Stop, "Stop", tint = Color.White)
                }
                // Pause button
                FloatingActionButton(
                    onClick = onPause,
                    containerColor = RunTrailGray,
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(Icons.Default.Pause, "Pause", tint = Color.White,
                        modifier = Modifier.size(36.dp))
                }
            }
            is RunState.Paused -> {
                // Stop button
                FloatingActionButton(
                    onClick = onStop,
                    containerColor = RunTrailRed,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(Icons.Default.Stop, "Stop", tint = Color.White)
                }
                // Resume button
                FloatingActionButton(
                    onClick = onResume,
                    containerColor = RunTrailBlue,
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, "Resume", tint = Color.White,
                        modifier = Modifier.size(36.dp))
                }
            }
            else -> Unit
        }
    }
}