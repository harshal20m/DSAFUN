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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@HiltWorker
class WeeklySummaryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val preferencesRepository: PreferencesRepository,
    private val dailyProgressDao: DailyProgressDao,
    private val notificationManager: DsaNotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Check if weekly summary is enabled
            val weeklySummaryEnabled = preferencesRepository.weeklySummaryEnabled.first()
            if (!weeklySummaryEnabled) {
                return Result.success()
            }

            // Check if it's Sunday and between 8-9 PM
            val now = LocalDate.now()
            val currentTime = LocalTime.now()
            
            if (now.dayOfWeek == DayOfWeek.SUNDAY && currentTime.hour == 20) {
                // Calculate last 7 days stats
                val endDate = now.toString()
                val startDate = now.minusDays(6).toString()
                
                val progressList = dailyProgressDao.getProgressForDateRange(startDate, endDate).first()
                val totalSolved = progressList.sumOf { it.problemsSolved }
                
                val currentStreak = preferencesRepository.currentStreak.first()
                
                notificationManager.showWeeklySummaryNotification(
                    problemsSolved = totalSolved,
                    currentStreak = currentStreak
                )
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

// Made with Bob