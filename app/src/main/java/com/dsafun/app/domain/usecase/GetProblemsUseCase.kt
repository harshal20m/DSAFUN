package com.dsafun.app.domain.usecase

import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.model.ProblemFilter
import com.dsafun.app.domain.repository.ProblemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProblemsUseCase @Inject constructor(
    private val repository: ProblemRepository
) {
    operator fun invoke(filter: ProblemFilter = ProblemFilter.EMPTY): Flow<List<Problem>> {
        return repository.getFilteredProblems(filter)
    }
}

// Made with Bob
