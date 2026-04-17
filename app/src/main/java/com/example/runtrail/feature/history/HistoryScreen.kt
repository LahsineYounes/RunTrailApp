package com.example.runtrail.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.runtrail.R
import com.example.runtrail.domain.model.Run
import com.example.runtrail.ui.components.RunSummaryCard
import com.example.runtrail.ui.theme.RunTrailBlack
import com.example.runtrail.ui.theme.RunTrailWhite

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onRunClick: (String) -> Unit
) {
    val runs by viewModel.runs.collectAsStateWithLifecycle()

    HistoryContent(
        runs = runs,
        onBackClick = onBackClick,
        onRunClick = onRunClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(
    runs: List<Run>,
    onBackClick: () -> Unit,
    onRunClick: (String) -> Unit
) {
    Scaffold(
        containerColor = RunTrailBlack,
        topBar = {
            TopAppBar(
                title = { Text("Run History", color = RunTrailWhite) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(runs, key = { it.id }) { run ->
                RunSummaryCard(
                    run = run,
                    onClick = { onRunClick(run.id) }
                )
            }
        }
    }
}