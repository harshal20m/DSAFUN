package com.dsafun.app.data.local.dao

import androidx.room.*
import com.dsafun.app.data.local.entity.DailyProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for daily progress tracking
 */
@Dao
interface DailyProgressDao {
    
    @Query("SELECT * FROM daily_progress WHERE date = :date")
    fun getProgressForDate(date: String): Flow<DailyProgressEntity?>
    
    @Query("SELECT * FROM daily_progress WHERE date = :date")
    suspend fun getProgressForDateSync(date: String): DailyProgressEntity?
    
    @Query("SELECT * FROM daily_progress ORDER BY date DESC LIMIT :limit")
    fun getRecentProgress(limit: Int = 7): Flow<List<DailyProgressEntity>>
    
    @Query("SELECT * FROM daily_progress ORDER BY date DESC LIMIT :limit")
    suspend fun getRecentProgressSync(limit: Int = 7): List<DailyProgressEntity>
    
    @Query("SELECT * FROM daily_progress WHERE streakActive = 1 ORDER BY date DESC")
    suspend fun getStreakDays(): List<DailyProgressEntity>
    
    @Query("SELECT COUNT(*) FROM daily_progress WHERE streakActive = 1")
    suspend fun getStreakCount(): Int
    
    @Query("SELECT * FROM daily_progress ORDER BY date DESC LIMIT 365")
    fun getLast365Days(): Flow<List<DailyProgressEntity>>
    
    @Query("SELECT * FROM daily_progress WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getProgressForDateRange(startDate: String, endDate: String): Flow<List<DailyProgressEntity>>
    
    @Query("SELECT COUNT(DISTINCT date) FROM daily_progress WHERE problemsSolved > 0")
    suspend fun getActiveDaysCount(): Int
    
    @Query("SELECT * FROM daily_progress ORDER BY date DESC LIMIT 84")
    fun getLast12Weeks(): Flow<List<DailyProgressEntity>>
    
    @Upsert
    suspend fun upsertProgress(progress: DailyProgressEntity)
    
    @Query("DELETE FROM daily_progress")
    suspend fun deleteAll()
}

// Made with Bob