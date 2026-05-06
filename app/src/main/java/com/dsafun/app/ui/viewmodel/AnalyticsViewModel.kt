package com.dsafun.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsafun.app.domain.usecase.AnalyticsData
import com.dsafun.app.domain.usecase.GetAnalyticsDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AnalyticsUiState {
    object Loading : AnalyticsUiState()
    data class Success(val data: AnalyticsData) : AnalyticsUiState()
    data class Error(val message: String) : AnalyticsUiState()
}

enum class TimeRange {
    ONE_MONTH,
    THREE_MONTHS,
    ALL
}

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getAnalyticsDataUseCase: GetAnalyticsDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnalyticsUiState>(AnalyticsUiState.Loading)
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    private val _selectedTimeRange = MutableStateFlow(TimeRange.ALL)
    val selectedTimeRange: StateFlow<TimeRange> = _selectedTimeRange.asStateFlow()

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            try {
                _uiState.value = AnalyticsUiState.Loading
                getAnalyticsDataUseCase.invoke().collect { data ->
                    _uiState.value = AnalyticsUiState.Success(data)
                }
            } catch (e: Exception) {
                _uiState.value = AnalyticsUiState.Error(
                    e.message ?: "Failed to load analytics"
                )
            }
        }
    }

    fun selectTimeRange(range: TimeRange) {
        _selectedTimeRange.value = range
    }

    fun getFilteredWeeklyData(data: AnalyticsData): AnalyticsData {
        val filteredWeeks = when (_selectedTimeRange.value) {
            TimeRange.ONE_MONTH -> data.weeklyProgress.takeLast(4)
            TimeRange.THREE_MONTHS -> data.weeklyProgress.takeLast(12)
            TimeRange.ALL -> data.weeklyProgress
        }
        return data.copy(weeklyProgress = filteredWeeks)
    }
}

// Made with Bob