package com.example.runtrail.domain.usecase

import com.example.runtrail.data.repository.RunRepository
import com.example.runtrail.domain.model.Run
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetRunHistoryUseCase @Inject constructor(
    private val repository: RunRepository
) {
    operator fun invoke(): Flow<List<Run>> = repository.getAllRuns()
}