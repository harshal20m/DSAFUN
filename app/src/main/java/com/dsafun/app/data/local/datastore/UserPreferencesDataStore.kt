package com.dsafun.app.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * DataStore for user preferences and progress tracking
 */
class UserPreferencesDataStore(private val context: Context) {
    
    private val dataStore = context.dataStore
    
    companion object {
        // User Profile
        val USER_NAME = stringPreferencesKey("user_name")
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        
        // XP and Level
        val TOTAL_XP = intPreferencesKey("total_xp")
        val CURRENT_LEVEL = intPreferencesKey("current_level")
        
        // Streak
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val LONGEST_STREAK = intPreferencesKey("longest_streak")
        val LAST_SOLVED_DATE = longPreferencesKey("last_solved_date")
        val LAST_ACTIVE_DATE = stringPreferencesKey("last_active_date") // Format: yyyy-MM-dd
        val FREEZE_TOKENS = intPreferencesKey("freeze_tokens")
        
        // Statistics
        val TOTAL_PROBLEMS_SOLVED = intPreferencesKey("total_problems_solved")
        val TOTAL_TIME_SPENT_MINUTES = intPreferencesKey("total_time_spent_minutes")
        
        // Goals and Settings
        val DAILY_GOAL = intPreferencesKey("daily_goal")
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val APP_THEME = stringPreferencesKey("app_theme") // DARK, LIGHT, MONOKAI, DRACULA, NORD
        val PREFERRED_LANGUAGE = stringPreferencesKey("preferred_language") // PYTHON, JAVA, etc.
        val FONT_SIZE_PREFERENCE = stringPreferencesKey("font_size_preference") // SMALL, MEDIUM, LARGE
        
        // Notifications
        val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        val REMINDER_HOUR = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE = intPreferencesKey("reminder_minute")
        val STREAK_WARNING_ENABLED = booleanPreferencesKey("streak_warning_enabled")
        val WEEKLY_SUMMARY_ENABLED = booleanPreferencesKey("weekly_summary_enabled")
        
        // Level thresholds for XP
        val LEVEL_THRESHOLDS = listOf(
            0,      // Level 1
            500,    // Level 2
            1200,   // Level 3
            2500,   // Level 4
            5000,   // Level 5
            8000,   // Level 6
            12000,  // Level 7
            17000,  // Level 8
            23000,  // Level 9
            30000   // Level 10
        )
    }
    
    // User Name
    val userName: Flow<String> = dataStore.data.map { preferences ->
        preferences[USER_NAME] ?: ""
    }
    
    suspend fun setUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }
    
    // Onboarding
    val onboardingComplete: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETE] ?: false
    }
    
    suspend fun setOnboardingComplete(complete: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETE] = complete
        }
    }
    
    // XP and Level
    val totalXp: Flow<Int> = dataStore.data.map { preferences ->
        preferences[TOTAL_XP] ?: 0
    }
    
    val currentLevel: Flow<Int> = dataStore.data.map { preferences ->
        preferences[CURRENT_LEVEL] ?: 1
    }
    
    /**
     * Awards XP and returns level up event if threshold crossed
     */
    suspend fun awardXp(xp: Int): LevelUpEvent? {
        var levelUpEvent: LevelUpEvent? = null
        
        dataStore.edit { preferences ->
            val currentXp = preferences[TOTAL_XP] ?: 0
            val currentLevel = preferences[CURRENT_LEVEL] ?: 1
            val newXp = currentXp + xp
            
            // Check if level up occurred
            val newLevel = calculateLevel(newXp)
            if (newLevel > currentLevel) {
                levelUpEvent = LevelUpEvent(
                    oldLevel = currentLevel,
                    newLevel = newLevel,
                    totalXp = newXp
                )
            }
            
            preferences[TOTAL_XP] = newXp
            preferences[CURRENT_LEVEL] = newLevel
        }
        
        return levelUpEvent
    }
    
    /**
     * Calculates level based on total XP
     */
    private fun calculateLevel(xp: Int): Int {
        for (i in LEVEL_THRESHOLDS.indices.reversed()) {
            if (xp >= LEVEL_THRESHOLDS[i]) {
                return i + 1
            }
        }
        return 1
    }
    
    /**
     * Gets XP needed for next level
     */
    fun getXpForNextLevel(currentXp: Int, currentLevel: Int): Int {
        val nextLevelIndex = currentLevel // 0-indexed in array
        return if (nextLevelIndex < LEVEL_THRESHOLDS.size) {
            LEVEL_THRESHOLDS[nextLevelIndex] - currentXp
        } else {
            0 // Max level reached
        }
    }
    
    // Streak
    val currentStreak: Flow<Int> = dataStore.data.map { preferences ->
        preferences[CURRENT_STREAK] ?: 0
    }
    
    val longestStreak: Flow<Int> = dataStore.data.map { preferences ->
        preferences[LONGEST_STREAK] ?: 0
    }
    
    suspend fun updateStreak() {
        dataStore.edit { preferences ->
            val currentStreak = (preferences[CURRENT_STREAK] ?: 0) + 1
            val longestStreak = preferences[LONGEST_STREAK] ?: 0
            
            preferences[CURRENT_STREAK] = currentStreak
            preferences[LAST_SOLVED_DATE] = System.currentTimeMillis()
            
            if (currentStreak > longestStreak) {
                preferences[LONGEST_STREAK] = currentStreak
            }
        }
    }
    
    suspend fun resetStreak() {
        dataStore.edit { preferences ->
            preferences[CURRENT_STREAK] = 0
        }
    }
    
    // Statistics
    val totalProblemsSolved: Flow<Int> = dataStore.data.map { preferences ->
        preferences[TOTAL_PROBLEMS_SOLVED] ?: 0
    }
    
    suspend fun incrementProblemsSolved() {
        dataStore.edit { preferences ->
            val current = preferences[TOTAL_PROBLEMS_SOLVED] ?: 0
            preferences[TOTAL_PROBLEMS_SOLVED] = current + 1
        }
    }
    
    val totalTimeSpentMinutes: Flow<Int> = dataStore.data.map { preferences ->
        preferences[TOTAL_TIME_SPENT_MINUTES] ?: 0
    }
    
    suspend fun addTimeSpent(minutes: Int) {
        dataStore.edit { preferences ->
            val current = preferences[TOTAL_TIME_SPENT_MINUTES] ?: 0
            preferences[TOTAL_TIME_SPENT_MINUTES] = current + minutes
        }
    }
    
    // Goals and Settings
    val dailyGoal: Flow<Int> = dataStore.data.map { preferences ->
        preferences[DAILY_GOAL] ?: 3
    }
    
    suspend fun setDailyGoal(goal: Int) {
        dataStore.edit { preferences ->
            preferences[DAILY_GOAL] = goal
        }
    }
    
    val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DARK_THEME] ?: true
    }
    
    suspend fun setDarkTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDark
        }
    }
    
    val appTheme: Flow<String> = dataStore.data.map { preferences ->
        preferences[APP_THEME] ?: "SYSTEM"
    }
    
    suspend fun setAppTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[APP_THEME] = theme
        }
    }
    
    val preferredLanguage: Flow<String> = dataStore.data.map { preferences ->
        preferences[PREFERRED_LANGUAGE] ?: "PYTHON"
    }
    
    suspend fun setPreferredLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PREFERRED_LANGUAGE] = language
        }
    }
    
    val fontSizePreference: Flow<String> = dataStore.data.map { preferences ->
        preferences[FONT_SIZE_PREFERENCE] ?: "MEDIUM"
    }
    
    suspend fun setFontSizePreference(size: String) {
        dataStore.edit { preferences ->
            preferences[FONT_SIZE_PREFERENCE] = size
        }
    }
    
    // Notifications
    val reminderEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[REMINDER_ENABLED] ?: true
    }
    
    suspend fun setReminderEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMINDER_ENABLED] = enabled
        }
    }
    
    val reminderHour: Flow<Int> = dataStore.data.map { preferences ->
        preferences[REMINDER_HOUR] ?: 20
    }
    
    val reminderMinute: Flow<Int> = dataStore.data.map { preferences ->
        preferences[REMINDER_MINUTE] ?: 0
    }
    
    suspend fun setReminderTime(hour: Int, minute: Int) {
        dataStore.edit { preferences ->
            preferences[REMINDER_HOUR] = hour
            preferences[REMINDER_MINUTE] = minute
        }
    }
    
    val streakWarningEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[STREAK_WARNING_ENABLED] ?: true
    }
    
    suspend fun setStreakWarningEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[STREAK_WARNING_ENABLED] = enabled
        }
    }
    
    val weeklySummaryEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[WEEKLY_SUMMARY_ENABLED] ?: true
    }
    
    suspend fun setWeeklySummaryEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[WEEKLY_SUMMARY_ENABLED] = enabled
        }
    }
    
    // Freeze Tokens
    val freezeTokens: Flow<Int> = dataStore.data.map { preferences ->
        preferences[FREEZE_TOKENS] ?: 2
    }
    
    suspend fun useFreezeToken() {
        dataStore.edit { preferences ->
            val current = preferences[FREEZE_TOKENS] ?: 0
            if (current > 0) {
                preferences[FREEZE_TOKENS] = current - 1
            }
        }
    }
    
    suspend fun addFreezeToken() {
        dataStore.edit { preferences ->
            val current = preferences[FREEZE_TOKENS] ?: 0
            preferences[FREEZE_TOKENS] = current + 1
        }
    }
    
    suspend fun resetMonthlyFreezeTokens() {
        dataStore.edit { preferences ->
            preferences[FREEZE_TOKENS] = 2
        }
    }
    
    // Last Active Date
    val lastActiveDate: Flow<String> = dataStore.data.map { preferences ->
        preferences[LAST_ACTIVE_DATE] ?: ""
    }
    
    suspend fun updateLastActiveDate(date: String) {
        dataStore.edit { preferences ->
            preferences[LAST_ACTIVE_DATE] = date
        }
    }
    
    /**
     * Gets all user stats as a single flow
     */
    val userStats: Flow<UserStats> = dataStore.data.map { preferences ->
        UserStats(
            userName = preferences[USER_NAME] ?: "",
            totalXp = preferences[TOTAL_XP] ?: 0,
            currentLevel = preferences[CURRENT_LEVEL] ?: 1,
            currentStreak = preferences[CURRENT_STREAK] ?: 0,
            longestStreak = preferences[LONGEST_STREAK] ?: 0,
            totalProblemsSolved = preferences[TOTAL_PROBLEMS_SOLVED] ?: 0,
            totalTimeSpentMinutes = preferences[TOTAL_TIME_SPENT_MINUTES] ?: 0,
            dailyGoal = preferences[DAILY_GOAL] ?: 3,
            freezeTokens = preferences[FREEZE_TOKENS] ?: 2
        )
    }
}

/**
 * Event emitted when user levels up
 */
data class LevelUpEvent(
    val oldLevel: Int,
    val newLevel: Int,
    val totalXp: Int
)

/**
 * Aggregated user statistics
 */
data class UserStats(
    val userName: String,
    val totalXp: Int,
    val currentLevel: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val totalProblemsSolved: Int,
    val totalTimeSpentMinutes: Int,
    val dailyGoal: Int,
    val freezeTokens: Int
)

// Made with Bob
