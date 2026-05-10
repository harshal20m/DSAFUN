package com.dsafun.app.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dsafun.app.data.local.dao.UserSolutionDao
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import com.dsafun.app.notifications.DsaNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate

/**
 * Worker for collection-specific reminders
 * Sends notifications for specific problem collections (LeetCode, Apna College, etc.)
 */
@HiltWorker
class CollectionReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val userPreferences: UserPreferencesDataStore,
    private val userSolutionDao: UserSolutionDao,
    private val notificationManager: DsaNotificationManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val collectionType = inputData.getString(KEY_COLLECTION_TYPE) ?: return Result.failure()
            
            when (collectionType) {
                COLLECTION_LEETCODE -> handleLeetCodeReminder()
                COLLECTION_APNA_COLLEGE -> handleApnaCollegeReminder()
                COLLECTION_FRAZ -> handleFrazReminder()
                COLLECTION_LOVE_BABBAR -> handleLoveBabbarReminder()
                else -> Result.failure()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    private suspend fun handleLeetCodeReminder(): Result {
        // Simplified: Just send a reminder notification
        val nextProblem = getNextUnsolvedProblem("LeetCode")
        
        if (nextProblem != null) {
            notificationManager.showCollectionReminderNotification(
                collectionName = "LeetCode",
                problemTitle = nextProblem.title,
                problemDescription = nextProblem.description,
                problemId = nextProblem.id.toLong(),
                difficulty = nextProblem.difficulty
            )
        }
        
        return Result.success()
    }
    
    private suspend fun handleApnaCollegeReminder(): Result {
        val nextProblem = getNextUnsolvedProblem("Apna College")
        
        if (nextProblem != null) {
            notificationManager.showCollectionReminderNotification(
                collectionName = "Apna College",
                problemTitle = nextProblem.title,
                problemDescription = nextProblem.description,
                problemId = nextProblem.id.toLong(),
                difficulty = nextProblem.difficulty
            )
        }
        
        return Result.success()
    }
    
    private suspend fun handleFrazReminder(): Result {
        val nextProblem = getNextUnsolvedProblem("Fraz")
        
        if (nextProblem != null) {
            notificationManager.showCollectionReminderNotification(
                collectionName = "DSA Sheet by Fraz",
                problemTitle = nextProblem.title,
                problemDescription = nextProblem.description,
                problemId = nextProblem.id.toLong(),
                difficulty = nextProblem.difficulty
            )
        }
        
        return Result.success()
    }
    
    private suspend fun handleLoveBabbarReminder(): Result {
        val nextProblem = getNextUnsolvedProblem("Love Babbar")
        
        if (nextProblem != null) {
            notificationManager.showCollectionReminderNotification(
                collectionName = "Love Babbar 450",
                problemTitle = nextProblem.title,
                problemDescription = nextProblem.description,
                problemId = nextProblem.id.toLong(),
                difficulty = nextProblem.difficulty
            )
        }
        
        return Result.success()
    }
    
    private suspend fun getNextUnsolvedProblem(collection: String): ProblemInfo? {
        // Get all solved problem IDs
        val solvedIds = userSolutionDao.getSolvedProblemsSync()
            .filter { it.status == "SOLVED" }
            .map { it.problemId }
            .toSet()
        
        // This is a simplified version - in production, you'd query from the actual collection
        // For now, we'll return a mock problem
        // TODO: Implement actual collection-specific problem fetching
        return ProblemInfo(
            id = 1,
            title = "Sample Problem from $collection",
            description = "Continue your learning journey with this problem!",
            difficulty = "Medium"
        )
    }
    
    companion object {
        const val KEY_COLLECTION_TYPE = "collection_type"
        const val COLLECTION_LEETCODE = "leetcode"
        const val COLLECTION_APNA_COLLEGE = "apna_college"
        const val COLLECTION_FRAZ = "fraz"
        const val COLLECTION_LOVE_BABBAR = "love_babbar"
    }
}

/**
 * Simple data class for problem information
 */
data class ProblemInfo(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: String
)

// Made with Bob