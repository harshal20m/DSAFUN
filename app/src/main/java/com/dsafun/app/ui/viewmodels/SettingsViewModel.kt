package com.dsafun.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsafun.app.data.repository.PreferencesRepository
import com.dsafun.app.workers.WorkerScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val userName: String = "",
    val dailyGoal: Int = 1,
    val isDarkTheme: Boolean = false,
    val appTheme: String = "DARK",
    val preferredLanguage: String = "PYTHON",
    val fontSizePreference: String = "MEDIUM",
    val reminderEnabled: Boolean = true,
    val reminderHour: Int = 9,
    val reminderMinute: Int = 0,
    val streakWarningEnabled: Boolean = true,
    val weeklySummaryEnabled: Boolean = true,
    val totalProblemsSolved: Int = 0,
    val currentStreak: Int = 0,
    val totalXp: Int = 0,
    val currentLevel: Int = 1,
    val appVersion: String = "1.0.0"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val workerScheduler: WorkerScheduler
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        preferencesRepository.userName,
        preferencesRepository.dailyGoal,
        preferencesRepository.isDarkTheme,
        preferencesRepository.appTheme,
        preferencesRepository.preferredLanguage,
        preferencesRepository.fontSizePreference,
        preferencesRepository.reminderEnabled,
        preferencesRepository.reminderHour,
        preferencesRepository.reminderMinute,
        preferencesRepository.streakWarningEnabled,
        preferencesRepository.weeklySummaryEnabled,
        preferencesRepository.totalProblemsSolved,
        preferencesRepository.currentStreak,
        preferencesRepository.totalXp,
        preferencesRepository.currentLevel
    ) { values ->
        SettingsUiState(
            userName = values[0] as String,
            dailyGoal = values[1] as Int,
            isDarkTheme = values[2] as Boolean,
            appTheme = values[3] as String,
            preferredLanguage = values[4] as String,
            fontSizePreference = values[5] as String,
            reminderEnabled = values[6] as Boolean,
            reminderHour = values[7] as Int,
            reminderMinute = values[8] as Int,
            streakWarningEnabled = values[9] as Boolean,
            weeklySummaryEnabled = values[10] as Boolean,
            totalProblemsSolved = values[11] as Int,
            currentStreak = values[12] as Int,
            totalXp = values[13] as Int,
            currentLevel = values[14] as Int
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun updateUserName(name: String) {
        viewModelScope.launch {
            preferencesRepository.setUserName(name)
        }
    }

    fun updateDailyGoal(goal: Int) {
        viewModelScope.launch {
            preferencesRepository.setDailyGoal(goal)
        }
    }

    fun updateTheme(isDark: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setDarkTheme(isDark)
        }
    }
    
    fun updateAppTheme(theme: String) {
        viewModelScope.launch {
            preferencesRepository.setAppTheme(theme)
        }
    }

    fun updatePreferredLanguage(language: String) {
        viewModelScope.launch {
            preferencesRepository.setPreferredLanguage(language)
        }
    }

    fun updateFontSize(size: String) {
        viewModelScope.launch {
            preferencesRepository.setFontSizePreference(size)
        }
    }

    fun updateReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setReminderEnabled(enabled)
            if (enabled) {
                val state = uiState.value
                workerScheduler.scheduleDailyReminder(state.reminderHour, state.reminderMinute)
            } else {
                workerScheduler.cancelDailyReminder()
            }
        }
    }

    fun updateReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            preferencesRepository.setReminderTime(hour, minute)
            if (uiState.value.reminderEnabled) {
                workerScheduler.rescheduleReminder(hour, minute)
            }
        }
    }

    fun updateStreakWarningEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setStreakWarningEnabled(enabled)
            if (enabled) {
                workerScheduler.scheduleStreakGuard()
            } else {
                workerScheduler.cancelStreakGuard()
            }
        }
    }

    fun updateWeeklySummaryEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setWeeklySummaryEnabled(enabled)
            if (enabled) {
                workerScheduler.scheduleWeeklySummary()
            } else {
                workerScheduler.cancelWeeklySummary()
            }
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            preferencesRepository.resetStreak()
            // Note: We don't reset total problems solved or XP
            // Only streak is reset as a "soft reset"
        }
    }
}

// Made with Bob