package com.example.runtrail.feature.tracker

import android.R.attr.action
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runtrail.data.repository.RunRepository
import com.example.runtrail.domain.model.RunState
import com.example.runtrail.domain.usecase.FormatTimeUseCase
import com.example.runtrail.service.RunTrackingService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ActiveRunViewModel @Inject constructor(
    private val repository: RunRepository,
    private val formatTime: FormatTimeUseCase
) : ViewModel() {

    // Observe directly from the Service's companion StateFlow
    val runState: StateFlow<RunState> = RunTrackingService.runState

    // Derived UI state — transformations done here, not in Compose
    val formattedTime: StateFlow<String> = runState
        .map { state ->
            when (state) {
                is RunState.Running -> formatTime(state.durationMillis)
                is RunState.Paused  -> formatTime(state.durationMillis)
                else -> "00:00"
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "00:00")

    val distanceKm: StateFlow<String> = runState
        .map { state ->
            val meters = when (state) {
                is RunState.Running -> state.distanceMeters
                is RunState.Paused  -> state.distanceMeters
                else -> 0f
            }
            "${"%.2f".format(meters / 1000f)} km"
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "0.00 km")

    // Called when user taps STOP — saves run to DB
    fun finishRun(context: Context) {
        context.startService(
            Intent(context, RunTrackingService::class.java).apply {
                action = RunTrackingService.ACTION_STOP
            }
        )
        // Observe Finished state and persist
        viewModelScope.launch {
            runState.filterIsInstance<RunState.Finished>().first().let { finished ->
                repository.saveRun(
                    run = finished.run,
                    locationPoints = finished.run.locationPoints
                )
            }
        }
    }

    fun pauseRun(context: Context) {
        context.startService(
            Intent(context, RunTrackingService::class.java).apply {
                action = RunTrackingService.ACTION_PAUSE
            }
        )
    }

    fun resumeRun(context: Context) {
        context.startService(
            Intent(context, RunTrackingService::class.java).apply {
                action = RunTrackingService.ACTION_RESUME
            }
        )
    }
}