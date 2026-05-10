package com.dsafun.app.domain.repository

import com.dsafun.app.data.local.entity.UserSolutionEntity
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.model.ProblemFilter
import com.dsafun.app.ui.screens.LeetCodeProblemItem
import kotlinx.coroutines.flow.Flow

data class MarkedProblemItem(
    val nativeProblems: List<Problem> = emptyList(),
    val leetCodeProblems: List<LeetCodeProblemItem> = emptyList(),
    val solvedNativeProblemIds: Set<Int> = emptySet(),
    val solvedLeetCodeProblemIds: Set<String> = emptySet()
)

interface ProblemRepository {
    fun getProblems(): Flow<List<Problem>>
    fun getFilteredProblems(filter: ProblemFilter): Flow<List<Problem>>
    fun getProblemById(id: Int): Flow<Problem?>
    fun getMarkedProblems(): Flow<MarkedProblemItem>
    suspend fun saveSolution(solution: UserSolutionEntity)
}

// Made with Bob
