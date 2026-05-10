package com.dsafun.app.ui.screens.codeeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.ui.components.SimpleCompilerWebView
import com.dsafun.app.ui.components.SubmissionResultOverlay
import com.dsafun.app.ui.components.animations.ConfettiSystem
import com.dsafun.app.ui.components.animations.LevelUpOverlay
import com.dsafun.app.ui.components.gamification.MilestoneCelebrationSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTimer: () -> Unit = {},
    viewModel: CodeEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showQuestionPanel by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.problem?.title ?: "Loading...",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                // Simple WebView - just loads OneCompiler, no complexity
                SimpleCompilerWebView(
                    language = uiState.selectedLanguage,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
                
                // Floating "Mark as Solved" button
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    SmallFloatingActionButton(
                        onClick = { showQuestionPanel = true },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = "Open Question"
                        )
                    }

                    FloatingActionButton(
                        onClick = {
                            viewModel.markAsSolved()
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Problem marked as solved! 🎉")
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Text("Mark as Solved")
                        }
                    }
                }

                if (showQuestionPanel && uiState.problem != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxHeight()
                            .widthIn(max = 420.dp)
                            .fillMaxWidth(0.92f)
                            .align(Alignment.CenterEnd)
                            .padding(top = paddingValues.calculateTopPadding() + 12.dp, end = 12.dp, bottom = 12.dp)
                            .shadow(12.dp, MaterialTheme.shapes.large),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Question",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                IconButton(onClick = { showQuestionPanel = false }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Close Question"
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = uiState.problem!!.title,
                                    style = MaterialTheme.typography.headlineSmall
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(uiState.problem!!.difficulty) }
                                    )
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(uiState.problem!!.topic) }
                                    )
                                }

                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = uiState.problem!!.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                if (uiState.problem!!.examples.isNotEmpty()) {
                                    Text(
                                        text = "Examples",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    uiState.problem!!.examples.forEachIndexed { index, example ->
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = "Example ${index + 1}",
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                                Text("Input: ${example.input}")
                                                Text("Output: ${example.output}")
                                                example.explanation?.let {
                                                    Text("Explanation: $it")
                                                }
                                            }
                                        }
                                    }
                                }

                                Text(
                                    text = "Constraints",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = uiState.problem!!.constraints,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                if (uiState.problem!!.hints.isNotEmpty()) {
                                    Text(
                                        text = "Hints",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    uiState.problem!!.hints.forEachIndexed { index, hint ->
                                        Text(
                                            text = "${index + 1}. $hint",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Set Timer Dialog
    if (uiState.showSetTimerDialog) {
        var timerMinutes by remember { mutableStateOf(uiState.customTimerMinutes.toString()) }
        
        AlertDialog(
            onDismissRequest = viewModel::hideSetTimerDialog,
            title = { Text("Set Timer") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Set a custom timer duration for focused coding")
                    
                    OutlinedTextField(
                        value = timerMinutes,
                        onValueChange = {
                            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                                timerMinutes = it
                            }
                        },
                        label = { Text("Minutes") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Quick presets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(15, 25, 45, 60).forEach { preset ->
                            FilterChip(
                                selected = timerMinutes == preset.toString(),
                                onClick = { timerMinutes = preset.toString() },
                                label = { Text("${preset}m") }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val minutes = timerMinutes.toIntOrNull() ?: 25
                        if (minutes > 0) {
                            viewModel.setCustomTimer(minutes)
                        }
                    }
                ) {
                    Text("Start")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideSetTimerDialog) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Language Switch Dialog
    if (uiState.showLanguageSwitchDialog) {
        AlertDialog(
            onDismissRequest = viewModel::onLanguageSwitchDismissed,
            title = { Text("Switch Language?") },
            text = { Text("Do you want to keep your current code or load the template for ${uiState.pendingLanguage?.displayName}?") },
            confirmButton = {
                TextButton(onClick = { viewModel.onLanguageSwitchConfirmed(keepCode = true) }) {
                    Text("Keep Code")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onLanguageSwitchConfirmed(keepCode = false) }) {
                    Text("Load Template")
                }
            }
        )
    }
    

    
    // Submission Result Overlay
    val submissionStatus = uiState.submissionStatus
    val testResults = uiState.testCaseResults
    if (uiState.showSubmissionResult && submissionStatus != null && testResults != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            SubmissionResultOverlay(
                status = submissionStatus,
                passedCount = testResults.count { it.passed },
                totalCount = testResults.size,
                xpGained = uiState.xpGained ?: 0,
                timeTaken = uiState.timerSeconds,
                onDismiss = viewModel::dismissSubmissionResult
            )
            
            // Confetti for successful submission
            if (submissionStatus == "SOLVED") {
                ConfettiSystem()
            }
        }
    }
    
    // Level Up Overlay
    uiState.levelUpEvent?.let { levelUp ->
        LevelUpOverlay(
            newLevel = levelUp.newLevel,
            onDismiss = viewModel::dismissLevelUp
        )
    }
    
    // Badge Unlock Celebration
    if (uiState.newBadges.isNotEmpty()) {
        MilestoneCelebrationSheet(
            badges = uiState.newBadges,
            onDismiss = viewModel::dismissBadgeCelebration
        )
    }
    
}





// Made with Bob