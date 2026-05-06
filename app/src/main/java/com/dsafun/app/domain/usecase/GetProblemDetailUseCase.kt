package com.dsafun.app.domain.usecase

import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.repository.ProblemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProblemDetailUseCase @Inject constructor(
    private val repository: ProblemRepository
) {
    operator fun invoke(problemId: Int): Flow<Problem?> {
        return repository.getProblemById(problemId)
    }
}

// Made with Bob
