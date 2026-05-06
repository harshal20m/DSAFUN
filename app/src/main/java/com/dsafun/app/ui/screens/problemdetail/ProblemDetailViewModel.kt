package com.dsafun.app.ui.screens.problemdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.usecase.GetProblemDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProblemDetailUiState(
    val problem: Problem? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val revealedHints: Set<Int> = emptySet()
)

@HiltViewModel
class ProblemDetailViewModel @Inject constructor(
    private val getProblemDetailUseCase: GetProblemDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val problemId: Int = checkNotNull(savedStateHandle["problemId"])

    private val _uiState = MutableStateFlow(ProblemDetailUiState())
    val uiState: StateFlow<ProblemDetailUiState> = _uiState.asStateFlow()

    init {
        loadProblemDetail()
    }

    private fun loadProblemDetail() {
        viewModelScope.launch {
            getProblemDetailUseCase(problemId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
                .collect { problem ->
                    _uiState.value = _uiState.value.copy(
                        problem = problem,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }

    fun revealHint(index: Int) {
        _uiState.value = _uiState.value.copy(
            revealedHints = _uiState.value.revealedHints + index
        )
    }
}

// Made with Bob
