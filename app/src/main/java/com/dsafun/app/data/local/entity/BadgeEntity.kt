package com.dsafun.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val iconType: String, // FLAME, TROPHY, CLOCK, STAR, OWL, GLOBE, LIGHTNING
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null // Timestamp in milliseconds
)

// Made with Bob