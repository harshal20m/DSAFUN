package com.dsafun.app.domain.usecase

import com.dsafun.app.data.local.entity.BadgeEntity
import com.dsafun.app.data.local.entity.UserSolutionEntity
import com.dsafun.app.data.repository.PreferencesRepository
import com.dsafun.app.data.repository.ProgressRepository
import com.dsafun.app.domain.executor.TestCaseRunner
import com.dsafun.app.domain.executor.TestRunResult
import com.dsafun.app.domain.model.Language
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.repository.ProblemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for submitting a solution and running tests
 */
class SubmitSolutionUseCase @Inject constructor(
    private val problemRepository: ProblemRepository,
    private val preferencesRepository: PreferencesRepository,
    private val progressRepository: ProgressRepository,
    private val testCaseRunner: TestCaseRunner,
    private val checkAndAwardBadgesUseCase: CheckAndAwardBadgesUseCase
) {
    
    operator fun invoke(
        problemId: Int,
        code: String,
        language: Language,
        timeTakenSeconds: Long
    ): Flow<SubmissionResult> = flow {
        // Emit loading state
        emit(SubmissionResult.Running)
        
        // Get problem details (get first value from Flow)
        val problem = problemRepository.getProblemById(problemId).first()
        if (problem == null) {
            emit(SubmissionResult.Error("Problem not found"))
            return@flow
        }
        
        // Run test cases
        val testResult = testCaseRunner.runTests(
            code = code,
            testCases = problem.testCases,
            language = language,
            problemId = problemId
        )
        
        // Determine status
        val status = when {
            testResult.allPassed -> "SOLVED"
            testResult.passedCount > 0 -> "ATTEMPTED"
            else -> "FAILED"
        }
        
        // Calculate XP reward
        val xpGained = if (testResult.allPassed) {
            problem.xpReward
        } else {
            0
        }
        
        // Save solution to database
        val solution = UserSolutionEntity(
            id = 0, // Auto-generated
            problemId = problemId,
            language = language.name,
            code = code,
            status = status,
            timeTakenSeconds = timeTakenSeconds,
            attemptCount = 1, // TODO: Increment existing attempts
            solvedAt = if (testResult.allPassed) System.currentTimeMillis() else null,
            isFavorite = false,
            lastEditedAt = System.currentTimeMillis()
        )
        
        problemRepository.saveSolution(solution)
        
        // Award XP if solved
        val levelUpEvent = if (xpGained > 0) {
            preferencesRepository.awardXp(xpGained)
        } else {
            null
        }
        
        // Update statistics and daily progress
        if (testResult.allPassed) {
            preferencesRepository.incrementProblemsSolved()
            
            // Record solve in daily progress (handles streak logic)
            val minutesSpent = (timeTakenSeconds / 60).toInt()
            progressRepository.recordSolve(xpGained, minutesSpent)
        }
        
        // Add time spent to total
        val minutesSpent = (timeTakenSeconds / 60).toInt()
        if (minutesSpent > 0) {
            preferencesRepository.addTimeSpent(minutesSpent)
        }
        
        // Check and award badges after successful solve
        val newBadges = if (testResult.allPassed) {
            val result = checkAndAwardBadgesUseCase(
                problemId = problemId,
                timeTakenSeconds = timeTakenSeconds.toInt(),
                difficulty = problem.difficulty
            )
            result.newlyUnlockedBadges
        } else {
            emptyList()
        }
        
        // Emit success result
        emit(
            SubmissionResult.Success(
                testResult = testResult,
                xpGained = xpGained,
                levelUpEvent = levelUpEvent,
                status = status,
                newBadges = newBadges
            )
        )
    }
}

/**
 * Sealed class representing submission states
 */
sealed class SubmissionResult {
    object Running : SubmissionResult()
    
    data class Success(
        val testResult: TestRunResult,
        val xpGained: Int,
        val levelUpEvent: com.dsafun.app.data.local.datastore.LevelUpEvent?,
        val status: String,
        val newBadges: List<BadgeEntity> = emptyList()
    ) : SubmissionResult()
    
    data class Error(val message: String) : SubmissionResult()
}

// Made with Bob
