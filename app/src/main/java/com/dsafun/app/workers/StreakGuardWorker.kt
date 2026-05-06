package com.dsafun.app.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dsafun.app.data.local.dao.DailyProgressDao
import com.dsafun.app.data.repository.PreferencesRepository
import com.dsafun.app.notifications.DsaNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalTime

@HiltWorker
class StreakGuardWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val preferencesRepository: PreferencesRepository,
    private val dailyProgressDao: DailyProgressDao,
    private val notificationManager: DsaNotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Check if streak warnings are enabled
            val streakWarningEnabled = preferencesRepository.streakWarningEnabled.first()
            if (!streakWarningEnabled) {
                return Result.success()
            }

            // Check if we're within 2 hours of midnight
            val now = LocalTime.now()
            val hoursUntilMidnight = 24 - now.hour
            
            if (hoursUntilMidnight <= 2) {
                // Check if daily goal is met
                val today = LocalDate.now().toString()
                val todayProgress = dailyProgressDao.getProgressForDate(today).first()
                val currentStreak = preferencesRepository.currentStreak.first()
                val dailyGoal = preferencesRepository.dailyGoal.first()

                val problemsSolved = todayProgress?.problemsSolved ?: 0
                
                // If goal not met and streak > 0, send warning
                if (problemsSolved < dailyGoal && currentStreak > 0) {
                    notificationManager.showStreakWarningNotification(
                        currentStreak = currentStreak,
                        hoursLeft = hoursUntilMidnight
                    )
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

// Made with Bob