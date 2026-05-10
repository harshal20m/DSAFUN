package com.dsafun.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import com.dsafun.app.ui.components.CollectionReminderSettings
import com.dsafun.app.ui.components.CreatorInfo
import com.dsafun.app.ui.components.CreatorInfoDialog
import com.dsafun.app.ui.components.EmptyStates
import com.dsafun.app.ui.theme.Dimens
import com.dsafun.app.ui.theme.Motion
import com.dsafun.app.workers.CollectionReminderWorker
import com.dsafun.app.workers.WorkerScheduler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ApnaCollegeScreen(
    viewModel: ApnaCollegeViewModel = hiltViewModel(),
    userPreferences: UserPreferencesDataStore = hiltViewModel<com.dsafun.app.ui.viewmodels.SettingsViewModel>().let {
        UserPreferencesDataStore(LocalContext.current)
    },
    workerScheduler: WorkerScheduler = WorkerScheduler(LocalContext.current)
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedTopic by remember { mutableStateOf<String?>(null) }
    var showFilters by remember { mutableStateOf(false) }
    var showCreatorInfo by remember { mutableStateOf(false) }
    
    // Reminder settings
    val reminderEnabled by userPreferences.apnaCollegeReminderEnabled.collectAsState(initial = false)
    val reminderHour by userPreferences.apnaCollegeReminderHour.collectAsState(initial = 10)
    val reminderMinute by userPreferences.apnaCollegeReminderMinute.collectAsState(initial = 0)

    val availableTopics = remember(uiState.problems) {
        uiState.problems
            .map { it.topic.trim() }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    val filteredProblems = remember(
        uiState.problems,
        searchQuery,
        selectedTopic
    ) {
        uiState.problems.filter { problem ->
            val matchesQuery = searchQuery.isBlank() ||
                problem.title.contains(searchQuery, ignoreCase = true) ||
                problem.topic.contains(searchQuery, ignoreCase = true) ||
                problem.companies.contains(searchQuery, ignoreCase = true)

            val matchesTopic = selectedTopic == null || problem.topic.equals(selectedTopic, ignoreCase = true)

            matchesQuery && matchesTopic
        }
    }

    val markedProblems = remember(uiState.problems, uiState.solvedProblemIds) {
        uiState.problems.filter { problem ->
            uiState.solvedProblemIds.contains(problem.id)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(Dimens.SpacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        // Tab Row
        item {
            TabRow(
                selectedTabIndex = uiState.selectedTab,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.onTabSelected(0) },
                    text = { Text("All (${filteredProblems.size})") }
                )
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) },
                    text = { Text("Marked (${markedProblems.size})") }
                )
            }
        }

        if (uiState.selectedTab == 0) {
            // All Problems Tab
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { isSearchActive = false },
                    active = isSearchActive,
                    onActiveChange = { isSearchActive = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search Apna College problems...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = { showFilters = !showFilters }) {
                                Text(if (showFilters) "Hide Filters" else "Show Filters")
                            }
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear search")
                                }
                            }
                        }
                    }
                ) {}
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Apna College DSA Sheet",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showCreatorInfo = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Creator Info",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Curated by Shradha Didi & Aman Bhaiya. Search, filter, open in browser, and mark solved.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Reminder Settings
            item {
                CollectionReminderSettings(
                    collectionName = "Apna College",
                    isEnabled = reminderEnabled,
                    reminderHour = reminderHour,
                    reminderMinute = reminderMinute,
                    onEnabledChange = { enabled ->
                        scope.launch {
                            userPreferences.setApnaCollegeReminderEnabled(enabled)
                            workerScheduler.scheduleCollectionReminder(
                                CollectionReminderWorker.COLLECTION_APNA_COLLEGE,
                                reminderHour,
                                reminderMinute,
                                enabled
                            )
                        }
                    },
                    onTimeChange = { hour, minute ->
                        scope.launch {
                            userPreferences.setApnaCollegeReminderTime(hour, minute)
                            if (reminderEnabled) {
                                workerScheduler.scheduleCollectionReminder(
                                    CollectionReminderWorker.COLLECTION_APNA_COLLEGE,
                                    hour,
                                    minute,
                                    true
                                )
                            }
                        }
                    }
                )
            }

            item {
                AnimatedVisibility(
                    visible = showFilters && !isSearchActive,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (availableTopics.isNotEmpty()) {
                            Text(
                                text = "Topics",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
                                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                            ) {
                                FilterChip(
                                    selected = selectedTopic == null,
                                    onClick = { selectedTopic = null },
                                    label = { Text("All Topics") }
                                )
                                availableTopics.forEach { topic ->
                                    FilterChip(
                                        selected = selectedTopic == topic,
                                        onClick = {
                                            selectedTopic = if (selectedTopic == topic) null else topic
                                        },
                                        label = { Text(topic) }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Showing ${filteredProblems.size} problem${if (filteredProblems.size != 1) "s" else ""}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            if (searchQuery.isNotBlank() || selectedTopic != null) {
                                TextButton(
                                    onClick = {
                                        searchQuery = ""
                                        selectedTopic = null
                                    }
                                ) {
                                    Text("Clear Filters")
                                }
                            }
                        }

                        Divider(modifier = Modifier.padding(top = Dimens.SpacingSmall))
                    }
                }
            }

            if (uiState.completionMessage != null) {
                item {
                    Text(
                        text = uiState.completionMessage ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (uiState.levelUpEvent != null) {
                item {
                    Text(
                        text = "Level up: ${uiState.levelUpEvent?.oldLevel} → ${uiState.levelUpEvent?.newLevel}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            when {
                uiState.isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Dimens.SpacingLarge),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                uiState.error != null -> {
                    item {
                        EmptyStates.GenericError(
                            message = uiState.error ?: "Failed to load Apna College problems",
                            onRetry = { viewModel.loadProblems() }
                        )
                    }
                }

                filteredProblems.isEmpty() -> {
                    item {
                        EmptyStates.SearchNoResults(query = searchQuery.ifBlank { "selected filters" })
                    }
                }

                else -> {
                    items(
                        items = filteredProblems,
                        key = { problem -> "${problem.id}-${problem.title}" }
                    ) { problem ->
                        ApnaCollegeProblemCard(
                            problem = problem,
                            index = filteredProblems.indexOf(problem),
                            isSolved = uiState.solvedProblemIds.contains(problem.id),
                            isUpdating = uiState.completionBusyId == problem.id,
                            onOpen = {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(problem.url))
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(browserIntent)
                            },
                            onToggleSolved = {
                                viewModel.toggleSolved(problem)
                            }
                        )
                    }
                }
            }
        } else {
            // Marked Tab
            item {
                Text(
                    text = "Marked Apna College Problems",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Text(
                    text = "Problems you've marked as solved.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            when {
                uiState.isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Dimens.SpacingLarge),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                markedProblems.isEmpty() -> {
                    item {
                        EmptyStates.NoProblems()
                    }
                }

                else -> {
                    items(
                        items = markedProblems,
                        key = { problem -> "marked_${problem.id}-${problem.title}" }
                    ) { problem ->
                        ApnaCollegeProblemCard(
                            problem = problem,
                            index = markedProblems.indexOf(problem),
                            isSolved = true,
                            isUpdating = uiState.completionBusyId == problem.id,
                            onOpen = {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(problem.url))
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(browserIntent)
                            },
                            onToggleSolved = {
                                viewModel.toggleSolved(problem)
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Creator Info Dialog
    if (showCreatorInfo) {
        CreatorInfoDialog(
            creatorInfo = CreatorInfo(
                name = "Shradha Khapra & Aman Dhattarwal",
                brand = "Apna College",
                website = "https://www.apnacollege.in/",
                youtube = "https://www.youtube.com/@ApnaCollegeOfficial",
                linkedin = null
            ),
            onDismiss = { showCreatorInfo = false }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ApnaCollegeProblemCard(
    problem: ApnaCollegeProblemItem,
    index: Int,
    isSolved: Boolean,
    isUpdating: Boolean,
    onOpen: () -> Unit,
    onToggleSolved: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(Motion.staggeredDelay(index).toLong())
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = Motion.TweenMedium,
        label = "apna_college_card_alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha),
        colors = CardDefaults.cardColors(
            containerColor = if (isSolved) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation),
        border = if (isSolved) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
        } else {
            null
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = problem.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingXSmall))
                    Text(
                        text = problem.topic,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (isSolved) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Solved",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            if (problem.companies.isNotBlank()) {
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                Text(
                    text = "Companies: ${problem.companies}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            if (problem.remarks.isNotBlank()) {
                Spacer(modifier = Modifier.height(Dimens.SpacingXSmall))
                Text(
                    text = "💡 ${problem.remarks}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
            ) {
                OutlinedButton(
                    onClick = onOpen,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowOutward,
                        contentDescription = "Open in browser"
                    )
                    Spacer(modifier = Modifier.padding(start = Dimens.SpacingXSmall))
                    Text("Open")
                }

                OutlinedButton(
                    onClick = onToggleSolved,
                    enabled = !isUpdating,
                    modifier = Modifier.weight(1f)
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = Dimens.SpacingXSmall)
                        )
                    } else {
                        Icon(
                            imageVector = if (isSolved) Icons.Default.Close else Icons.Default.CheckCircle,
                            contentDescription = if (isSolved) "Mark unsolved" else "Mark solved"
                        )
                        Spacer(modifier = Modifier.padding(start = Dimens.SpacingXSmall))
                    }
                    Text(if (isSolved) "Unsolved" else "Solved")
                }
            }
        }
    }
}

@Composable
private fun ApnaCollegeStatsCard(
    totalSolved: Int,
    totalProblems: Int
) {
    val solvedPercentage = if (totalProblems > 0) {
        (totalSolved.toFloat() / totalProblems * 100).toInt()
    } else 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Apna College Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = Dimens.SpacingXSmall)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = totalSolved.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "Solved",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = totalProblems.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "$solvedPercentage%",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "Complete",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

// Made with Bob
