package com.dsafun.app.domain.usecase

import com.dsafun.app.data.local.dao.DailyProgressDao
import com.dsafun.app.data.local.dao.ProblemDao
import com.dsafun.app.data.local.dao.UserSolutionDao
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import com.dsafun.app.data.local.entity.UserSolutionEntity
import com.dsafun.app.data.local.entity.toDomainModel
import com.dsafun.app.data.repository.ProgressRepository
import com.dsafun.app.domain.model.Problem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

/**
 * Use case for getting home screen statistics
 */
class GetHomeStatsUseCase @Inject constructor(
    private val preferencesDataStore: UserPreferencesDataStore,
    private val dailyProgressDao: DailyProgressDao,
    private val userSolutionDao: UserSolutionDao,
    private val problemDao: ProblemDao,
    private val progressRepository: ProgressRepository
) {
    
    operator fun invoke(): Flow<HomeStats> {
        return combine(
            preferencesDataStore.userStats,
            progressRepository.getTodayProgress(),
            preferencesDataStore.dailyGoal
        ) { userStats, todayProgress, dailyGoal ->
            
            // Get recent solutions
            val recentSolutions = userSolutionDao.getRecentSolutionsSync(5)
            
            // Get daily challenge (deterministic based on day of year)
            val dailyChallenge = getDailyChallenge()
            
            // Check if daily challenge is completed
            val solvedProblems = userSolutionDao.getSolvedProblemsSync()
            val isDailyChallengeCompleted = dailyChallenge?.let { challenge ->
                solvedProblems.any { it.problemId == challenge.id && it.status == "SOLVED" }
            } ?: false
            
            // Get topic progress
            val topicProgress = getTopicProgress()
            
            // Calculate XP to next level
            val xpToNextLevel = preferencesDataStore.getXpForNextLevel(
                userStats.totalXp,
                userStats.currentLevel
            )
            
            HomeStats(
                userName = userStats.userName.ifEmpty { "Coder" },
                currentStreak = userStats.currentStreak,
                bestStreak = userStats.longestStreak,
                todaySolved = todayProgress?.problemsSolved ?: 0,
                dailyGoal = dailyGoal,
                totalXp = userStats.totalXp,
                level = userStats.currentLevel,
                xpToNextLevel = xpToNextLevel,
                dailyChallenge = dailyChallenge,
                isDailyChallengeCompleted = isDailyChallengeCompleted,
                recentSolutions = recentSolutions,
                topicProgresses = topicProgress,
                freezeTokens = userStats.freezeTokens
            )
        }
    }
    
    private suspend fun getDailyChallenge(): Problem? {
        val allProblems = problemDao.getAllProblemsSync()
        if (allProblems.isEmpty()) return null
        
        // Use day of year to deterministically select a problem
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val problemIndex = dayOfYear % allProblems.size
        
        return allProblems[problemIndex].toDomainModel()
    }
    
    private suspend fun getTopicProgress(): List<TopicProgress> {
        val allProblems = problemDao.getAllProblemsSync()
        val solvedProblems = userSolutionDao.getSolvedProblemsSync()
        
        // Group by topic
        val topicMap = allProblems.groupBy { it.topic }
        
        return topicMap.map { (topic, problems) ->
            val solvedCount = problems.count { problem ->
                solvedProblems.any { it.problemId == problem.id && it.status == "SOLVED" }
            }
            TopicProgress(
                topic = topic,
                solved = solvedCount,
                total = problems.size
            )
        }.sortedByDescending { it.solved }
    }
}

/**
 * Home screen statistics
 */
data class HomeStats(
    val userName: String,
    val currentStreak: Int,
    val bestStreak: Int,
    val todaySolved: Int,
    val dailyGoal: Int,
    val totalXp: Int,
    val level: Int,
    val xpToNextLevel: Int,
    val dailyChallenge: Problem?,
    val isDailyChallengeCompleted: Boolean,
    val recentSolutions: List<UserSolutionEntity>,
    val topicProgresses: List<TopicProgress>,
    val freezeTokens: Int
)

/**
 * Topic progress data
 */
data class TopicProgress(
    val topic: String,
    val solved: Int,
    val total: Int
) {
    val progress: Float = if (total > 0) solved.toFloat() / total else 0f
}

// Made with Bob