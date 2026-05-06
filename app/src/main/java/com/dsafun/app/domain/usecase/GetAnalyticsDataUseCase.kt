package com.dsafun.app.domain.usecase

import com.dsafun.app.data.local.dao.DailyProgressDao
import com.dsafun.app.data.local.dao.ProblemDao
import com.dsafun.app.data.local.dao.UserSolutionDao
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import com.dsafun.app.data.local.entity.UserSolutionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Analytics data models
 */
data class HeatmapEntry(
    val date: String,
    val count: Int
)

data class WeeklyEntry(
    val weekNumber: Int,
    val problemsSolved: Int
)

data class TopicCount(
    val topic: String,
    val count: Int
)

data class DifficultyBreakdown(
    val easy: Int,
    val medium: Int,
    val hard: Int
)

data class LanguageCount(
    val language: String,
    val count: Int
)

data class FastestSolve(
    val problemName: String,
    val timeSeconds: Int
)

data class AnalyticsData(
    val heatmapData: List<HeatmapEntry>,
    val weeklyProgress: List<WeeklyEntry>,
    val topicDistribution: List<TopicCount>,
    val difficultyBreakdown: DifficultyBreakdown,
    val languageUsage: List<LanguageCount>,
    val currentStreak: Int,
    val bestStreak: Int,
    val totalSolved: Int,
    val accuracyRate: Int,
    val fastestSolves: FastestSolvesData
)

data class FastestSolvesData(
    val easy: FastestSolve?,
    val medium: FastestSolve?,
    val hard: FastestSolve?
)

/**
 * Use case to aggregate all analytics data
 */
class GetAnalyticsDataUseCase @Inject constructor(
    private val dailyProgressDao: DailyProgressDao,
    private val userSolutionDao: UserSolutionDao,
    private val problemDao: ProblemDao,
    private val userPreferences: UserPreferencesDataStore
) {
    
    suspend operator fun invoke(): Flow<AnalyticsData> {
        val allProblems = problemDao.getAllProblemsSync()
        
        return combine(
            dailyProgressDao.getLast365Days(),
            userSolutionDao.getAllSolutions(),
            userPreferences.currentStreak,
            userPreferences.longestStreak
        ) { dailyProgress: List<com.dsafun.app.data.local.entity.DailyProgressEntity>,
            solutions: List<UserSolutionEntity>,
            currentStreak: Int,
            bestStreak: Int ->
            
            val solvedSolutions = solutions.filter { it.status == "SOLVED" }
            
            AnalyticsData(
                heatmapData = buildHeatmapData(dailyProgress),
                weeklyProgress = buildWeeklyData(dailyProgress),
                topicDistribution = buildTopicDistribution(solvedSolutions, allProblems),
                difficultyBreakdown = buildDifficultyBreakdown(solvedSolutions, allProblems),
                languageUsage = buildLanguageUsage(solvedSolutions),
                currentStreak = currentStreak,
                bestStreak = bestStreak,
                totalSolved = solvedSolutions.distinctBy { it.problemId }.size,
                accuracyRate = calculateAccuracyRate(solutions),
                fastestSolves = findFastestSolves(solvedSolutions, allProblems)
            )
        }
    }
    
    private fun buildHeatmapData(dailyProgress: List<com.dsafun.app.data.local.entity.DailyProgressEntity>): List<HeatmapEntry> {
        val progressMap = dailyProgress.associateBy { it.date }
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        
        return (0 until 365).map { daysAgo ->
            val date = today.minusDays(daysAgo.toLong())
            val dateStr = date.format(formatter)
            val count = progressMap[dateStr]?.problemsSolved ?: 0
            HeatmapEntry(dateStr, count)
        }.reversed()
    }
    
    private fun buildWeeklyData(dailyProgress: List<com.dsafun.app.data.local.entity.DailyProgressEntity>): List<WeeklyEntry> {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        
        return (0 until 12).map { weeksAgo ->
            val weekStart = today.minusWeeks(weeksAgo.toLong()).minusDays(today.dayOfWeek.value.toLong() - 1)
            val weekEnd = weekStart.plusDays(6)
            
            val weekCount = dailyProgress
                .filter {
                    val date = LocalDate.parse(it.date, formatter)
                    !date.isBefore(weekStart) && !date.isAfter(weekEnd)
                }
                .sumOf { it.problemsSolved }
            
            WeeklyEntry(12 - weeksAgo, weekCount)
        }.reversed()
    }
    
    private fun buildTopicDistribution(solutions: List<UserSolutionEntity>, problems: List<com.dsafun.app.data.local.entity.ProblemEntity>): List<TopicCount> {
        val problemIds = solutions.map { it.problemId }.distinct()
        
        return problems
            .filter { it.id in problemIds }
            .groupBy { it.topic }
            .map { (topic, probs) -> TopicCount(topic, probs.size) }
            .sortedByDescending { it.count }
    }
    
    private fun buildDifficultyBreakdown(solutions: List<UserSolutionEntity>, problems: List<com.dsafun.app.data.local.entity.ProblemEntity>): DifficultyBreakdown {
        val problemIds = solutions.map { it.problemId }.distinct()
        
        val solvedProblems = problems.filter { it.id in problemIds }
        
        return DifficultyBreakdown(
            easy = solvedProblems.count { it.difficulty == "Easy" },
            medium = solvedProblems.count { it.difficulty == "Medium" },
            hard = solvedProblems.count { it.difficulty == "Hard" }
        )
    }
    
    private fun buildLanguageUsage(solutions: List<UserSolutionEntity>): List<LanguageCount> {
        return solutions
            .groupBy { it.language }
            .map { (language, sols) -> LanguageCount(language, sols.size) }
            .sortedByDescending { it.count }
    }
    
    private fun calculateAccuracyRate(solutions: List<UserSolutionEntity>): Int {
        if (solutions.isEmpty()) return 0
        
        val firstAttempts = solutions
            .groupBy { it.problemId }
            .mapValues { it.value.minByOrNull { sol -> sol.lastEditedAt } }
            .values
            .filterNotNull()
        
        val successfulFirstAttempts = firstAttempts.count { it.status == "SOLVED" }
        
        return if (firstAttempts.isNotEmpty()) {
            ((successfulFirstAttempts.toFloat() / firstAttempts.size) * 100f).toInt()
        } else 0
    }
    
    private fun findFastestSolves(solutions: List<UserSolutionEntity>, problems: List<com.dsafun.app.data.local.entity.ProblemEntity>): FastestSolvesData {
        val problemMap = problems.associateBy { it.id }
        
        val easyFastest = solutions
            .filter { problemMap[it.problemId]?.difficulty == "Easy" }
            .minByOrNull { it.timeTakenSeconds }
            ?.let { solution ->
                problemMap[solution.problemId]?.let { problem ->
                    FastestSolve(problem.title, solution.timeTakenSeconds.toInt())
                }
            }
        
        val mediumFastest = solutions
            .filter { problemMap[it.problemId]?.difficulty == "Medium" }
            .minByOrNull { it.timeTakenSeconds }
            ?.let { solution ->
                problemMap[solution.problemId]?.let { problem ->
                    FastestSolve(problem.title, solution.timeTakenSeconds.toInt())
                }
            }
        
        val hardFastest = solutions
            .filter { problemMap[it.problemId]?.difficulty == "Hard" }
            .minByOrNull { it.timeTakenSeconds }
            ?.let { solution ->
                problemMap[solution.problemId]?.let { problem ->
                    FastestSolve(problem.title, solution.timeTakenSeconds.toInt())
                }
            }
        
        return FastestSolvesData(
            easy = easyFastest,
            medium = mediumFastest,
            hard = hardFastest
        )
    }
}

// Made with Bob