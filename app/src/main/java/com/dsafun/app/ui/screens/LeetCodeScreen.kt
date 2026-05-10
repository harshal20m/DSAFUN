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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.ui.components.DifficultyBadge
import com.dsafun.app.ui.components.EmptyStates
import com.dsafun.app.ui.components.TopicTag
import com.dsafun.app.ui.theme.Dimens
import com.dsafun.app.ui.theme.Motion
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LeetCodeScreen(
    viewModel: LeetCodeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var selectedTopic by remember { mutableStateOf<String?>(null) }
    var showFilters by remember { mutableStateOf(false) }

    val availableTopics = remember(uiState.problems) {
        uiState.problems
            .flatMap { problem -> problem.topics }
            .map { topic -> topic.trim() }
            .filter { topic -> topic.isNotBlank() }
            .distinct()
            .sorted()
    }

    val filteredProblems = remember(
        uiState.problems,
        searchQuery,
        selectedDifficulty,
        selectedTopic
    ) {
        uiState.problems.filter { problem: LeetCodeProblemItem ->
            val matchesQuery = searchQuery.isBlank() ||
                problem.title.contains(searchQuery, ignoreCase = true) ||
                problem.topics.any { topic: String -> topic.contains(searchQuery, ignoreCase = true) }

            val matchesDifficulty =
                selectedDifficulty == null || problem.difficulty == selectedDifficulty
            val matchesTopic =
                selectedTopic == null || problem.topics.any { topic: String ->
                    topic.equals(selectedTopic, ignoreCase = true)
                }

            matchesQuery && matchesDifficulty && matchesTopic
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(Dimens.SpacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        item {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { isSearchActive = false },
                active = isSearchActive,
                onActiveChange = { isSearchActive = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search LeetCode problems...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search LeetCode") },
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
            Text(
                text = "LeetCode Practice",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Text(
                text = "Search, filter, open in browser, and manually mark solved to count toward app progress.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                    ) {
                        FilterChip(
                            selected = selectedDifficulty == null,
                            onClick = { selectedDifficulty = null },
                            label = { Text("All") }
                        )
                        listOf("Easy", "Medium", "Hard").forEach { difficulty: String ->
                            FilterChip(
                                selected = selectedDifficulty == difficulty,
                                onClick = {
                                    selectedDifficulty =
                                        if (selectedDifficulty == difficulty) null else difficulty
                                },
                                label = { Text(difficulty) }
                            )
                        }
                    }

                    if (availableTopics.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
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
                            availableTopics.take(24).forEach { topic: String ->
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
                        if (searchQuery.isNotBlank() || selectedDifficulty != null || selectedTopic != null) {
                            TextButton(
                                onClick = {
                                    searchQuery = ""
                                    selectedDifficulty = null
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
                        message = uiState.error ?: "Failed to load LeetCode problems",
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
                    key = { problem: LeetCodeProblemItem -> "${problem.id}-${problem.title}" }
                ) { problem ->
                    LeetCodeProblemCard(
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
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LeetCodeProblemCard(
    problem: LeetCodeProblemItem,
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
        label = "leetcode_card_alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha),
        colors = CardDefaults.cardColors(
            containerColor = if (isSolved) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surface
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
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = problem.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(Dimens.SpacingXSmall))
                    Text(
                        text = "LeetCode #${problem.id}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DifficultyBadge(difficulty = problem.difficulty)
                problem.topics.firstOrNull()?.takeIf { it.isNotBlank() }?.let { topic ->
                    TopicTag(topic = topic)
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Text(
                text = "${problem.acceptanceRate}% acceptance",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (problem.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                Text(
                    text = problem.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4
                )
            }

            if (problem.topics.size > 1) {
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
                    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                ) {
                    problem.topics.drop(1).take(4).forEach { topic: String ->
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = topic,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
            ) {
                AssistChip(
                    onClick = onOpen,
                    label = { Text("Open") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowOutward,
                            contentDescription = "Open in browser"
                        )
                    }
                )

                OutlinedButton(
                    onClick = onToggleSolved,
                    enabled = !isUpdating
                ) {
                    Text(if (isSolved) "Mark Unsolved" else "Mark Solved")
                }
            }
        }
    }
}

// Made with Bob
