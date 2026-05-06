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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                
                // 1b. Sessions Count Selector
                item {
                    SessionsCountSelector(
                        sessionsCount = uiState.targetSessions,
                        enabled = uiState.timerState == TimerState.IDLE,
                        onSessionsCountChanged = viewModel::onTargetSessionsChanged
                    )
                }

                // 2. Link Problem Card
                item {
                    LinkedProblemCard(
                        problem = uiState.linkedProblem,
                        enabled = uiState.timerState == TimerState.IDLE,
                        onLinkProblem = viewModel::showProblemPicker
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
            
            // Note: FloatingTimerWidget is now shown globally in MainActivity above bottom nav
            // No need to show it here on the Timer screen itself
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
    
    // Problem Picker Dialog
    if (uiState.showProblemPicker) {
        ProblemPickerDialog(
            problems = uiState.allProblems,
            onDismiss = viewModel::hideProblemPicker,
            onProblemSelected = viewModel::onProblemLinked
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
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Session Type",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SessionType.values().forEach { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { if (enabled) onTypeSelected(type) },
                    label = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = when (type) {
                                    SessionType.QUICK_15 -> "Quick"
                                    SessionType.PRACTICE_25 -> "Practice"
                                    SessionType.DEEP_50 -> "Deep Focus"
                                    SessionType.CUSTOM -> "Custom"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selectedType == type) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                            Text(
                                text = "${type.durationMinutes}m",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selectedType == type)
                                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    },
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Composable
fun SessionsCountSelector(
    sessionsCount: Int,
    enabled: Boolean,
    onSessionsCountChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Number of Sessions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (sessionsCount > 1) onSessionsCountChanged(sessionsCount - 1) },
                    enabled = enabled && sessionsCount > 1
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease sessions",
                        tint = if (enabled && sessionsCount > 1)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
                
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.width(60.dp)
                ) {
                    Text(
                        text = "$sessionsCount",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                IconButton(
                    onClick = { if (sessionsCount < 10) onSessionsCountChanged(sessionsCount + 1) },
                    enabled = enabled && sessionsCount < 10
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase sessions",
                        tint = if (enabled && sessionsCount < 10)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }
        }
        
        Text(
            text = "Choose 1-10 focus sessions with breaks in between",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
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
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (problem != null) 2.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Icon(
                    imageVector = if (problem != null) Icons.Default.CheckCircle else Icons.Default.Link,
                    contentDescription = null,
                    tint = if (problem != null)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = problem?.title ?: "Link a Problem",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (problem != null) FontWeight.SemiBold else FontWeight.Medium
                        ),
                        color = if (problem != null)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                    
                    if (problem != null) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = when (problem.difficulty) {
                                    "Easy" -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                                    "Medium" -> Color(0xFFFF9800).copy(alpha = 0.15f)
                                    else -> Color(0xFFF44336).copy(alpha = 0.15f)
                                }
                            ) {
                                Text(
                                    text = problem.difficulty,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = when (problem.difficulty) {
                                        "Easy" -> Color(0xFF4CAF50)
                                        "Medium" -> Color(0xFFFF9800)
                                        else -> Color(0xFFF44336)
                                    },
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            
                            Text(
                                text = problem.topic,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
            
            if (problem == null) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
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

            Button(
                onClick = onSkip,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Skip Break")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemPickerDialog(
    problems: List<com.dsafun.app.domain.model.Problem>,
    onDismiss: () -> Unit,
    onProblemSelected: (com.dsafun.app.domain.model.Problem) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    
    val filteredProblems = remember(searchQuery, selectedDifficulty, problems) {
        problems.filter { problem ->
            val matchesSearch = problem.title.contains(searchQuery, ignoreCase = true) ||
                    problem.topic.contains(searchQuery, ignoreCase = true)
            val matchesDifficulty = selectedDifficulty == null || problem.difficulty == selectedDifficulty
            matchesSearch && matchesDifficulty
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(0.95f)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Link a Problem",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
                
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    placeholder = { Text("Search problems...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Difficulty Filter
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedDifficulty == null,
                        onClick = { selectedDifficulty = null },
                        label = { Text("All") }
                    )
                    FilterChip(
                        selected = selectedDifficulty == "Easy",
                        onClick = { selectedDifficulty = "Easy" },
                        label = { Text("Easy") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                        )
                    )
                    FilterChip(
                        selected = selectedDifficulty == "Medium",
                        onClick = { selectedDifficulty = "Medium" },
                        label = { Text("Medium") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                        )
                    )
                    FilterChip(
                        selected = selectedDifficulty == "Hard",
                        onClick = { selectedDifficulty = "Hard" },
                        label = { Text("Hard") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Problems List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (filteredProblems.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No problems found",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(filteredProblems) { problem ->
                            ProblemPickerItem(
                                problem = problem,
                                onClick = {
                                    onProblemSelected(problem)
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemPickerItem(
    problem: com.dsafun.app.domain.model.Problem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
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
                    text = problem.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = problem.topic,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (problem.difficulty) {
                    "Easy" -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    "Medium" -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                    else -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                }
            ) {
                Text(
                    text = problem.difficulty,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = when (problem.difficulty) {
                        "Easy" -> MaterialTheme.colorScheme.secondary
                        "Medium" -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.error
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
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
