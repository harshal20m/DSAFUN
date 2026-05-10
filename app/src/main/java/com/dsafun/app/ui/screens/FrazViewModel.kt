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

private const val FRAZ_LANGUAGE = "FRAZ_DSA"

data class FrazProblemItem(
    val id: String,
    val topic: String,
    val difficulty: String,
    val problemUrl: String,
    val editorialUrl: String
)

data class FrazUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val problems: List<FrazProblemItem> = emptyList(),
    val solvedProblemIds: Set<String> = emptySet(),
    val completionBusyId: String? = null,
    val completionMessage: String? = null,
    val levelUpEvent: LevelUpEvent? = null,
    val selectedTab: Int = 0 // 0 = All, 1 = Marked
)

@HiltViewModel
class FrazViewModel @Inject constructor(
    private val application: Application,
    private val userSolutionDao: UserSolutionDao,
    private val preferencesRepository: PreferencesRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FrazUiState())
    val uiState: StateFlow<FrazUiState> = _uiState.asStateFlow()

    init {
        loadProblems()
    }

    fun loadProblems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            runCatching {
                withContext(Dispatchers.IO) {
                    val problems = loadFrazProblems()
                    val solvedIds = userSolutionDao.getAllSolutions().first()
                        .filter { it.language == FRAZ_LANGUAGE && it.status == "SOLVED" }
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
                    error = throwable.message ?: "Failed to load Fraz DSA Sheet problems"
                )
            }
        }
    }

    fun toggleSolved(problem: FrazProblemItem) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                completionBusyId = problem.id,
                completionMessage = null,
                levelUpEvent = null
            )

            val wasSolved = _uiState.value.solvedProblemIds.contains(problem.id)

            withContext(Dispatchers.IO) {
                val existingSolution = userSolutionDao
                    .getSolutionForProblem(problem.id.toInt(), FRAZ_LANGUAGE)
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
                    val xpGained = 30 // Fixed XP for Fraz problems

                    val solution = existingSolution?.copy(
                        status = "SOLVED",
                        solvedAt = System.currentTimeMillis(),
                        lastEditedAt = System.currentTimeMillis()
                    ) ?: UserSolutionEntity(
                        problemId = problem.id.toInt(),
                        language = FRAZ_LANGUAGE,
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
                        completionMessage = "✅ Problem marked as solved! +$xpGained XP"
                    )
                }
            }

            val refreshedSolvedIds = withContext(Dispatchers.IO) {
                userSolutionDao.getAllSolutions().first()
                    .filter { it.language == FRAZ_LANGUAGE && it.status == "SOLVED" }
                    .map { it.problemId.toString() }
                    .toSet()
            }

            _uiState.update { currentState ->
                currentState.copy(
                    solvedProblemIds = refreshedSolvedIds,
                    completionBusyId = null,
                    completionMessage = currentState.completionMessage
                        ?: if (wasSolved) "Problem marked unsolved" else null
                )
            }
        }
    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tabIndex)
    }

    private fun loadFrazProblems(): List<FrazProblemItem> {
        val reader = BufferedReader(
            InputStreamReader(
                application.assets.open("DSASheetByFraz.csv")
            )
        )

        val problems = mutableListOf<FrazProblemItem>()
        var currentTopic = ""
        var currentDifficulty = ""
        var problemId = 0

        reader.useLines { lines ->
            lines.forEachIndexed { index, line ->
                // Skip first 7 header lines
                if (index < 7) return@forEachIndexed
                
                val parts = line.split(",")
                
                if (parts.size >= 2) {
                    val firstCol = parts[1].trim()
                    
                    // Check if this is a topic line (e.g., "Arrays", "RECURSION")
                    if (firstCol.isNotEmpty() && !firstCol.startsWith("http") && 
                        firstCol != "EASY" && firstCol != "MEDIUM" && firstCol != "HARD") {
                        currentTopic = firstCol
                        currentDifficulty = ""
                    }
                    // Check if this is a difficulty line
                    else if (firstCol == "EASY" || firstCol == "MEDIUM" || firstCol == "HARD") {
                        currentDifficulty = firstCol
                    }
                    // Check if this is a problem line (has URL)
                    else if (firstCol.startsWith("http")) {
                        problemId++
                        val problemUrl = firstCol
                        val editorialUrl = if (parts.size >= 3) parts[2].trim() else ""
                        
                        // Extract problem title from URL
                        val title = problemUrl
                            .substringAfter("leetcode.com/problems/")
                            .substringBefore("/")
                            .replace("-", " ")
                            .split(" ")
                            .joinToString(" ") { it.capitalize() }
                        
                        problems.add(
                            FrazProblemItem(
                                id = problemId.toString(),
                                topic = currentTopic,
                                difficulty = currentDifficulty.ifEmpty { "MEDIUM" },
                                problemUrl = problemUrl,
                                editorialUrl = editorialUrl
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
