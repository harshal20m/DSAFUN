package com.dsafun.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_solutions")
data class UserSolutionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val problemId: Int,
    val language: String,
    val code: String,
    val status: String, // "NOT_ATTEMPTED", "ATTEMPTED", "SOLVED"
    val timeTakenSeconds: Long = 0,
    val attemptCount: Int = 0,
    val solvedAt: Long? = null, // timestamp in millis
    val isFavorite: Boolean = false,
    val lastEditedAt: Long = System.currentTimeMillis()
)

// Made with Bob