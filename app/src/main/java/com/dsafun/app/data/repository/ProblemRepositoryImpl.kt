package com.dsafun.app.data.repository

import com.dsafun.app.data.local.dao.ProblemDao
import com.dsafun.app.data.local.dao.UserSolutionDao
import com.dsafun.app.data.local.entity.ProblemEntity
import com.dsafun.app.data.local.entity.UserSolutionEntity
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.model.ProblemFilter
import com.dsafun.app.domain.repository.ProblemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProblemRepositoryImpl @Inject constructor(
    private val problemDao: ProblemDao,
    private val userSolutionDao: UserSolutionDao
) : ProblemRepository {

    override fun getProblems(): Flow<List<Problem>> {
        return problemDao.getAllProblems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFilteredProblems(filter: ProblemFilter): Flow<List<Problem>> {
        return if (!filter.isActive()) {
            getProblems()
        } else {
            problemDao.getFilteredProblems(
                topic = filter.topic,
                difficulty = null, // We'll handle multiple difficulties in memory
                searchQuery = filter.searchQuery
            ).map { entities ->
                entities
                    .filter { entity ->
                        // Filter by difficulties if specified
                        filter.difficulties.isEmpty() || entity.difficulty in filter.difficulties
                    }
                    .map { it.toDomain() }
            }
        }
    }

    override fun getProblemById(id: Int): Flow<Problem?> {
        return problemDao.getProblemById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun saveSolution(solution: UserSolutionEntity) {
        userSolutionDao.upsertSolution(solution)
    }

    private fun ProblemEntity.toDomain(): Problem {
        return Problem(
            id = id,
            title = title,
            description = description,
            difficulty = difficulty,
            topic = topic,
            constraints = constraints,
            examples = examples,
            hints = hints,
            editorial = editorial,
            timeEstimateMinutes = timeEstimateMinutes,
            acceptanceRate = acceptanceRate,
            xpReward = xpReward,
            starterCode = starterCode,
            testCases = testCases
        )
    }
}

// Made with Bob
