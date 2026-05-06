package com.dsafun.app.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferences: UserPreferencesDataStore
) : ViewModel() {
    
    fun completeOnboarding(userName: String, dailyGoal: Int) {
        viewModelScope.launch {
            userPreferences.setUserName(userName)
            userPreferences.setDailyGoal(dailyGoal)
            userPreferences.setOnboardingComplete(true)
        }
    }
}

// Made with Bob