package com.dsafun.app.ui.screens.codeeditor

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.domain.model.Language
import com.dsafun.app.ui.components.SubmissionResultOverlay
import com.dsafun.app.ui.components.TestCaseResultsBottomSheet
import com.dsafun.app.ui.components.animations.ConfettiSystem
import com.dsafun.app.ui.components.animations.LevelUpOverlay
import com.dsafun.app.ui.components.gamification.MilestoneCelebrationSheet
import com.dsafun.app.ui.editor.CodeExporter
import com.dsafun.app.ui.editor.SyntaxHighlighter
import com.dsafun.app.ui.editor.handleAutoIndent
import com.dsafun.app.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTimer: () -> Unit = {},
    viewModel: CodeEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.problem?.title ?: "Loading...",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = formatTime(uiState.timerSeconds),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    // Timer controls
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Set Timer button
                        IconButton(onClick = viewModel::showSetTimerDialog) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Set Timer",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        
                        // Pause/Resume button
                        IconButton(
                            onClick = {
                                if (uiState.isTimerRunning) {
                                    viewModel.pauseTimer()
                                } else {
                                    viewModel.resumeTimer()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (uiState.isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (uiState.isTimerRunning) "Pause Timer" else "Resume Timer",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        // Reset timer button
                        IconButton(onClick = viewModel::resetTimer) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Timer",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                        
                        if (!uiState.isDraftSaved) {
                            Text(
                                text = "Unsaved",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.SpacingSmall),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = viewModel::resetCode) {
                        Text("Reset")
                    }
                    TextButton(onClick = {
                        copyToClipboard(context, uiState.currentCode)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Code copied to clipboard")
                        }
                    }) {
                        Text("Copy")
                    }
                    TextButton(onClick = viewModel::saveDraft) {
                        Text("Save")
                    }
                    TextButton(onClick = onNavigateToTimer) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Focus")
                    }
                    TextButton(onClick = {
                        uiState.problem?.let { problem ->
                            val exporter = CodeExporter(context)
                            val intent = exporter.exportCode(
                                problem = problem,
                                language = uiState.selectedLanguage,
                                code = uiState.currentCode,
                                timeTaken = uiState.timerSeconds
                            )
                            intent?.let { context.startActivity(Intent.createChooser(it, "Export Code")) }
                        }
                    }) {
                        Text("Export")
                    }
                }
            }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Language Tabs (only show if multiple languages available)
                if (uiState.availableLanguages.size > 1) {
                    ScrollableTabRow(
                        selectedTabIndex = uiState.availableLanguages.indexOf(uiState.selectedLanguage),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        uiState.availableLanguages.forEach { language ->
                            Tab(
                                selected = language == uiState.selectedLanguage,
                                onClick = { viewModel.onLanguageSelected(language) },
                                text = { Text(language.displayName) }
                            )
                        }
                    }
                    Divider()
                }

                // Code Editor
                CodeEditor(
                    code = uiState.currentCode,
                    language = uiState.selectedLanguage,
                    onCodeChange = viewModel::onCodeChanged,
                    fontSize = when (uiState.fontSizePreference) {
                        "SMALL" -> 12.sp
                        "LARGE" -> 16.sp
                        else -> 14.sp // MEDIUM
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )

                // Run & Submit Button
                Button(
                    onClick = viewModel::runAndSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.SpacingMedium),
                    enabled = !uiState.isRunning
                ) {
                    if (uiState.isRunning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
                        Text("Running...")
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
                        Text("Run & Submit")
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
    
    // Test Results Bottom Sheet
    uiState.testCaseResults?.let { results ->
        if (uiState.showTestResults) {
            TestCaseResultsBottomSheet(
                testResults = results,
                isRunning = uiState.isRunning,
                onDismiss = viewModel::dismissTestResults
            )
        }
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

@Composable
fun CodeEditor(
    code: String,
    language: Language,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 14.sp
) {
    val scrollState = rememberScrollState()
    
    // Convert String to TextFieldValue for auto-indent support
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = code))
    }
    
    // Track if change is from user typing (to preserve cursor) or external (reset cursor)
    var isUserTyping by remember { mutableStateOf(false) }
    
    // Track previous language to detect language switches
    var previousLanguage by remember { mutableStateOf(language) }
    
    // Update text when code changes from external source (not from typing)
    // This handles: template loading, reset, language switch, saved solution loading
    LaunchedEffect(code, language) {
        // Language changed - always update and reset cursor
        if (language != previousLanguage) {
            textFieldValue = TextFieldValue(
                text = code,
                selection = androidx.compose.ui.text.TextRange(0)
            )
            previousLanguage = language
            isUserTyping = false
        }
        // Code changed externally (not from typing) - update and reset cursor
        else if (!isUserTyping && code != textFieldValue.text) {
            textFieldValue = TextFieldValue(
                text = code,
                selection = androidx.compose.ui.text.TextRange(code.length)
            )
        }
        // Reset typing flag after processing
        isUserTyping = false
    }
    
    val keywordColor = MaterialTheme.colorScheme.primary
    val stringColor = MaterialTheme.colorScheme.secondary
    val commentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    val numberColor = MaterialTheme.colorScheme.tertiary
    
    val highlighter = remember(language, keywordColor, stringColor, commentColor, numberColor) {
        SyntaxHighlighter(
            language = language,
            keywordColor = keywordColor,
            stringColor = stringColor,
            commentColor = commentColor,
            numberColor = numberColor
        )
    }

    Row(modifier = modifier) {
        // Line Numbers
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 8.dp, vertical = Dimens.SpacingMedium)
                .verticalScroll(scrollState)
        ) {
            val lineCount = textFieldValue.text.count { it == '\n' } + 1
            repeat(lineCount) { index ->
                Text(
                    text = "${index + 1}",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = fontSize,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        // Code Input with Auto-Indent
        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                // Mark as user typing to prevent external updates from resetting cursor
                isUserTyping = true
                // Apply auto-indent logic
                val processedValue = handleAutoIndent(textFieldValue, newValue)
                textFieldValue = processedValue
                onCodeChange(processedValue.text)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.SpacingMedium)
                .verticalScroll(scrollState),
            textStyle = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = fontSize,
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Box {
                    if (textFieldValue.text.isEmpty()) {
                        Text(
                            text = "Start coding...",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = fontSize,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", minutes, secs)
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("code", text)
    clipboard.setPrimaryClip(clip)
}

// Made with Bob