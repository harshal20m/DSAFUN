package com.dsafun.app.ui.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.domain.model.SessionType
import com.dsafun.app.domain.model.TimerState
import com.dsafun.app.ui.components.timer.BreathingAnimation
import com.dsafun.app.ui.components.timer.CircularCountdown
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusTimerScreen(
    viewModel: FocusTimerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCustomDurationDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus Timer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Session Type Chips
                item {
                    SessionTypeSelector(
                        selectedType = uiState.sessionType,
                        enabled = uiState.timerState == TimerState.IDLE,
                        onTypeSelected = { type ->
                            if (type == SessionType.CUSTOM) {
                                showCustomDurationDialog = true
                            }
                            viewModel.onSessionTypeSelected(type)
                        }
                    )
                }

                // 2. Link Problem Card
                item {
                    LinkedProblemCard(
                        problem = uiState.linkedProblem,
                        enabled = uiState.timerState == TimerState.IDLE,
                        onLinkProblem = { /* TODO: Implement problem picker */ }
                    )
                }

                // 3. Main Circular Timer
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularCountdown(
                            remainingSeconds = uiState.remainingSeconds,
                            totalSeconds = uiState.totalSeconds,
                            timerState = uiState.timerState,
                            currentSession = uiState.currentSession,
                            targetSessions = uiState.targetSessions
                        )
                    }
                }

                // 4. Control Buttons
                item {
                    TimerControls(
                        timerState = uiState.timerState,
                        onStart = viewModel::onStartTimer,
                        onPause = viewModel::onPauseTimer,
                        onResume = viewModel::onResumeTimer,
                        onStop = viewModel::onStopTimer,
                        onSkipBreak = viewModel::onSkipBreak
                    )
                }

                // 5. Today's Sessions
                item {
                    TodaysSessionsSection(
                        sessions = uiState.todaySessions,
                        totalMinutes = uiState.totalMinutesToday
                    )
                }
            }

            // Break Overlay
            if (uiState.timerState == TimerState.BREAK) {
                BreakOverlay(
                    message = uiState.breakMessage,
                    remainingSeconds = uiState.remainingSeconds,
                    onSkip = viewModel::onSkipBreak
                )
            }

            // Completion Overlay
            if (uiState.timerState == TimerState.COMPLETED) {
                CompletionOverlay(
                    onDismiss = viewModel::onStopTimer
                )
            }
        }
    }

    // Custom Duration Dialog
    if (showCustomDurationDialog) {
        CustomDurationDialog(
            currentMinutes = uiState.customDurationMinutes,
            onDismiss = { showCustomDurationDialog = false },
            onConfirm = { minutes ->
                viewModel.onCustomDurationSet(minutes)
                showCustomDurationDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionTypeSelector(
    selectedType: SessionType,
    enabled: Boolean,
    onTypeSelected: (SessionType) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Session Type",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SessionType.values().forEach { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { if (enabled) onTypeSelected(type) },
                    label = { Text(type.displayName) },
                    enabled = enabled,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun LinkedProblemCard(
    problem: com.dsafun.app.domain.model.Problem?,
    enabled: Boolean,
    onLinkProblem: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onLinkProblem),
        colors = CardDefaults.cardColors(
            containerColor = if (problem != null) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (problem != null) "Linked Problem" else "Link a Problem",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = problem?.title ?: "Optional: Focus on a specific problem",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (problem != null) FontWeight.Medium else FontWeight.Normal
                )
            }
            Icon(
                imageVector = if (problem != null) Icons.Default.CheckCircle else Icons.Default.Add,
                contentDescription = null,
                tint = if (problem != null)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TimerControls(
    timerState: TimerState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    onSkipBreak: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when (timerState) {
            TimerState.IDLE -> {
                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start Session")
                }
            }
            TimerState.RUNNING -> {
                OutlinedButton(
                    onClick = onPause,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Pause, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pause")
                }
                OutlinedButton(
                    onClick = onStop,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Stop, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Stop")
                }
            }
            TimerState.PAUSED -> {
                Button(
                    onClick = onResume,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resume")
                }
                OutlinedButton(
                    onClick = onStop,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Stop, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Stop")
                }
            }
            TimerState.BREAK -> {
                Button(
                    onClick = onSkipBreak,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.SkipNext, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Skip Break")
                }
            }
            TimerState.COMPLETED -> {
                // Handled by overlay
            }
        }
    }
}

@Composable
fun TodaysSessionsSection(
    sessions: List<com.dsafun.app.domain.model.FocusSession>,
    totalMinutes: Long
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Sessions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${totalMinutes / 60}h ${totalMinutes % 60}m focused",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            if (sessions.isEmpty()) {
                Text(
                    text = "No sessions yet today. Start your first one!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                sessions.take(5).forEach { session ->
                    SessionItem(session = session)
                }
            }
        }
    }
}

@Composable
fun SessionItem(session: com.dsafun.app.domain.model.FocusSession) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = SessionType.valueOf(session.sessionType).displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formatTimestamp(session.completedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = "${session.durationSeconds / 60}m",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BreakOverlay(
    message: String,
    remainingSeconds: Long,
    onSkip: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            BreathingAnimation()

            Text(
                text = message,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Resume in ${formatTime(remainingSeconds)}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Button(onClick = onSkip) {
                Text("Skip Break")
            }
        }
    }
}

@Composable
fun CompletionOverlay(
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(32.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🎉",
                    fontSize = 64.sp
                )
                Text(
                    text = "All Sessions Complete!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Great work! You've completed all focus sessions.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun CustomDurationDialog(
    currentMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var minutes by remember { mutableStateOf(currentMinutes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Custom Duration") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Set your custom focus duration (5-120 minutes)")
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (minutes > 5) minutes -= 5 }
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    
                    Text(
                        text = "$minutes minutes",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(
                        onClick = { if (minutes < 120) minutes += 5 }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
                
                Slider(
                    value = minutes.toFloat(),
                    onValueChange = { minutes = it.toInt() },
                    valueRange = 5f..120f,
                    steps = 22 // 5-minute increments
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(minutes) }) {
                Text("Set")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

// Made with Bob
