package com.dsafun.app.ui.screens.codeeditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsafun.app.data.local.dao.UserSolutionDao
import com.dsafun.app.data.local.entity.BadgeEntity
import com.dsafun.app.data.local.entity.UserSolutionEntity
import com.dsafun.app.data.local.datastore.LevelUpEvent
import com.dsafun.app.data.repository.PreferencesRepository
import com.dsafun.app.data.repository.ProgressRepository
import com.dsafun.app.domain.executor.TestCaseResult
import com.dsafun.app.domain.model.Language
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.usecase.GetProblemDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CodeEditorUiState(
    val problem: Problem? = null,
    val selectedLanguage: Language = Language.KOTLIN,
    val currentCode: String = "// Loading...",
    val timerSeconds: Long = 0,
    val isTimerRunning: Boolean = false,
    val isDraftSaved: Boolean = true,
    val isLoading: Boolean = true,
    val showLanguageSwitchDialog: Boolean = false,
    val pendingLanguage: Language? = null,
    // Submission fields
    val isRunning: Boolean = false,
    val testCaseResults: List<TestCaseResult>? = null,
    val showTestResults: Boolean = false,
    val submissionStatus: String? = null, // "SOLVED", "ATTEMPTED", "FAILED"
    val xpGained: Int? = null,
    val levelUpEvent: LevelUpEvent? = null,
    val showSubmissionResult: Boolean = false,
    val submissionError: String? = null,
    val newBadges: List<BadgeEntity> = emptyList(),
    // Preferences
    val preferredLanguage: String = "",
    val fontSizePreference: String = "MEDIUM",
    val availableLanguages: List<Language> = Language.entries,
    // Timer settings
    val showSetTimerDialog: Boolean = false,
    val customTimerMinutes: Int = 25
)

@HiltViewModel
class CodeEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProblemDetailUseCase: GetProblemDetailUseCase,
    private val userSolutionDao: UserSolutionDao,
    internal val preferencesRepository: PreferencesRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val problemId: Int = checkNotNull(savedStateHandle["problemId"])

    private val _uiState = MutableStateFlow(CodeEditorUiState())
    val uiState: StateFlow<CodeEditorUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var autoSaveJob: Job? = null

    init {
        loadProblem()
        loadPreferences()
        startTimer()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            combine(
                preferencesRepository.preferredLanguage,
                preferencesRepository.fontSizePreference
            ) { preferredLang, fontSize ->
                Pair(preferredLang, fontSize)
            }.collect { (preferredLang, fontSize) ->
                val currentLang = _uiState.value.selectedLanguage
                
                // Filter available languages based on preference
                val availableLanguages = if (preferredLang.isNotBlank() && preferredLang != "NONE") {
                    Language.entries.filter { it.name == preferredLang }
                } else {
                    Language.entries
                }
                
                // Change language if preference is set and different from current
                val newLanguage = if (preferredLang.isNotBlank() && preferredLang != "NONE") {
                    Language.entries.find { it.name == preferredLang } ?: currentLang
                } else {
                    currentLang
                }
                
                _uiState.value = _uiState.value.copy(
                    preferredLanguage = preferredLang,
                    fontSizePreference = fontSize,
                    availableLanguages = availableLanguages,
                    selectedLanguage = newLanguage
                )
                
                // Reload solution if language changed and problem is loaded
                if (newLanguage != currentLang && _uiState.value.problem != null) {
                    loadSavedSolution()
                }
            }
        }
    }

    private fun loadProblem() {
        viewModelScope.launch {
            getProblemDetailUseCase(problemId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )
                }
                .collect { problem ->
                    _uiState.value = _uiState.value.copy(
                        problem = problem,
                        isLoading = false
                    )
                    problem?.let {
                        loadSavedSolution()
                    }
                }
        }
    }

    private fun loadSavedSolution() {
        viewModelScope.launch {
            val problem = _uiState.value.problem
            if (problem == null) {
                // Problem not loaded yet, wait
                return@launch
            }
            
            val solution = userSolutionDao.getSolutionForProblem(
                problemId = problemId,
                language = _uiState.value.selectedLanguage.name
            ).first()
            
            if (solution != null) {
                _uiState.value = _uiState.value.copy(
                    currentCode = solution.code,
                    timerSeconds = solution.timeTakenSeconds
                )
            } else {
                // Load starter code from problem
                val starterCode = problem.starterCode[_uiState.value.selectedLanguage.displayName]
                
                if (!starterCode.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        currentCode = starterCode
                    )
                } else {
                    // No starter code, provide default template
                    val defaultTemplate = when (_uiState.value.selectedLanguage) {
                        Language.KOTLIN -> "fun solution() {\n    // Write your code here\n}"
                        Language.JAVA -> "public class Solution {\n    public void solution() {\n        // Write your code here\n    }\n}"
                        Language.PYTHON -> "def solution():\n    # Write your code here\n    pass"
                        Language.JAVASCRIPT -> "function solution() {\n    // Write your code here\n}"
                        Language.CPP -> "#include <iostream>\nusing namespace std;\n\nint main() {\n    // Write your code here\n    return 0;\n}"
                    }
                    _uiState.value = _uiState.value.copy(
                        currentCode = defaultTemplate
                    )
                }
            }
        }
    }

    fun onCodeChanged(newCode: String) {
        _uiState.value = _uiState.value.copy(
            currentCode = newCode,
            isDraftSaved = false
        )
        scheduleAutoSave()
    }

    fun onLanguageSelected(language: Language) {
        if (language == _uiState.value.selectedLanguage) return
        
        // Show dialog if there's unsaved code
        if (_uiState.value.currentCode.isNotBlank()) {
            _uiState.value = _uiState.value.copy(
                showLanguageSwitchDialog = true,
                pendingLanguage = language
            )
        } else {
            switchLanguage(language)
        }
    }

    fun onLanguageSwitchConfirmed(keepCode: Boolean) {
        val newLanguage = _uiState.value.pendingLanguage ?: return
        
        if (!keepCode) {
            switchLanguage(newLanguage)
        } else {
            _uiState.value = _uiState.value.copy(
                selectedLanguage = newLanguage,
                showLanguageSwitchDialog = false,
                pendingLanguage = null
            )
        }
    }

    fun onLanguageSwitchDismissed() {
        _uiState.value = _uiState.value.copy(
            showLanguageSwitchDialog = false,
            pendingLanguage = null
        )
    }

    private fun switchLanguage(language: Language) {
        viewModelScope.launch {
            // Save current code before switching
            saveDraft()
            
            _uiState.value = _uiState.value.copy(
                selectedLanguage = language,
                showLanguageSwitchDialog = false,
                pendingLanguage = null
            )
            
            // Load solution for new language
            loadSavedSolution()
        }
    }

    fun saveDraft() {
        viewModelScope.launch {
            val solution = UserSolutionEntity(
                problemId = problemId,
                language = _uiState.value.selectedLanguage.name,
                code = _uiState.value.currentCode,
                status = "ATTEMPTED",
                timeTakenSeconds = _uiState.value.timerSeconds,
                attemptCount = 1,
                lastEditedAt = System.currentTimeMillis()
            )
            userSolutionDao.upsertSolution(solution)
            _uiState.value = _uiState.value.copy(isDraftSaved = true)
        }
    }

    fun resetCode() {
        val problem = _uiState.value.problem
        val starterCode = problem?.starterCode?.get(
            _uiState.value.selectedLanguage.displayName
        )
        
        val codeToReset = if (!starterCode.isNullOrBlank()) {
            starterCode
        } else {
            // No starter code, provide default template
            when (_uiState.value.selectedLanguage) {
                Language.KOTLIN -> "fun solution() {\n    // Write your code here\n}"
                Language.JAVA -> "public class Solution {\n    public void solution() {\n        // Write your code here\n    }\n}"
                Language.PYTHON -> "def solution():\n    # Write your code here\n    pass"
                Language.JAVASCRIPT -> "function solution() {\n    // Write your code here\n}"
                Language.CPP -> "#include <iostream>\nusing namespace std;\n\nint main() {\n    // Write your code here\n    return 0;\n}"
            }
        }
        
        _uiState.value = _uiState.value.copy(
            currentCode = codeToReset,
            isDraftSaved = false
        )
    }

    private fun scheduleAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(30_000) // 30 seconds
            saveDraft()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(isTimerRunning = true)
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    timerSeconds = _uiState.value.timerSeconds + 1
                )
            }
        }
    }
    
    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(isTimerRunning = false)
    }
    
    fun resumeTimer() {
        if (!_uiState.value.isTimerRunning) {
            startTimer()
        }
    }
    
    fun resetTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            timerSeconds = 0,
            isTimerRunning = false
        )
    }
    
    fun showSetTimerDialog() {
        _uiState.value = _uiState.value.copy(showSetTimerDialog = true)
    }
    
    fun hideSetTimerDialog() {
        _uiState.value = _uiState.value.copy(showSetTimerDialog = false)
    }
    
    fun setCustomTimer(minutes: Int) {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            customTimerMinutes = minutes,
            timerSeconds = 0,
            isTimerRunning = false,
            showSetTimerDialog = false
        )
        // Auto-start timer
        startTimer()
    }
    
    fun dismissTestResults() {
        _uiState.value = _uiState.value.copy(
            showTestResults = false
        )
    }

    fun dismissSubmissionResult() {
        _uiState.value = _uiState.value.copy(
            showSubmissionResult = false,
            testCaseResults = null,
            xpGained = null,
            levelUpEvent = null
        )
    }

    fun dismissLevelUp() {
        _uiState.value = _uiState.value.copy(
            levelUpEvent = null
        )
    }
    
    fun dismissBadgeCelebration() {
        _uiState.value = _uiState.value.copy(
            newBadges = emptyList()
        )
    }

    fun markAsSolved() {
        viewModelScope.launch {
            // Save as solved without running test cases
            val solution = UserSolutionEntity(
                problemId = problemId,
                language = _uiState.value.selectedLanguage.name,
                code = "", // No code saved, user solved it in OneCompiler
                status = "SOLVED",
                timeTakenSeconds = _uiState.value.timerSeconds,
                attemptCount = 1,
                lastEditedAt = System.currentTimeMillis()
            )
            userSolutionDao.upsertSolution(solution)
            
            // Award XP for solving
            val xpGained = 50 // Fixed XP for marking as solved
            val levelUpEvent = preferencesRepository.awardXp(xpGained)
            
            preferencesRepository.incrementProblemsSolved()
            
            // Record solve in daily progress (this updates today's solved count)
            val timeSpentMinutes = (_uiState.value.timerSeconds / 60).toInt()
            progressRepository.recordSolve(xpGained, timeSpentMinutes)
            
            // Update UI state
            _uiState.value = _uiState.value.copy(
                submissionStatus = "SOLVED",
                xpGained = xpGained,
                levelUpEvent = levelUpEvent,
                showSubmissionResult = true
            )
        }
    }
    

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        autoSaveJob?.cancel()
        // Save on exit
        viewModelScope.launch {
            saveDraft()
        }
    }
}

// Made with Bob