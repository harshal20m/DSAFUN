package com.dsafun.app.ui.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.ui.viewmodels.SettingsViewModel
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showResetDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showFontSizeDialog by remember { mutableStateOf(false) }
    var showDailyGoalDialog by remember { mutableStateOf(false) }
    var showNameDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Section
            SettingsSection(title = "Profile") {
                SettingsItem(
                    icon = Icons.Default.Person,
                    title = "Name",
                    subtitle = uiState.userName.ifEmpty { "Not set" },
                    onClick = { showNameDialog = true }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Code Editor Section
            SettingsSection(title = "Code Editor") {
                SettingsItem(
                    icon = Icons.Default.Code,
                    title = "Preferred Language",
                    subtitle = when (uiState.preferredLanguage) {
                        "KOTLIN" -> "Kotlin"
                        "JAVA" -> "Java"
                        "PYTHON" -> "Python"
                        "JAVASCRIPT" -> "JavaScript"
                        "CPP" -> "C++"
                        else -> uiState.preferredLanguage
                    },
                    onClick = { showLanguageDialog = true }
                )
                SettingsItem(
                    icon = Icons.Default.TextFields,
                    title = "Font Size",
                    subtitle = when (uiState.fontSizePreference) {
                        "SMALL" -> "Small"
                        "MEDIUM" -> "Medium"
                        "LARGE" -> "Large"
                        else -> uiState.fontSizePreference
                    },
                    onClick = { showFontSizeDialog = true }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Notifications Section
            SettingsSection(title = "Notifications") {
                SettingsSwitchItem(
                    icon = Icons.Default.Notifications,
                    title = "Daily Reminder",
                    subtitle = if (uiState.reminderEnabled) {
                        String.format("%02d:%02d", uiState.reminderHour, uiState.reminderMinute)
                    } else {
                        "Disabled"
                    },
                    checked = uiState.reminderEnabled,
                    onCheckedChange = { enabled ->
                        viewModel.updateReminderEnabled(enabled)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                if (enabled) "Daily reminder enabled" else "Daily reminder disabled"
                            )
                        }
                    }
                )
                
                if (uiState.reminderEnabled) {
                    SettingsItem(
                        icon = Icons.Default.Schedule,
                        title = "Reminder Time",
                        subtitle = String.format("%02d:%02d", uiState.reminderHour, uiState.reminderMinute),
                        onClick = {
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    viewModel.updateReminderTime(hour, minute)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Reminder time set to ${String.format("%02d:%02d", hour, minute)}"
                                        )
                                    }
                                },
                                uiState.reminderHour,
                                uiState.reminderMinute,
                                true
                            ).show()
                        }
                    )
                }

                SettingsSwitchItem(
                    icon = Icons.Default.Warning,
                    title = "Streak Warning",
                    subtitle = "Remind me if I haven't solved today",
                    checked = uiState.streakWarningEnabled,
                    onCheckedChange = { enabled ->
                        viewModel.updateStreakWarningEnabled(enabled)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                if (enabled) "Streak warning enabled" else "Streak warning disabled"
                            )
                        }
                    }
                )

                SettingsSwitchItem(
                    icon = Icons.Default.CalendarMonth,
                    title = "Weekly Summary",
                    subtitle = "Sunday progress report",
                    checked = uiState.weeklySummaryEnabled,
                    onCheckedChange = { enabled ->
                        viewModel.updateWeeklySummaryEnabled(enabled)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                if (enabled) "Weekly summary enabled" else "Weekly summary disabled"
                            )
                        }
                    }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Appearance Section
            SettingsSection(title = "Appearance") {
                SettingsItem(
                    icon = Icons.Outlined.Palette,
                    title = "App Theme",
                    subtitle = when (uiState.appTheme) {
                        "LIGHT" -> "Light"
                        "MONOKAI" -> "Monokai"
                        "DRACULA" -> "Dracula"
                        "NORD" -> "Nord"
                        else -> "Dark"
                    },
                    onClick = { showThemeDialog = true }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Goals Section
            SettingsSection(title = "Goals") {
                SettingsItem(
                    icon = Icons.Default.Flag,
                    title = "Daily Goal",
                    subtitle = "${uiState.dailyGoal} problem${if (uiState.dailyGoal != 1) "s" else ""} per day",
                    onClick = { showDailyGoalDialog = true }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Data Section
            SettingsSection(title = "Data") {
                SettingsItem(
                    icon = Icons.Default.Delete,
                    title = "Reset Progress",
                    subtitle = "Reset streak (keeps XP and problems solved)",
                    onClick = { showResetDialog = true },
                    isDestructive = true
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // About Section
            SettingsSection(title = "About") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "App Version",
                    subtitle = uiState.appVersion,
                    onClick = { }
                )
                SettingsItem(
                    icon = Icons.Default.EmojiEvents,
                    title = "Your Stats",
                    subtitle = "Level ${uiState.currentLevel} • ${uiState.totalProblemsSolved} solved • ${uiState.currentStreak} day streak",
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Dialogs
    if (showNameDialog) {
        TextInputDialog(
            title = "Edit Name",
            initialValue = uiState.userName,
            onDismiss = { showNameDialog = false },
            onConfirm = { name ->
                viewModel.updateUserName(name)
                showNameDialog = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Name updated to $name")
                }
            }
        )
    }

    if (showLanguageDialog) {
        SelectionDialog(
            title = "Preferred Language",
            options = listOf("Kotlin", "Java", "Python", "JavaScript", "C++"),
            selectedOption = when (uiState.preferredLanguage) {
                "KOTLIN" -> "Kotlin"
                "JAVA" -> "Java"
                "PYTHON" -> "Python"
                "JAVASCRIPT" -> "JavaScript"
                "CPP" -> "C++"
                else -> uiState.preferredLanguage
            },
            onDismiss = { showLanguageDialog = false },
            onSelect = { language ->
                // Convert displayName to enum name
                val enumName = when (language) {
                    "Kotlin" -> "KOTLIN"
                    "Java" -> "JAVA"
                    "Python" -> "PYTHON"
                    "JavaScript" -> "JAVASCRIPT"
                    "C++" -> "CPP"
                    else -> language
                }
                viewModel.updatePreferredLanguage(enumName)
                showLanguageDialog = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Preferred language set to $language")
                }
            }
        )
    }

    if (showFontSizeDialog) {
        SelectionDialog(
            title = "Font Size",
            options = listOf("Small", "Medium", "Large"),
            selectedOption = when (uiState.fontSizePreference) {
                "SMALL" -> "Small"
                "MEDIUM" -> "Medium"
                "LARGE" -> "Large"
                else -> uiState.fontSizePreference
            },
            onDismiss = { showFontSizeDialog = false },
            onSelect = { size ->
                // Convert to enum-style name
                val enumName = when (size) {
                    "Small" -> "SMALL"
                    "Medium" -> "MEDIUM"
                    "Large" -> "LARGE"
                    else -> size
                }
                viewModel.updateFontSize(enumName)
                showFontSizeDialog = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Font size changed to $size")
                }
            }
        )
    }

    if (showDailyGoalDialog) {
        SelectionDialog(
            title = "Daily Goal",
            options = (1..5).map { "$it problem${if (it != 1) "s" else ""}" },
            selectedOption = "${uiState.dailyGoal} problem${if (uiState.dailyGoal != 1) "s" else ""}",
            onDismiss = { showDailyGoalDialog = false },
            onSelect = { goal ->
                val goalNumber = goal.split(" ")[0].toInt()
                viewModel.updateDailyGoal(goalNumber)
                showDailyGoalDialog = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Daily goal set to $goalNumber problem${if (goalNumber != 1) "s" else ""}")
                }
            }
        )
    }
    
    if (showThemeDialog) {
        SelectionDialog(
            title = "App Theme",
            options = listOf("Dark", "Light", "Monokai", "Dracula", "Nord"),
            selectedOption = when (uiState.appTheme) {
                "LIGHT" -> "Light"
                "MONOKAI" -> "Monokai"
                "DRACULA" -> "Dracula"
                "NORD" -> "Nord"
                else -> "Dark"
            },
            onDismiss = { showThemeDialog = false },
            onSelect = { theme ->
                val themeKey = when (theme) {
                    "Light" -> "LIGHT"
                    "Monokai" -> "MONOKAI"
                    "Dracula" -> "DRACULA"
                    "Nord" -> "NORD"
                    else -> "DARK"
                }
                viewModel.updateAppTheme(themeKey)
                showThemeDialog = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Theme changed to $theme")
                }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Progress?") },
            text = { Text("This will reset your streak to 0. Your XP, level, and problems solved will be kept.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetProgress()
                        showResetDialog = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Progress reset successfully")
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        content()
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun TextInputDialog(
    title: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text) },
                enabled = text.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SelectionDialog(
    title: String,
    options: List<String>,
    selectedOption: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(option) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option == selectedOption,
                            onClick = { onSelect(option) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

// Made with Bob
