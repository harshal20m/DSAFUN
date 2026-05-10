package com.dsafun.app.data.repository

import com.dsafun.app.data.local.datastore.LevelUpEvent
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import com.dsafun.app.data.local.datastore.UserStats
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for user preferences and progress
 */
@Singleton
class PreferencesRepository @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) {
    
    // User Profile
    val userName: Flow<String> = dataStore.userName
    val onboardingComplete: Flow<Boolean> = dataStore.onboardingComplete
    
    suspend fun setUserName(name: String) = dataStore.setUserName(name)
    suspend fun setOnboardingComplete(complete: Boolean) = dataStore.setOnboardingComplete(complete)
    
    // XP and Level
    val totalXp: Flow<Int> = dataStore.totalXp
    val currentLevel: Flow<Int> = dataStore.currentLevel
    
    suspend fun awardXp(xp: Int): LevelUpEvent? = dataStore.awardXp(xp)
    
    fun getXpForNextLevel(currentXp: Int, currentLevel: Int): Int =
        dataStore.getXpForNextLevel(currentXp, currentLevel)
    
    // Streak
    val currentStreak: Flow<Int> = dataStore.currentStreak
    val longestStreak: Flow<Int> = dataStore.longestStreak
    
    suspend fun updateStreak() = dataStore.updateStreak()
    suspend fun resetStreak() = dataStore.resetStreak()
    
    // Statistics
    val totalProblemsSolved: Flow<Int> = dataStore.totalProblemsSolved
    val totalTimeSpentMinutes: Flow<Int> = dataStore.totalTimeSpentMinutes
    
    suspend fun incrementProblemsSolved() = dataStore.incrementProblemsSolved()
    suspend fun addTimeSpent(minutes: Int) = dataStore.addTimeSpent(minutes)
    
    // Settings
    val dailyGoal: Flow<Int> = dataStore.dailyGoal
    val isDarkTheme: Flow<Boolean> = dataStore.isDarkTheme
    val appTheme: Flow<String> = dataStore.appTheme
    val preferredLanguage: Flow<String> = dataStore.preferredLanguage
    val fontSizePreference: Flow<String> = dataStore.fontSizePreference
    
    suspend fun setDailyGoal(goal: Int) = dataStore.setDailyGoal(goal)
    suspend fun setDarkTheme(isDark: Boolean) = dataStore.setDarkTheme(isDark)
    suspend fun setAppTheme(theme: String) = dataStore.setAppTheme(theme)
    suspend fun setPreferredLanguage(language: String) = dataStore.setPreferredLanguage(language)
    suspend fun setFontSizePreference(size: String) = dataStore.setFontSizePreference(size)
    
    // Notifications
    val reminderEnabled: Flow<Boolean> = dataStore.reminderEnabled
    val reminderHour: Flow<Int> = dataStore.reminderHour
    val reminderMinute: Flow<Int> = dataStore.reminderMinute
    val streakWarningEnabled: Flow<Boolean> = dataStore.streakWarningEnabled
    val weeklySummaryEnabled: Flow<Boolean> = dataStore.weeklySummaryEnabled
    
    suspend fun setReminderEnabled(enabled: Boolean) = dataStore.setReminderEnabled(enabled)
    suspend fun setReminderTime(hour: Int, minute: Int) = dataStore.setReminderTime(hour, minute)
    suspend fun setStreakWarningEnabled(enabled: Boolean) = dataStore.setStreakWarningEnabled(enabled)
    suspend fun setWeeklySummaryEnabled(enabled: Boolean) = dataStore.setWeeklySummaryEnabled(enabled)
    
    // App Updates
    val autoUpdateEnabled: Flow<Boolean> = dataStore.autoUpdateEnabled
    
    suspend fun setAutoUpdateEnabled(enabled: Boolean) = dataStore.setAutoUpdateEnabled(enabled)
    
    // Aggregated Stats
    val userStats: Flow<UserStats> = dataStore.userStats
}

// Made with Bob
