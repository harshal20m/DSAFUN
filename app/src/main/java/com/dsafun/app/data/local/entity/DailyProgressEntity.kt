package com.dsafun.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tracks daily progress for streak and goal tracking
 */
@Entity(tableName = "daily_progress")
data class DailyProgressEntity(
    @PrimaryKey
    val date: String, // Format: yyyy-MM-dd
    val problemsSolved: Int = 0,
    val goalTarget: Int = 3,
    val xpEarned: Int = 0,
    val streakActive: Boolean = false,
    val timeSpentMinutes: Int = 0
)

// Made with Bob