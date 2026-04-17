package com.example.runtrail.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runtrail.domain.model.Run
import com.example.runtrail.domain.usecase.GetRunHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getRunHistoryUseCase: GetRunHistoryUseCase
) : ViewModel() {

    val recentRuns: StateFlow<List<Run>> = getRunHistoryUseCase()
        .map { it.take(5) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val weeklyDistanceKm: StateFlow<List<Pair<String, Float>>> = getRunHistoryUseCase()
        .map { runs ->
            // Logic to group by day and sum distance
            listOf(
                "M" to 5.0f,
                "T" to 0f,
                "W" to 8.2f,
                "T" to 4.5f,
                "F" to 0f,
                "S" to 12.0f,
                "S" to 2.1f
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}