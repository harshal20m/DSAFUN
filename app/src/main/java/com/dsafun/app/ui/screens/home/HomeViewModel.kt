package com.dsafun.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsafun.app.domain.usecase.GetHomeStatsUseCase
import com.dsafun.app.domain.usecase.HomeStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for Home screen
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    getHomeStatsUseCase: GetHomeStatsUseCase
) : ViewModel() {
    
    val homeStats: StateFlow<HomeStats?> = getHomeStatsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}

// Made with Bob