package com.dsafun.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {
    const val DAILY_REMINDER = "daily_reminder"
    const val STREAK_WARNING = "streak_warning"
    const val MILESTONES = "milestones"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Daily Reminder Channel
            val dailyReminderChannel = NotificationChannel(
                DAILY_REMINDER,
                "Daily Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily problem reminders to keep you on track"
                enableVibration(true)
            }

            // Streak Warning Channel
            val streakWarningChannel = NotificationChannel(
                STREAK_WARNING,
                "Streak Warnings",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when your streak is at risk"
                enableVibration(true)
            }

            // Milestones Channel
            val milestonesChannel = NotificationChannel(
                MILESTONES,
                "Milestones & Achievements",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Celebrate your achievements and milestones"
                enableVibration(false)
            }

            notificationManager.createNotificationChannels(
                listOf(dailyReminderChannel, streakWarningChannel, milestonesChannel)
            )
        }
    }
}

// Made with Bob