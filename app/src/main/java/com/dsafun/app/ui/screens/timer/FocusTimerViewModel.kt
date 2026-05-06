package com.dsafun.app.ui.screens.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsafun.app.data.local.dao.FocusSessionDao
import com.dsafun.app.data.local.entity.FocusSessionEntity
import com.dsafun.app.domain.model.FocusSession
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.model.SessionType
import com.dsafun.app.domain.model.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class FocusTimerUiState(
    val timerState: TimerState = TimerState.IDLE,
    val sessionType: SessionType = SessionType.PRACTICE_25,
    val totalSeconds: Long = SessionType.PRACTICE_25.getDurationSeconds(),
    val remainingSeconds: Long = SessionType.PRACTICE_25.getDurationSeconds(),
    val currentSession: Int = 1,
    val targetSessions: Int = 4,
    val linkedProblem: Problem? = null,
    val todaySessions: List<FocusSession> = emptyList(),
    val totalMinutesToday: Long = 0,
    val customDurationMinutes: Int = 25,
    val breakMessage: String = ""
)

@HiltViewModel
class FocusTimerViewModel @Inject constructor(
    private val focusSessionDao: FocusSessionDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(FocusTimerUiState())
    val uiState: StateFlow<FocusTimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var sessionStartTime: Long = 0

    private val breakMessages = listOf(
        "Great work! Take a breather 🌟",
        "You're doing amazing! Rest up 💪",
        "Time to recharge your brain 🧠",
        "Stretch and relax for a moment 🧘",
        "Well done! Enjoy your break ☕",
        "Keep up the momentum! Quick break 🚀",
        "Excellent focus! Time to rest 🎯",
        "You're on fire! Cool down a bit 🔥",
        "Nice session! Refresh yourself 🌊",
        "Fantastic! Take it easy for a moment 🌈"
    )

    init {
        loadTodaySessions()
    }

    private fun loadTodaySessions() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = calendar.timeInMillis

            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val endOfDay = calendar.timeInMillis

            combine(
                focusSessionDao.getSessionsForDate(startOfDay, endOfDay),
                focusSessionDao.getTotalFocusTimeForDate(startOfDay, endOfDay)
            ) { sessions, totalSeconds ->
                Pair(sessions.map { it.toFocusSession() }, totalSeconds / 60)
            }.collect { (sessions, totalMinutes) ->
                _uiState.value = _uiState.value.copy(
                    todaySessions = sessions,
                    totalMinutesToday = totalMinutes
                )
            }
        }
    }

    fun onSessionTypeSelected(sessionType: SessionType) {
        if (_uiState.value.timerState != TimerState.IDLE) return

        val duration = if (sessionType == SessionType.CUSTOM) {
            _uiState.value.customDurationMinutes * 60L
        } else {
            sessionType.getDurationSeconds()
        }

        _uiState.value = _uiState.value.copy(
            sessionType = sessionType,
            totalSeconds = duration,
            remainingSeconds = duration
        )
    }

    fun onCustomDurationSet(minutes: Int) {
        _uiState.value = _uiState.value.copy(
            customDurationMinutes = minutes,
            totalSeconds = minutes * 60L,
            remainingSeconds = minutes * 60L
        )
    }

    fun onProblemLinked(problem: Problem?) {
        _uiState.value = _uiState.value.copy(linkedProblem = problem)
    }

    fun onStartTimer() {
        sessionStartTime = System.currentTimeMillis()
        _uiState.value = _uiState.value.copy(timerState = TimerState.RUNNING)
        startTicker()
    }

    fun onPauseTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(timerState = TimerState.PAUSED)
    }

    fun onResumeTimer() {
        _uiState.value = _uiState.value.copy(timerState = TimerState.RUNNING)
        startTicker()
    }

    fun onStopTimer() {
        timerJob?.cancel()
        
        // Save session if > 5 minutes
        val elapsedSeconds = _uiState.value.totalSeconds - _uiState.value.remainingSeconds
        if (elapsedSeconds >= 300) { // 5 minutes
            saveSession(elapsedSeconds)
        }

        resetTimer()
    }

    fun onSkipBreak() {
        timerJob?.cancel()
        
        if (_uiState.value.currentSession >= _uiState.value.targetSessions) {
            // All sessions complete
            _uiState.value = _uiState.value.copy(
                timerState = TimerState.COMPLETED,
                currentSession = 1
            )
        } else {
            // Start next session
            _uiState.value = _uiState.value.copy(
                timerState = TimerState.IDLE,
                remainingSeconds = _uiState.value.totalSeconds
            )
        }
    }

    private fun startTicker() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            flow {
                while (true) {
                    emit(Unit)
                    delay(1000)
                }
            }.collect {
                val remaining = _uiState.value.remainingSeconds - 1

                if (remaining <= 0) {
                    onTimerComplete()
                } else {
                    _uiState.value = _uiState.value.copy(remainingSeconds = remaining)
                }
            }
        }
    }

    private fun onTimerComplete() {
        timerJob?.cancel()

        // Save completed session
        saveSession(_uiState.value.totalSeconds)

        val currentSession = _uiState.value.currentSession

        if (currentSession >= _uiState.value.targetSessions) {
            // All sessions complete - long break
            startBreak(15 * 60L, "All sessions complete! Take a long break 🎉")
        } else {
            // Short break
            startBreak(5 * 60L, breakMessages.random())
            _uiState.value = _uiState.value.copy(
                currentSession = currentSession + 1
            )
        }
    }

    private fun startBreak(durationSeconds: Long, message: String) {
        _uiState.value = _uiState.value.copy(
            timerState = TimerState.BREAK,
            remainingSeconds = durationSeconds,
            breakMessage = message
        )
        startBreakTicker()
    }

    private fun startBreakTicker() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            flow {
                while (true) {
                    emit(Unit)
                    delay(1000)
                }
            }.collect {
                val remaining = _uiState.value.remainingSeconds - 1

                if (remaining <= 0) {
                    onBreakComplete()
                } else {
                    _uiState.value = _uiState.value.copy(remainingSeconds = remaining)
                }
            }
        }
    }

    private fun onBreakComplete() {
        timerJob?.cancel()

        if (_uiState.value.currentSession > _uiState.value.targetSessions) {
            // All done
            _uiState.value = _uiState.value.copy(
                timerState = TimerState.COMPLETED,
                currentSession = 1
            )
        } else {
            // Ready for next session
            _uiState.value = _uiState.value.copy(
                timerState = TimerState.IDLE,
                remainingSeconds = _uiState.value.totalSeconds
            )
        }
    }

    private fun saveSession(durationSeconds: Long) {
        viewModelScope.launch {
            val session = FocusSessionEntity(
                linkedProblemId = _uiState.value.linkedProblem?.id,
                durationSeconds = durationSeconds,
                sessionType = _uiState.value.sessionType.name,
                completedAt = System.currentTimeMillis()
            )
            focusSessionDao.insertSession(session)
        }
    }

    private fun resetTimer() {
        _uiState.value = _uiState.value.copy(
            timerState = TimerState.IDLE,
            remainingSeconds = _uiState.value.totalSeconds,
            currentSession = 1
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

private fun FocusSessionEntity.toFocusSession() = FocusSession(
    id = id,
    linkedProblemId = linkedProblemId,
    durationSeconds = durationSeconds,
    sessionType = sessionType,
    completedAt = completedAt
)

// Made with Bob
