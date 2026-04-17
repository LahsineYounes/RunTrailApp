package com.example.runtrail.feature.history.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runtrail.data.repository.RunRepository
import com.example.runtrail.domain.model.Run
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RunDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: RunRepository
) : ViewModel() {

    // runId is passed via Navigation arguments
    private val runId: String = checkNotNull(savedStateHandle["runId"])

    private val _run = MutableStateFlow<Run?>(null)
    val run: StateFlow<Run?> = _run.asStateFlow()

    init {
        viewModelScope.launch {
            _run.value = repository.getRunWithLocations(runId)
        }
    }
}