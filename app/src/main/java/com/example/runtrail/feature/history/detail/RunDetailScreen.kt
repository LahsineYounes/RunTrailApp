package com.example.runtrail.feature.history.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.runtrail.domain.model.Run
import com.example.runtrail.ui.theme.RunTrailBlack
import com.example.runtrail.ui.theme.RunTrailBlue
import com.example.runtrail.ui.theme.RunTrailDarkGray
import com.example.runtrail.ui.theme.RunTrailLightGray
import com.example.runtrail.ui.theme.RunTrailWhite
import com.example.runtrail.util.RunFormatter

@Composable
fun RunDetailScreen(
    viewModel: RunDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val run by viewModel.run.collectAsStateWithLifecycle()

    RunDetailContent(
        run = run,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunDetailContent(
    run: Run?,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = RunTrailBlack,
        topBar = {
            TopAppBar(
                title = { Text("Run Details", color = RunTrailWhite) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = RunTrailWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RunTrailBlack
                )
            )
        }
    ) { padding ->
        if (run == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RunTrailBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Map Snapshot
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(RunTrailDarkGray)
                ) {
                    if (run.mapSnapshotUri != null) {
                        AsyncImage(
                            model = run.mapSnapshotUri,
                            contentDescription = "Run Map",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            "No map data available",
                            modifier = Modifier.align(Alignment.Center),
                            color = RunTrailLightGray
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Date & Time
                Text(
                    text = RunFormatter.formatRunDate(run.timestamp),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = RunTrailWhite,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.height(24.dp))

                // Stats Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        label = "DISTANCE",
                        value = RunFormatter.formatDistance(run.totalDistanceMeters),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "PACE",
                        value = "${RunFormatter.formatPace(run.averagePaceMinPerKm)} /km",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        label = "DURATION",
                        value = formatDuration(run.durationInMillis),
                        modifier = Modifier.weight(1f)
                    )
                    // You could add calories here if available in the model
                    StatCard(
                        label = "STEPS",
                        value = "--", 
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = RunTrailDarkGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = RunTrailLightGray,
                    letterSpacing = 1.sp
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = RunTrailWhite,
                    fontWeight = FontWeight.Black
                )
            )
        }
    }
}

private fun formatDuration(millis: Long): String {
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