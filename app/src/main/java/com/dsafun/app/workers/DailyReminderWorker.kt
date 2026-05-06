package com.dsafun.app.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dsafun.app.data.local.dao.DailyProgressDao
import com.dsafun.app.data.local.dao.ProblemDao
import com.dsafun.app.data.repository.PreferencesRepository
import com.dsafun.app.notifications.DsaNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@HiltWorker
class DailyReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val preferencesRepository: PreferencesRepository,
    private val dailyProgressDao: DailyProgressDao,
    private val problemDao: ProblemDao,
    private val notificationManager: DsaNotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Check if reminders are enabled
            val reminderEnabled = preferencesRepository.reminderEnabled.first()
            if (!reminderEnabled) {
                return Result.success()
            }

            // Check if user has solved a problem today
            val today = LocalDate.now().toString()
            val todayProgress = dailyProgressDao.getProgressForDate(today).first()
            
            if (todayProgress == null || todayProgress.problemsSolved == 0) {
                // Get a random problem as daily challenge
                val allProblems = problemDao.getAllProblems().first()

                if (allProblems.isNotEmpty()) {
                    val dailyChallenge = allProblems.random()
                    notificationManager.showDailyReminderNotification(
                        problemTitle = dailyChallenge.title,
                        problemId = dailyChallenge.id.toLong()
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