package com.example.runtrail.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.runtrail.R
import com.example.runtrail.domain.model.Run
import com.example.runtrail.ui.components.RunSummaryCard
import com.example.runtrail.ui.components.WeeklyDistanceChart
import com.example.runtrail.ui.theme.RunTrailBlack
import com.example.runtrail.ui.theme.RunTrailBlue
import com.example.runtrail.ui.theme.RunTrailLightGray
import com.example.runtrail.ui.theme.RunTrailWhite

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onStartRun: () -> Unit,
    onRunClick: (String) -> Unit
) {
    val recentRuns by viewModel.recentRuns.collectAsStateWithLifecycle()
    val weeklyDistanceKm by viewModel.weeklyDistanceKm.collectAsStateWithLifecycle()

    DashboardContent(
        recentRuns = recentRuns,
        weeklyDistanceKm = weeklyDistanceKm,
        onStartRun = onStartRun,
        onRunClick = onRunClick
    )
}

@Composable
fun DashboardContent(
    recentRuns: List<Run>,
    weeklyDistanceKm: List<Pair<String, Float>>,
    onStartRun: () -> Unit,
    onRunClick: (String) -> Unit
) {
    Scaffold(
        containerColor = RunTrailBlack,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onStartRun,
                containerColor = RunTrailBlue,
                icon = { Icon(Icons.Default.PlayArrow, null, tint = Color.White) },
                text = {
                    Text(
                        text = stringResource(R.string.start_run),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = RunTrailWhite,
                        fontWeight = FontWeight.Black
                    )
                )
                Spacer(Modifier.height(16.dp))
            }

            // Weekly distance bar chart
            item {
                WeeklyDistanceChart(
                    weeklyDistanceKm = weeklyDistanceKm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }

            if (recentRuns.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.recent_runs),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = RunTrailLightGray,
                            letterSpacing = 3.sp
                        ),
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }

                items(recentRuns, key = { it.id }) { run ->
                    RunSummaryCard(run = run, onClick = { onRunClick(run.id) })
                }
            } else {
                item {
                    EmptyRecentRunsState()
                }
            }
            
            // Add extra space at bottom so FAB doesn't cover last item
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun EmptyRecentRunsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_recent_runs),
            color = RunTrailLightGray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}