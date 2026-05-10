package com.dsafun.app.ui.screens

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsafun.app.data.local.dao.UserSolutionDao
import com.dsafun.app.data.local.datastore.LevelUpEvent
import com.dsafun.app.data.local.entity.UserSolutionEntity
import com.dsafun.app.data.repository.PreferencesRepository
import com.dsafun.app.data.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

private const val APNA_COLLEGE_LANGUAGE = "APNA_COLLEGE"

data class ApnaCollegeProblemItem(
    val id: String,
    val title: String,
    val topic: String,
    val url: String,
    val companies: String,
    val remarks: String
)

data class ApnaCollegeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val problems: List<ApnaCollegeProblemItem> = emptyList(),
    val solvedProblemIds: Set<String> = emptySet(),
    val completionBusyId: String? = null,
    val completionMessage: String? = null,
    val levelUpEvent: LevelUpEvent? = null,
    val selectedTab: Int = 0 // 0 = All, 1 = Marked
)

@HiltViewModel
class ApnaCollegeViewModel @Inject constructor(
    private val application: Application,
    private val userSolutionDao: UserSolutionDao,
    private val preferencesRepository: PreferencesRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApnaCollegeUiState())
    val uiState: StateFlow<ApnaCollegeUiState> = _uiState.asStateFlow()

    init {
        loadProblems()
    }

    fun loadProblems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            runCatching {
                withContext(Dispatchers.IO) {
                    val problems = loadApnaCollegeProblems()
                    val solvedIds = userSolutionDao.getAllSolutions().first()
                        .filter { it.language == APNA_COLLEGE_LANGUAGE && it.status == "SOLVED" }
                        .map { it.problemId.toString() }
                        .toSet()

                    problems to solvedIds
                }
            }.onSuccess { (problems, solvedIds) ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    problems = problems,
                    solvedProblemIds = solvedIds,
                    error = null
                )
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = throwable.message ?: "Failed to load Apna College problems"
                )
            }
        }
    }

    fun toggleSolved(problem: ApnaCollegeProblemItem) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                completionBusyId = problem.id,
                completionMessage = null,
                levelUpEvent = null
            )

            val wasSolved = _uiState.value.solvedProblemIds.contains(problem.id)

            withContext(Dispatchers.IO) {
                val existingSolution = userSolutionDao
                    .getSolutionForProblem(problem.id.toInt(), APNA_COLLEGE_LANGUAGE)
                    .first()

                if (wasSolved) {
                    existingSolution?.let {
                        userSolutionDao.upsertSolution(
                            it.copy(
                                status = "ATTEMPTED",
                                solvedAt = null,
                                lastEditedAt = System.currentTimeMillis()
                            )
                        )
                    }
                } else {
                    val xpGained = 30 // Fixed XP for Apna College problems

                    val solution = existingSolution?.copy(
                        status = "SOLVED",
                        solvedAt = System.currentTimeMillis(),
                        lastEditedAt = System.currentTimeMillis()
                    ) ?: UserSolutionEntity(
                        problemId = problem.id.toInt(),
                        language = APNA_COLLEGE_LANGUAGE,
                        code = "",
                        status = "SOLVED",
                        solvedAt = System.currentTimeMillis(),
                        lastEditedAt = System.currentTimeMillis()
                    )

                    userSolutionDao.upsertSolution(solution)
                    
                    val levelEvent = preferencesRepository.awardXp(xpGained)
                    preferencesRepository.incrementProblemsSolved()
                    progressRepository.recordSolve(xpGained, 0)

                    _uiState.value = _uiState.value.copy(
                        levelUpEvent = levelEvent,
                        completionMessage = "✅ ${problem.title} marked as solved! +$xpGained XP"
                    )
                }
            }

            val refreshedSolvedIds = withContext(Dispatchers.IO) {
                userSolutionDao.getAllSolutions().first()
                    .filter { it.language == APNA_COLLEGE_LANGUAGE && it.status == "SOLVED" }
                    .map { it.problemId.toString() }
                    .toSet()
            }

            _uiState.update { currentState ->
                currentState.copy(
                    solvedProblemIds = refreshedSolvedIds,
                    completionBusyId = null,
                    completionMessage = currentState.completionMessage
                        ?: if (wasSolved) "${problem.title} marked unsolved" else null
                )
            }
        }
    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tabIndex)
    }

    private fun loadApnaCollegeProblems(): List<ApnaCollegeProblemItem> {
        val reader = BufferedReader(
            InputStreamReader(
                application.assets.open("ApnaCollege.csv")
            )
        )

        val problems = mutableListOf<ApnaCollegeProblemItem>()
        var lineNumber = 0

        reader.useLines { lines ->
            lines.forEach { line ->
                lineNumber++
                
                // Skip header rows (first 12 lines based on the CSV structure)
                if (lineNumber <= 12) return@forEach
                
                val parts = line.split(",")
                
                // Ensure we have enough columns and the question name is not empty
                if (parts.size >= 7 && parts[2].isNotBlank()) {
                    val topic = parts[0].trim()
                    val title = parts[2].trim()
                    val url = parts[3].trim()
                    val companies = if (parts.size > 4) parts[4].trim() else ""
                    val remarks = if (parts.size > 6) parts[6].trim() else ""
                    
                    // Only add if we have a valid title and URL
                    if (title.isNotEmpty() && url.isNotEmpty()) {
                        problems.add(
                            ApnaCollegeProblemItem(
                                id = lineNumber.toString(),
                                title = title,
                                topic = topic,
                                url = url,
                                companies = companies,
                                remarks = remarks
                            )
                        )
                    }
                }
            }
        }

        return problems
    }
}

// Made with Bob
