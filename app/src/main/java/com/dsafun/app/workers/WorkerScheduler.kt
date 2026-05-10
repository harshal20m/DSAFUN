package com.dsafun.app.workers

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    fun scheduleAll(reminderHour: Int = 9, reminderMinute: Int = 0) {
        scheduleDailyReminder(reminderHour, reminderMinute)
        scheduleStreakGuard()
        scheduleWeeklySummary()
    }
    
    fun scheduleCollectionReminder(
        collectionType: String,
        hour: Int,
        minute: Int,
        enabled: Boolean
    ) {
        val workName = "${COLLECTION_REMINDER_WORK}_$collectionType"
        
        if (!enabled) {
            workManager.cancelUniqueWork(workName)
            return
        }
        
        // Calculate initial delay to hit the target time
        val now = LocalTime.now()
        val targetTime = LocalTime.of(hour, minute)
        
        val initialDelay = if (targetTime.isAfter(now)) {
            Duration.between(now, targetTime).toMinutes()
        } else {
            // Schedule for tomorrow
            Duration.between(now, targetTime).plusHours(24).toMinutes()
        }
        
        val inputData = workDataOf(
            CollectionReminderWorker.KEY_COLLECTION_TYPE to collectionType
        )
        
        val collectionReminderRequest = PeriodicWorkRequestBuilder<CollectionReminderWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .setInputData(inputData)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.REPLACE,
            collectionReminderRequest
        )
    }

    fun scheduleDailyReminder(hour: Int, minute: Int) {
        // Cancel existing reminder
        workManager.cancelUniqueWork(DAILY_REMINDER_WORK)

        // Calculate initial delay to hit the target time
        val now = LocalTime.now()
        val targetTime = LocalTime.of(hour, minute)
        
        val initialDelay = if (targetTime.isAfter(now)) {
            Duration.between(now, targetTime).toMinutes()
        } else {
            // Schedule for tomorrow
            Duration.between(now, targetTime).plusHours(24).toMinutes()
        }

        val dailyReminderRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            DAILY_REMINDER_WORK,
            ExistingPeriodicWorkPolicy.REPLACE,
            dailyReminderRequest
        )
    }

    fun scheduleStreakGuard() {
        val streakGuardRequest = PeriodicWorkRequestBuilder<StreakGuardWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            STREAK_GUARD_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            streakGuardRequest
        )
    }

    fun scheduleWeeklySummary() {
        val weeklySummaryRequest = PeriodicWorkRequestBuilder<WeeklySummaryWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            WEEKLY_SUMMARY_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            weeklySummaryRequest
        )
    }

    fun rescheduleReminder(hour: Int, minute: Int) {
        scheduleDailyReminder(hour, minute)
    }

    fun cancelAll() {
        workManager.cancelUniqueWork(DAILY_REMINDER_WORK)
        workManager.cancelUniqueWork(STREAK_GUARD_WORK)
        workManager.cancelUniqueWork(WEEKLY_SUMMARY_WORK)
    }

    fun cancelDailyReminder() {
        workManager.cancelUniqueWork(DAILY_REMINDER_WORK)
    }

    fun cancelStreakGuard() {
        workManager.cancelUniqueWork(STREAK_GUARD_WORK)
    }

    fun cancelWeeklySummary() {
        workManager.cancelUniqueWork(WEEKLY_SUMMARY_WORK)
    }
    
    fun cancelCollectionReminder(collectionType: String) {
        val workName = "${COLLECTION_REMINDER_WORK}_$collectionType"
        workManager.cancelUniqueWork(workName)
    }

    companion object {
        private const val DAILY_REMINDER_WORK = "daily_reminder_work"
        private const val STREAK_GUARD_WORK = "streak_guard_work"
        private const val WEEKLY_SUMMARY_WORK = "weekly_summary_work"
        private const val COLLECTION_REMINDER_WORK = "collection_reminder_work"
    }
}

// Made with Bob