package com.dsafun.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dsafun.app.data.local.entity.BadgeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {
    
    @Query("SELECT * FROM badges ORDER BY id")
    fun getAllBadges(): Flow<List<BadgeEntity>>
    
    @Query("SELECT * FROM badges WHERE isUnlocked = 1 ORDER BY unlockedAt DESC")
    fun getUnlockedBadges(): Flow<List<BadgeEntity>>
    
    @Query("SELECT * FROM badges WHERE id = :badgeId")
    suspend fun getBadgeById(badgeId: String): BadgeEntity?
    
    @Query("UPDATE badges SET isUnlocked = 1, unlockedAt = :timestamp WHERE id = :badgeId")
    suspend fun unlockBadge(badgeId: String, timestamp: Long)
    
    @Query("SELECT isUnlocked FROM badges WHERE id = :badgeId")
    suspend fun isUnlocked(badgeId: String): Boolean
    
    @Query("SELECT COUNT(*) FROM badges WHERE isUnlocked = 1")
    suspend fun getUnlockedCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)
    
    @Query("DELETE FROM badges")
    suspend fun deleteAll()
}

// Made with Bob