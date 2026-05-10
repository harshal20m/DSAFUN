package com.dsafun.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.ui.components.CreatorInfo
import com.dsafun.app.ui.components.CreatorInfoDialog
import com.dsafun.app.ui.components.EmptyStates
import com.dsafun.app.ui.components.animations.LevelUpOverlay

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoveBabbarScreen(
    onNavigateBack: () -> Unit,
    viewModel: LoveBabbarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedTopic by remember { mutableStateOf<String?>(null) }
    var showCreatorInfo by remember { mutableStateOf(false) }

    // Get unique topics from problems
    val topics = remember(uiState.problems) {
        uiState.problems.map { it.topic }.distinct().sorted()
    }

    // Filter problems based on search, topic, and tab
    val filteredProblems = remember(
        uiState.problems,
        uiState.solvedProblemIds,
        searchQuery,
        selectedTopic,
        uiState.selectedTab
    ) {
        var filtered = uiState.problems

        // Apply tab filter
        if (uiState.selectedTab == 1) {
            filtered = filtered.filter { uiState.solvedProblemIds.contains(it.id) }
        }

        // Apply search filter
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.topic.contains(searchQuery, ignoreCase = true)
            }
        }

        // Apply topic filter
        if (selectedTopic != null) {
            filtered = filtered.filter { it.topic == selectedTopic }
        }

        filtered
    }

    // Group problems by topic for Marked tab
    val groupedProblems = remember(filteredProblems, uiState.selectedTab) {
        if (uiState.selectedTab == 1) {
            filteredProblems.groupBy { it.topic }
        } else {
            emptyMap()
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Love Babbar 450") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Filled.ArrowBack, "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showCreatorInfo = true }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Creator Info",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                // Tab Row
                TabRow(
                    selectedTabIndex = uiState.selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Tab(
                        selected = uiState.selectedTab == 0,
                        onClick = { viewModel.onTabSelected(0) },
                        text = {
                            Text(
                                "All (${filteredProblems.size})",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    )
                    Tab(
                        selected = uiState.selectedTab == 1,
                        onClick = { viewModel.onTabSelected(1) },
                        text = {
                            Text(
                                "Marked (${uiState.solvedProblemIds.size})",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = remember { SnackbarHostState() }) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search problems...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                // Topic Filter Dropdown
                if (topics.isNotEmpty()) {
                    var topicDropdownExpanded by remember { mutableStateOf(false) }
                    
                    ExposedDropdownMenuBox(
                        expanded = topicDropdownExpanded,
                        onExpandedChange = { topicDropdownExpanded = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedTopic ?: "All Topics",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Filter by Topic") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = topicDropdownExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = topicDropdownExpanded,
                            onDismissRequest = { topicDropdownExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Topics") },
                                onClick = {
                                    selectedTopic = null
                                    topicDropdownExpanded = false
                                },
                                leadingIcon = if (selectedTopic == null) {
                                    { Icon(Icons.Filled.Check, contentDescription = null) }
                                } else null
                            )
                            topics.forEach { topic ->
                                DropdownMenuItem(
                                    text = { Text(topic) },
                                    onClick = {
                                        selectedTopic = topic
                                        topicDropdownExpanded = false
                                    },
                                    leadingIcon = if (selectedTopic == topic) {
                                        { Icon(Icons.Filled.Check, contentDescription = null) }
                                    } else null
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.error != null -> {
                        EmptyStates.GenericError(
                            message = uiState.error ?: "Unknown error",
                            onRetry = { viewModel.loadProblems() }
                        )
                    }

                    filteredProblems.isEmpty() && uiState.selectedTab == 1 -> {
                        EmptyStates.NoProblems()
                    }

                    filteredProblems.isEmpty() -> {
                        EmptyStates.SearchNoResults(query = searchQuery)
                    }

                    uiState.selectedTab == 1 -> {
                        // Grouped by topic for Marked tab
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            groupedProblems.forEach { (topic, problems) ->
                                item {
                                    Text(
                                        text = topic,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }
                                items(problems) { problem ->
                                    LoveBabbarProblemCard(
                                        problem = problem,
                                        isSolved = uiState.solvedProblemIds.contains(problem.id),
                                        isBusy = uiState.completionBusyId == problem.id,
                                        onToggleSolved = { viewModel.toggleSolved(problem) },
                                        onOpenLink = {
                                            context.startActivity(
                                                Intent(Intent.ACTION_VIEW, Uri.parse(problem.url))
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        // All problems list
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredProblems) { problem ->
                                LoveBabbarProblemCard(
                                    problem = problem,
                                    isSolved = uiState.solvedProblemIds.contains(problem.id),
                                    isBusy = uiState.completionBusyId == problem.id,
                                    onToggleSolved = { viewModel.toggleSolved(problem) },
                                    onOpenLink = {
                                        context.startActivity(
                                            Intent(Intent.ACTION_VIEW, Uri.parse(problem.url))
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Level Up Overlay
            uiState.levelUpEvent?.let { event ->
                LevelUpOverlay(
                    newLevel = event.newLevel,
                    onDismiss = { /* Level up event handled */ }
                )
            }

            // Completion Message
            AnimatedVisibility(
                visible = uiState.completionMessage != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                uiState.completionMessage?.let { message ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = message,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
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
                name = "Love Babbar",
                brand = "Love Babbar",
                website = "https://www.lovebabbar.com/",
                youtube = "https://www.youtube.com/@CodeHelp",
                linkedin = "https://www.linkedin.com/in/love-babbar-38ab2887/"
            ),
            onDismiss = { showCreatorInfo = false }
        )
    }
}

@Composable
private fun LoveBabbarProblemCard(
    problem: LoveBabbarProblemItem,
    isSolved: Boolean,
    isBusy: Boolean,
    onToggleSolved: () -> Unit,
    onOpenLink: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSolved)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Header with topic badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = problem.topic,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Circle,
                            contentDescription = null,
                            modifier = Modifier.size(8.dp)
                        )
                    },
                    modifier = Modifier.height(24.dp)
                )

                if (isSolved) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Solved",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Problem Title
            Text(
                text = problem.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons - Compact Design
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenLink,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Link,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Open",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Button(
                    onClick = onToggleSolved,
                    enabled = !isBusy,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSolved)
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isBusy) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Icon(
                            if (isSolved) Icons.Default.Circle else Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            if (isSolved) "Unmark" else "Done",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

// Made with Bob
