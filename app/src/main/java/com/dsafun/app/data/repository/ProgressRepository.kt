package com.dsafun.app.data.repository

import com.dsafun.app.data.local.dao.DailyProgressDao
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import com.dsafun.app.data.local.entity.DailyProgressEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing daily progress and streaks
 */
@Singleton
class ProgressRepository @Inject constructor(
    private val dailyProgressDao: DailyProgressDao,
    private val preferencesDataStore: UserPreferencesDataStore
) {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    /**
     * Records a problem solve and updates streak
     */
    suspend fun recordSolve(xpEarned: Int, timeSpentMinutes: Int) {
        val today = getTodayDate()
        val yesterday = getYesterdayDate()
        
        // Get or create today's progress
        val todayProgress = dailyProgressDao.getProgressForDateSync(today)
        val dailyGoal = preferencesDataStore.dailyGoal.first()
        
        val updatedProgress = if (todayProgress != null) {
            // Update existing progress
            todayProgress.copy(
                problemsSolved = todayProgress.problemsSolved + 1,
                xpEarned = todayProgress.xpEarned + xpEarned,
                timeSpentMinutes = todayProgress.timeSpentMinutes + timeSpentMinutes,
                streakActive = true
            )
        } else {
            // Create new progress for today
            // Check if yesterday was active to continue streak
            val yesterdayProgress = dailyProgressDao.getProgressForDateSync(yesterday)
            val isStreakContinued = yesterdayProgress?.streakActive == true
            
            if (isStreakContinued) {
                // Continue streak
                preferencesDataStore.updateStreak()
            } else {
                // Start new streak
                preferencesDataStore.resetStreak()
                preferencesDataStore.updateStreak() // Sets to 1
            }
            
            DailyProgressEntity(
                date = today,
                problemsSolved = 1,
                goalTarget = dailyGoal,
                xpEarned = xpEarned,
                streakActive = true,
                timeSpentMinutes = timeSpentMinutes
            )
        }
        
        dailyProgressDao.upsertProgress(updatedProgress)
        preferencesDataStore.updateLastActiveDate(today)
    }
    
    /**
     * Gets current streak by counting consecutive active days
     */
    suspend fun getCurrentStreak(): Int {
        val streakDays = dailyProgressDao.getStreakDays()
        if (streakDays.isEmpty()) return 0
        
        // Count consecutive days from today backwards
        var streak = 0
        val today = getTodayDate()
        var checkDate = today
        
        for (progress in streakDays) {
            if (progress.date == checkDate && progress.streakActive) {
                streak++
                checkDate = getPreviousDate(checkDate)
            } else {
                break
            }
        }
        
        return streak
    }
    
    /**
     * Gets today's progress
     */
    fun getTodayProgress(): Flow<DailyProgressEntity?> {
        return dailyProgressDao.getProgressForDate(getTodayDate())
    }
    
    /**
     * Gets recent progress (last N days)
     */
    fun getRecentProgress(days: Int = 7): Flow<List<DailyProgressEntity>> {
        return dailyProgressDao.getRecentProgress(days)
    }
    
    /**
     * Checks if streak should be broken (missed yesterday)
     */
    suspend fun checkAndUpdateStreak() {
        val today = getTodayDate()
        val yesterday = getYesterdayDate()
        val lastActiveDate = preferencesDataStore.lastActiveDate.first()
        
        // If last active was before yesterday, break streak
        if (lastActiveDate.isNotEmpty() && lastActiveDate < yesterday) {
            val freezeTokens = preferencesDataStore.freezeTokens.first()
            if (freezeTokens > 0) {
                // Use freeze token to save streak
                preferencesDataStore.useFreezeToken()
                // Mark yesterday as active with freeze
                dailyProgressDao.upsertProgress(
                    DailyProgressEntity(
                        date = yesterday,
                        problemsSolved = 0,
                        goalTarget = preferencesDataStore.dailyGoal.first(),
                        xpEarned = 0,
                        streakActive = true,
                        timeSpentMinutes = 0
                    )
                )
            } else {
                // Break streak
                preferencesDataStore.resetStreak()
            }
        }
    }
    
    /**
     * Manually use a freeze token to save yesterday's streak
     * Returns true if successful, false if no tokens available
     */
    suspend fun useFreezeToken(): Boolean {
        val freezeTokens = preferencesDataStore.freezeTokens.first()
        if (freezeTokens <= 0) {
            return false
        }
        
        val yesterday = getYesterdayDate()
        val dailyGoal = preferencesDataStore.dailyGoal.first()
        
        // Use the token
        preferencesDataStore.useFreezeToken()
        
        // Mark yesterday as active with freeze
        dailyProgressDao.upsertProgress(
            DailyProgressEntity(
                date = yesterday,
                problemsSolved = 0,
                goalTarget = dailyGoal,
                xpEarned = 0,
                streakActive = true,
                timeSpentMinutes = 0
            )
        )
        
        return true
    }
    
    /**
     * Reset freeze tokens to 2 at the start of a new month
     * Should be called when app opens and month has changed
     */
    suspend fun resetMonthlyFreezeTokens() {
        preferencesDataStore.resetMonthlyFreezeTokens()
    }
    
    /**
     * Get current freeze token count
     */
    fun getFreezeTokens(): Flow<Int> {
        return preferencesDataStore.freezeTokens
    }
    
    private fun getTodayDate(): String {
        return dateFormat.format(Date())
    }
    
    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return dateFormat.format(calendar.time)
    }
    
    private fun getPreviousDate(date: String): String {
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(date) ?: Date()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return dateFormat.format(calendar.time)
    }
}

// Made with Bob