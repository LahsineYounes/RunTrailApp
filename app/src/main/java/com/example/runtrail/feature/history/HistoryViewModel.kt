package com.example.runtrail.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runtrail.domain.model.Run
import com.example.runtrail.domain.usecase.GetRunHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getRunHistory: GetRunHistoryUseCase
) : ViewModel() {

    // Directly expose the use case Flow as StateFlow.
    // Room emits a new list every time the DB changes.
    val runs: StateFlow<List<Run>> = getRunHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}