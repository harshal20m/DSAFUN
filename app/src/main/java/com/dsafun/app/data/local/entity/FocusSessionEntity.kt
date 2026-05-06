package com.dsafun.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val linkedProblemId: Int? = null,
    val durationSeconds: Long,
    val sessionType: String, // "QUICK_15", "PRACTICE_25", "DEEP_50", "CUSTOM"
    val completedAt: Long
)

// Made with Bob
