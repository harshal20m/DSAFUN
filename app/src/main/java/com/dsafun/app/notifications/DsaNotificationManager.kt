package com.dsafun.app.notifications

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dsafun.app.MainActivity
import com.dsafun.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DsaNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun createPendingIntent(problemId: Long? = null): PendingIntent {
        val intent = if (problemId != null) {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("problem://id/$problemId")
                setClass(context, MainActivity::class.java)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        } else {
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        }

        return PendingIntent.getActivity(
            context,
            problemId?.toInt() ?: 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun showDailyReminderNotification(problemTitle: String, problemId: Long) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, NotificationChannels.DAILY_REMINDER)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Daily Challenge Awaits! 🎯")
            .setContentText(problemTitle)
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "Ready to solve today's challenge? Keep your streak alive!"
            ))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent(problemId))
            .build()

        notificationManager.notify(NOTIFICATION_ID_DAILY_REMINDER, notification)
    }

    fun showStreakWarningNotification(currentStreak: Int, hoursLeft: Int) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, NotificationChannels.STREAK_WARNING)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🔥 Streak at Risk!")
            .setContentText("Your $currentStreak day streak ends in $hoursLeft hours")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "Don't break your $currentStreak day streak! Solve a problem before midnight to keep it alive."
            ))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()

        notificationManager.notify(NOTIFICATION_ID_STREAK_WARNING, notification)
    }

    fun showWeeklySummaryNotification(problemsSolved: Int, currentStreak: Int) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, NotificationChannels.MILESTONES)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("📊 Weekly Summary")
            .setContentText("You solved $problemsSolved problems this week!")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "Great week! You solved $problemsSolved problems and maintained a $currentStreak day streak. Keep up the momentum!"
            ))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()

        notificationManager.notify(NOTIFICATION_ID_WEEKLY_SUMMARY, notification)
    }

    fun showMilestoneNotification(badgeName: String, badgeDescription: String) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, NotificationChannels.MILESTONES)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🏆 New Badge Unlocked!")
            .setContentText(badgeName)
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "$badgeName\n\n$badgeDescription"
            ))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()

        notificationManager.notify(NOTIFICATION_ID_MILESTONE, notification)
    }

    companion object {
        private const val NOTIFICATION_ID_DAILY_REMINDER = 1001
        private const val NOTIFICATION_ID_STREAK_WARNING = 1002
        private const val NOTIFICATION_ID_WEEKLY_SUMMARY = 1003
        private const val NOTIFICATION_ID_MILESTONE = 1004
    }
}

// Made with Bob