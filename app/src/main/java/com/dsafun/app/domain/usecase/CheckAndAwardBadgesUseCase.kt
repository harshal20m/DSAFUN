package com.dsafun.app.domain.usecase

import com.dsafun.app.data.local.dao.ProblemDao
import com.dsafun.app.data.local.dao.UserSolutionDao
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import com.dsafun.app.data.local.entity.BadgeEntity
import com.dsafun.app.data.repository.BadgeRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

data class BadgeUnlockResult(
    val newlyUnlockedBadges: List<BadgeEntity>
)

class CheckAndAwardBadgesUseCase @Inject constructor(
    private val badgeRepository: BadgeRepository,
    private val userSolutionDao: UserSolutionDao,
    private val problemDao: ProblemDao,
    private val userPreferences: UserPreferencesDataStore
) {
    
    suspend operator fun invoke(
        problemId: Int,
        timeTakenSeconds: Int,
        difficulty: String
    ): BadgeUnlockResult {
        val newlyUnlocked = mutableListOf<BadgeEntity>()
        
        // Get all badges
        val allBadges = badgeRepository.getAllBadges().first()
        val lockedBadges = allBadges.filter { !it.isUnlocked }
        
        // Get current stats
        val allSolutions = userSolutionDao.getAllSolutions().first()
        val solvedSolutions = allSolutions.filter { it.status == "SOLVED" }
        val totalSolved = solvedSolutions.distinctBy { it.problemId }.size
        val currentStreak = userPreferences.currentStreak.first()
        
        // Check each locked badge
        for (badge in lockedBadges) {
            val shouldUnlock = when (badge.id) {
                "FIRST_SOLVE" -> checkFirstSolve(totalSolved)
                "STREAK_3" -> checkStreak(currentStreak, 3)
                "STREAK_7" -> checkStreak(currentStreak, 7)
                "STREAK_30" -> checkStreak(currentStreak, 30)
                "STREAK_100" -> checkStreak(currentStreak, 100)
                "STREAK_365" -> checkStreak(currentStreak, 365)
                "SOLVED_10" -> checkTotalSolved(totalSolved, 10)
                "SOLVED_50" -> checkTotalSolved(totalSolved, 50)
                "SOLVED_100" -> checkTotalSolved(totalSolved, 100)
                "SPEED_DEMON" -> checkSpeedDemon(difficulty, timeTakenSeconds)
                "POLYGLOT" -> checkPolyglot(solvedSolutions)
                "NIGHT_OWL" -> checkNightOwl()
                else -> false
            }
            
            if (shouldUnlock) {
                badgeRepository.unlockBadge(badge.id)
                newlyUnlocked.add(badge.copy(isUnlocked = true, unlockedAt = System.currentTimeMillis()))
            }
        }
        
        return BadgeUnlockResult(newlyUnlocked)
    }
    
    private fun checkFirstSolve(totalSolved: Int): Boolean {
        return totalSolved >= 1
    }
    
    private fun checkStreak(currentStreak: Int, target: Int): Boolean {
        return currentStreak >= target
    }
    
    private fun checkTotalSolved(totalSolved: Int, target: Int): Boolean {
        return totalSolved >= target
    }
    
    private fun checkSpeedDemon(difficulty: String, timeTakenSeconds: Int): Boolean {
        return difficulty == "Hard" && timeTakenSeconds < 600 // 10 minutes
    }
    
    private fun checkPolyglot(solutions: List<com.dsafun.app.data.local.entity.UserSolutionEntity>): Boolean {
        val languages = solutions.map { it.language }.distinct()
        val requiredLanguages = setOf("Kotlin", "Java", "Python", "JavaScript", "C++")
        return languages.containsAll(requiredLanguages)
    }
    
    private fun checkNightOwl(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour in 0..4 // Midnight to 4 AM
    }
}

// Made with Bob