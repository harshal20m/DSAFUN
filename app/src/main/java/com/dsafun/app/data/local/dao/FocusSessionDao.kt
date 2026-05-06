package com.dsafun.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dsafun.app.data.local.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    
    @Insert
    suspend fun insertSession(session: FocusSessionEntity)
    
    @Query("""
        SELECT * FROM focus_sessions 
        WHERE completedAt >= :startOfDay AND completedAt < :endOfDay
        ORDER BY completedAt DESC
    """)
    fun getSessionsForDate(startOfDay: Long, endOfDay: Long): Flow<List<FocusSessionEntity>>
    
    @Query("""
        SELECT COALESCE(SUM(durationSeconds), 0) 
        FROM focus_sessions 
        WHERE completedAt >= :startOfDay AND completedAt < :endOfDay
    """)
    fun getTotalFocusTimeForDate(startOfDay: Long, endOfDay: Long): Flow<Long>
    
    @Query("""
        SELECT * FROM focus_sessions 
        ORDER BY completedAt DESC 
        LIMIT :limit
    """)
    fun getRecentSessions(limit: Int = 10): Flow<List<FocusSessionEntity>>
}

// Made with Bob
