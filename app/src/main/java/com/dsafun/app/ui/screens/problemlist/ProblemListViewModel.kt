package com.dsafun.app.ui.screens.problemlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.model.ProblemFilter
import com.dsafun.app.domain.usecase.GetProblemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProblemListUiState(
    val problems: List<Problem> = emptyList(),
    val filter: ProblemFilter = ProblemFilter.EMPTY,
    val isSearchActive: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class ProblemListViewModel @Inject constructor(
    private val getProblemsUseCase: GetProblemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProblemListUiState())
    val uiState: StateFlow<ProblemListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadProblems()
    }

    fun onTopicSelected(topic: String?) {
        val newFilter = _uiState.value.filter.copy(topic = topic)
        updateFilter(newFilter)
    }

    fun onDifficultyToggled(difficulty: String) {
        val currentDifficulties = _uiState.value.filter.difficulties
        val newDifficulties = if (difficulty in currentDifficulties) {
            currentDifficulties - difficulty
        } else {
            currentDifficulties + difficulty
        }
        val newFilter = _uiState.value.filter.copy(difficulties = newDifficulties)
        updateFilter(newFilter)
    }

    fun onSearchQueryChanged(query: String) {
        val newFilter = _uiState.value.filter.copy(searchQuery = query)
        updateFilter(newFilter)
    }

    fun onSearchActiveChanged(isActive: Boolean) {
        _uiState.value = _uiState.value.copy(isSearchActive = isActive)
        if (!isActive && _uiState.value.filter.searchQuery.isNotEmpty()) {
            onSearchQueryChanged("")
        }
    }

    fun clearFilters() {
        updateFilter(ProblemFilter.EMPTY)
    }

    private fun updateFilter(filter: ProblemFilter) {
        _uiState.value = _uiState.value.copy(filter = filter)
        loadProblems()
    }

    private fun loadProblems() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getProblemsUseCase(_uiState.value.filter)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
                .collect { problems ->
                    _uiState.value = _uiState.value.copy(
                        problems = problems,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }
}

// Made with Bob
