package com.dsafun.app.domain.repository

import com.dsafun.app.data.local.entity.UserSolutionEntity
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.model.ProblemFilter
import kotlinx.coroutines.flow.Flow

interface ProblemRepository {
    fun getProblems(): Flow<List<Problem>>
    fun getFilteredProblems(filter: ProblemFilter): Flow<List<Problem>>
    fun getProblemById(id: Int): Flow<Problem?>
    suspend fun saveSolution(solution: UserSolutionEntity)
}

// Made with Bob
