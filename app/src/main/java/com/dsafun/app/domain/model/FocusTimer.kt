package com.dsafun.app.domain.model

enum class TimerState {
    IDLE,
    RUNNING,
    PAUSED,
    BREAK,
    COMPLETED
}

enum class SessionType(val displayName: String, val durationMinutes: Int) {
    QUICK_15("Quick 15m", 15),
    PRACTICE_25("Practice 25m", 25),
    DEEP_50("Deep Focus 50m", 50),
    CUSTOM("Custom", 25); // Default to 25, can be changed
    
    fun getDurationSeconds(): Long = durationMinutes * 60L
}

data class FocusSession(
    val id: Int = 0,
    val linkedProblemId: Int? = null,
    val durationSeconds: Long,
    val sessionType: String,
    val completedAt: Long
)

// Made with Bob
