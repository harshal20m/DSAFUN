package com.dsafun.app.ui.screens.streak

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsafun.app.data.local.entity.BadgeEntity
import com.dsafun.app.data.local.entity.DailyProgressEntity
import com.dsafun.app.data.repository.BadgeRepository
import com.dsafun.app.data.repository.ProgressRepository
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for Streak Screen
 */
@HiltViewModel
class StreakViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val badgeRepository: BadgeRepository,
    private val preferencesDataStore: UserPreferencesDataStore
) : ViewModel() {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    private val _uiState = MutableStateFlow(StreakUiState())
    val uiState: StateFlow<StreakUiState> = _uiState.asStateFlow()
    
    init {
        loadStreakData()
    }
    
    private fun loadStreakData() {
        viewModelScope.launch {
            combine(
                preferencesDataStore.currentStreak,
                preferencesDataStore.longestStreak,
                preferencesDataStore.totalXp,
                preferencesDataStore.currentLevel,
                preferencesDataStore.freezeTokens,
                preferencesDataStore.dailyGoal,
                badgeRepository.getAllBadges()
            ) { flows: Array<Any> ->
                val currentStreak = flows[0] as Int
                val longestStreak = flows[1] as Int
                val totalXp = flows[2] as Int
                val currentLevel = flows[3] as Int
                val freezeTokens = flows[4] as Int
                val dailyGoal = flows[5] as Int
                val badges = flows[6] as List<BadgeEntity>
                StreakUiState(
                    currentStreak = currentStreak,
                    longestStreak = longestStreak,
                    totalXp = totalXp,
                    currentLevel = currentLevel,
                    xpForNextLevel = preferencesDataStore.getXpForNextLevel(totalXp, currentLevel),
                    freezeTokens = freezeTokens,
                    dailyGoal = dailyGoal,
                    badges = badges,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
                loadMonthlyCalendar(state.selectedMonth)
            }
        }
    }
    
    private suspend fun loadMonthlyCalendar(month: YearMonth) {
        val startDate = "${month.year}-${month.month.toString().padStart(2, '0')}-01"
        val calendar = Calendar.getInstance()
        calendar.set(month.year, month.month - 1, 1)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        
        // Get all progress for the month
        val progressList = mutableListOf<DailyProgressEntity?>()
        for (day in 1..daysInMonth) {
            val date = "${month.year}-${month.month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
            val progress = progressRepository.getTodayProgress().first() // This needs to be fixed to get specific date
            progressList.add(progress)
        }
        
        _uiState.update { it.copy(monthlyProgress = progressList) }
    }
    
    fun onMonthChanged(offset: Int) {
        val current = _uiState.value.selectedMonth
        val calendar = Calendar.getInstance()
        calendar.set(current.year, current.month - 1, 1)
        calendar.add(Calendar.MONTH, offset)
        
        val newMonth = YearMonth(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) + 1
        )
        
        _uiState.update { it.copy(selectedMonth = newMonth) }
        viewModelScope.launch {
            loadMonthlyCalendar(newMonth)
        }
    }
    
    fun onDailyGoalChanged(goal: Int) {
        viewModelScope.launch {
            preferencesDataStore.setDailyGoal(goal)
        }
    }
    
    fun onUseFreezeToken() {
        viewModelScope.launch {
            val success = progressRepository.useFreezeToken()
            if (success) {
                _uiState.update { it.copy(
                    showFreezeTokenSuccess = true
                ) }
                // Reload calendar to show updated streak
                loadMonthlyCalendar(_uiState.value.selectedMonth)
            }
        }
    }
    
    fun onDismissFreezeTokenSuccess() {
        _uiState.update { it.copy(showFreezeTokenSuccess = false) }
    }
    
    fun onDaySelected(date: String) {
        _uiState.update { it.copy(selectedDate = date) }
    }
    
    fun onDismissDateDetail() {
        _uiState.update { it.copy(selectedDate = null) }
    }
    
    fun onBadgeSelected(badge: BadgeEntity) {
        _uiState.update { it.copy(selectedBadge = badge) }
    }
    
    fun onDismissBadgeDetail() {
        _uiState.update { it.copy(selectedBadge = null) }
    }
}

/**
 * UI State for Streak Screen
 */
data class StreakUiState(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalXp: Int = 0,
    val currentLevel: Int = 1,
    val xpForNextLevel: Int = 500,
    val freezeTokens: Int = 2,
    val dailyGoal: Int = 3,
    val badges: List<BadgeEntity> = emptyList(),
    val selectedMonth: YearMonth = YearMonth.current(),
    val monthlyProgress: List<DailyProgressEntity?> = emptyList(),
    val selectedDate: String? = null,
    val selectedBadge: BadgeEntity? = null,
    val showFreezeTokenSuccess: Boolean = false,
    val isLoading: Boolean = true
)

/**
 * Represents a year and month
 */
data class YearMonth(
    val year: Int,
    val month: Int // 1-12
) {
    companion object {
        fun current(): YearMonth {
            val calendar = Calendar.getInstance()
            return YearMonth(
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH) + 1
            )
        }
    }
    
    fun getDisplayName(): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
        return "$monthName $year"
    }
}

// Made with Bob