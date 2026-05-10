package com.dsafun.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.ui.components.CreatorInfo
import com.dsafun.app.ui.components.CreatorInfoDialog
import com.dsafun.app.ui.components.EmptyStates
import com.dsafun.app.ui.theme.Dimens
import com.dsafun.app.ui.theme.Motion
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FrazScreen(
    viewModel: FrazViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var showCreatorInfo by remember { mutableStateOf(false) }

    val availableDifficulties = listOf("EASY", "MEDIUM", "HARD")

    val filteredProblems = remember(
        uiState.problems,
        searchQuery,
        selectedDifficulty
    ) {
        uiState.problems.filter { problem ->
            val matchesQuery = searchQuery.isBlank() ||
                problem.topic.contains(searchQuery, ignoreCase = true) ||
                problem.problemUrl.contains(searchQuery, ignoreCase = true)

            val matchesDifficulty = selectedDifficulty == null || problem.difficulty.equals(selectedDifficulty, ignoreCase = true)

            matchesQuery && matchesDifficulty
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
                    placeholder = { Text("Search problems...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Search suggestions can go here
                }
            }
            
            // Title with Info Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DSA Sheet by Fraz",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Curated by Mohammad Fraz",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { showCreatorInfo = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Creator Info",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Difficulty Filters
            item {
                Column {
                    Text(
                        text = "Difficulty",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = selectedDifficulty == null,
                            onClick = { selectedDifficulty = null },
                            label = { Text("All") }
                        )
                        availableDifficulties.forEach { difficulty ->
                            FilterChip(
                                selected = selectedDifficulty == difficulty,
                                onClick = { selectedDifficulty = if (selectedDifficulty == difficulty) null else difficulty },
                                label = { Text(difficulty) }
                            )
                        }
                    }
                }
            }

            // Problems List
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.SpacingLarge),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (uiState.error != null) {
                item {
                    EmptyStates.GenericError(
                        message = uiState.error ?: "Failed to load Fraz DSA Sheet problems",
                        onRetry = { viewModel.loadProblems() }
                    )
                }
            } else if (filteredProblems.isEmpty()) {
                item {
                    EmptyStates.SearchNoResults(query = searchQuery.ifBlank { "selected filters" })
                }
            } else {
                items(
                    items = filteredProblems,
                    key = { it.id }
                ) { problem ->
                    FrazProblemCard(
                        problem = problem,
                        isSolved = uiState.solvedProblemIds.contains(problem.id),
                        isUpdating = uiState.completionBusyId == problem.id,
                        onOpen = {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(problem.problemUrl)))
                        },
                        onOpenEditorial = if (problem.editorialUrl.isNotEmpty()) {
                            { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(problem.editorialUrl))) }
                        } else null,
                        onToggleSolved = { viewModel.toggleSolved(problem) }
                    )
                }
            }
        } else {
            // Marked Problems Tab
            if (markedProblems.isEmpty()) {
                item {
                    EmptyStates.NoProblems()
                }
            } else {
                // Group by topic
                val groupedProblems = markedProblems.groupBy { it.topic }
                groupedProblems.forEach { (topic, problems) ->
                    item {
                        Text(
                            text = topic,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = Dimens.SpacingSmall)
                        )
                    }
                    items(
                        items = problems,
                        key = { it.id }
                    ) { problem ->
                        FrazProblemCard(
                            problem = problem,
                            isSolved = true,
                            isUpdating = uiState.completionBusyId == problem.id,
                            onOpen = {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(problem.problemUrl)))
                            },
                            onOpenEditorial = if (problem.editorialUrl.isNotEmpty()) {
                                { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(problem.editorialUrl))) }
                            } else null,
                            onToggleSolved = { viewModel.toggleSolved(problem) }
                        )
                    }
                }
            }
        }
    }

    // Show completion message
    AnimatedVisibility(
        visible = uiState.completionMessage != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LaunchedEffect(uiState.completionMessage) {
            delay(3000)
            // Message will auto-hide
        }
    }
    
    // Creator Info Dialog
    if (showCreatorInfo) {
        CreatorInfoDialog(
            creatorInfo = CreatorInfo(
                name = "Mohammad Fraz",
                brand = "Fraz",
                website = "https://www.mohammadfraz.com/",
                youtube = "https://www.youtube.com/@mohammadfraz",
                linkedin = "https://www.linkedin.com/in/mohammad-fraz/"
            ),
            onDismiss = { showCreatorInfo = false }
        )
    }
}

@Composable
private fun FrazProblemCard(
    problem: FrazProblemItem,
    isSolved: Boolean,
    isUpdating: Boolean,
    onOpen: () -> Unit,
    onOpenEditorial: (() -> Unit)?,
    onToggleSolved: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSolved) 
                MaterialTheme.colorScheme.secondaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = problem.topic,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = problem.problemUrl
                            .substringAfter("leetcode.com/problems/")
                            .substringBefore("/")
                            .replace("-", " ")
                            .split(" ")
                            .joinToString(" ") { it.capitalize() },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
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

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
            
            Text(
                text = "Difficulty: ${problem.difficulty}",
                style = MaterialTheme.typography.bodySmall,
                color = when (problem.difficulty) {
                    "EASY" -> MaterialTheme.colorScheme.tertiary
                    "HARD" -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.primary
                },
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlinedButton(
                    onClick = onOpen,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowOutward,
                        contentDescription = "Open problem",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Open", style = MaterialTheme.typography.labelSmall)
                }

                if (onOpenEditorial != null) {
                    OutlinedButton(
                        onClick = onOpenEditorial,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Open editorial",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Video", style = MaterialTheme.typography.labelSmall)
                    }
                }

                OutlinedButton(
                    onClick = onToggleSolved,
                    enabled = !isUpdating,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    if (isUpdating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Icon(
                            imageVector = if (isSolved) Icons.Default.Close else Icons.Default.CheckCircle,
                            contentDescription = if (isSolved) "Mark unsolved" else "Mark solved",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isSolved) "Done" else "Mark",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

// Made with Bob
