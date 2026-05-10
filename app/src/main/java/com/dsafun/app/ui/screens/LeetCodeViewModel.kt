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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

private const val LEETCODE_LANGUAGE = "LEETCODE"

data class LeetCodeProblemItem(
    val id: String,
    val title: String,
    val difficulty: String,
    val url: String,
    val acceptanceRate: String,
    val topics: List<String>,
    val description: String
)

data class LeetCodeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val problems: List<LeetCodeProblemItem> = emptyList(),
    val solvedProblemIds: Set<String> = emptySet(),
    val completionBusyId: String? = null,
    val completionMessage: String? = null,
    val levelUpEvent: LevelUpEvent? = null
)

@HiltViewModel
class LeetCodeViewModel @Inject constructor(
    private val application: Application,
    private val userSolutionDao: UserSolutionDao,
    private val preferencesRepository: PreferencesRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeetCodeUiState())
    val uiState: StateFlow<LeetCodeUiState> = _uiState.asStateFlow()

    init {
        loadProblems()
    }

    fun loadProblems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            runCatching {
                withContext(Dispatchers.IO) {
                    val problems = loadLeetCodeProblems()
                    val solvedIds = userSolutionDao.getAllSolutions().first()
                        .filter { it.language == LEETCODE_LANGUAGE && it.status == "SOLVED" }
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
                    error = throwable.message ?: "Failed to load LeetCode problems"
                )
            }
        }
    }

    fun toggleSolved(problem: LeetCodeProblemItem) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                completionBusyId = problem.id,
                completionMessage = null,
                levelUpEvent = null
            )

            val wasSolved = _uiState.value.solvedProblemIds.contains(problem.id)

            withContext(Dispatchers.IO) {
                val existingSolution = userSolutionDao
                    .getSolutionForProblem(problem.id.toInt(), LEETCODE_LANGUAGE)
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
                    val xpGained = when (problem.difficulty) {
                        "Easy" -> 25
                        "Medium" -> 50
                        "Hard" -> 75
                        else -> 40
                    }

                    val updatedSolution = (existingSolution ?: UserSolutionEntity(
                        problemId = problem.id.toInt(),
                        language = LEETCODE_LANGUAGE,
                        code = "",
                        status = "ATTEMPTED",
                        timeTakenSeconds = 0,
                        attemptCount = 0,
                        lastEditedAt = System.currentTimeMillis()
                    )).copy(
                        code = problem.url,
                        status = "SOLVED",
                        attemptCount = maxOf(existingSolution?.attemptCount ?: 0, 1),
                        solvedAt = System.currentTimeMillis(),
                        lastEditedAt = System.currentTimeMillis()
                    )

                    userSolutionDao.upsertSolution(updatedSolution)
                    val levelEvent = preferencesRepository.awardXp(xpGained)
                    preferencesRepository.incrementProblemsSolved()
                    progressRepository.recordSolve(xpGained, 0)

                    _uiState.value = _uiState.value.copy(
                        levelUpEvent = levelEvent,
                        completionMessage = "${problem.title} marked solved (+$xpGained XP)"
                    )
                }
            }

            val refreshedSolvedIds = withContext(Dispatchers.IO) {
                userSolutionDao.getAllSolutions().first()
                    .filter { it.language == LEETCODE_LANGUAGE && it.status == "SOLVED" }
                    .map { it.problemId.toString() }
                    .toSet()
            }

            _uiState.value = _uiState.value.copy(
                solvedProblemIds = refreshedSolvedIds,
                completionBusyId = null,
                completionMessage = _uiState.value.completionMessage
                    ?: if (wasSolved) "${problem.title} marked unsolved" else null
            )
        }
    }

    private fun loadLeetCodeProblems(): List<LeetCodeProblemItem> {
        val reader = BufferedReader(
            InputStreamReader(
                application.assets.open("leetcode_problem.csv")
            )
        )

        val rows = mutableListOf<LeetCodeProblemItem>()
        val fields = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var isFirstRecord = true

        reader.use { bufferedReader ->
            while (true) {
                val nextChar = bufferedReader.read()
                if (nextChar == -1) break

                val char = nextChar.toChar()
                when {
                    char == '"' -> {
                        if (inQuotes) {
                            bufferedReader.mark(1)
                            val peek = bufferedReader.read()
                            if (peek == '"'.code) {
                                current.append('"')
                            } else {
                                inQuotes = false
                                if (peek != -1) bufferedReader.reset()
                            }
                        } else {
                            inQuotes = true
                        }
                    }

                    char == ',' && !inQuotes -> {
                        fields.add(current.toString())
                        current.clear()
                    }

                    (char == '\n' || char == '\r') && !inQuotes -> {
                        if (char == '\r') {
                            bufferedReader.mark(1)
                            val peek = bufferedReader.read()
                            if (peek != '\n'.code && peek != -1) bufferedReader.reset()
                        }

                        fields.add(current.toString())
                        current.clear()

                        if (isFirstRecord) {
                            isFirstRecord = false
                        } else if (fields.size >= 15) {
                            rows.add(
                                LeetCodeProblemItem(
                                    id = fields.getOrElse(1) { "" }.trim(),
                                    title = fields.getOrElse(2) { "" }.trim(),
                                    description = fields.getOrElse(3) { "" }
                                        .replace("`", "")
                                        .replace("\n", " ")
                                        .replace(Regex("\\s+"), " ")
                                        .trim(),
                                    difficulty = fields.getOrElse(5) { "" }.trim(),
                                    acceptanceRate = fields.getOrElse(7) { "" }.trim(),
                                    url = fields.getOrElse(9) { "" }.trim(),
                                    topics = fields.getOrElse(14) { "" }
                                        .split(",")
                                        .map { it.trim() }
                                        .filter { it.isNotBlank() }
                                )
                            )
                        }

                        fields.clear()
                    }

                    else -> current.append(char)
                }
            }
        }

        if (current.isNotEmpty() || fields.isNotEmpty()) {
            fields.add(current.toString())
            if (!isFirstRecord && fields.size >= 15) {
                rows.add(
                    LeetCodeProblemItem(
                        id = fields.getOrElse(1) { "" }.trim(),
                        title = fields.getOrElse(2) { "" }.trim(),
                        description = fields.getOrElse(3) { "" }
                            .replace("`", "")
                            .replace("\n", " ")
                            .replace(Regex("\\s+"), " ")
                            .trim(),
                        difficulty = fields.getOrElse(5) { "" }.trim(),
                        acceptanceRate = fields.getOrElse(7) { "" }.trim(),
                        url = fields.getOrElse(9) { "" }.trim(),
                        topics = fields.getOrElse(14) { "" }
                            .split(",")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                    )
                )
            }
        }

        return rows.filter { it.title.isNotBlank() && it.url.startsWith("http") }
    }
}

// Made with Bob
