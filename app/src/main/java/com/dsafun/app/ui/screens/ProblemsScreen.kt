package com.dsafun.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.model.ProblemFilter
import com.dsafun.app.ui.components.DifficultyBadge
import com.dsafun.app.ui.components.EmptyStates
import com.dsafun.app.ui.components.ProblemCard
import com.dsafun.app.ui.components.TopicTag
import com.dsafun.app.ui.screens.problemlist.ProblemListViewModel
import com.dsafun.app.ui.theme.Dimens
import com.dsafun.app.ui.theme.Motion
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemsScreen(
    onProblemClick: (Int) -> Unit,
    onLeetCodeClick: () -> Unit,
    onCommonProblemsClick: () -> Unit,
    showCollectionsOnly: Boolean = false,
    viewModel: ProblemListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilters by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.SpacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        if (showCollectionsOnly) {
            item {
                Text(
                    text = "Problem Collections",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Text(
                    text = "Choose a practice set.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                ProblemsCollectionsSection(
                    onCommonProblemsClick = onCommonProblemsClick,
                    onLeetCodeClick = onLeetCodeClick
                )
            }
        } else {
            item {
                SearchBar(
                    query = uiState.filter.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged,
                    onSearch = { },
                    active = uiState.isSearchActive,
                    onActiveChange = viewModel::onSearchActiveChanged,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Search common problems...")
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = { showFilters = !showFilters }) {
                                Text(if (showFilters) "Hide Filters" else "Show Filters")
                            }
                            if (uiState.filter.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        }
                    }
                ) {}
            }
            item {
                AnimatedVisibility(
                    visible = showFilters && !uiState.isSearchActive,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Topics",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = Dimens.SpacingSmall)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                        ) {
                            item {
                                FilterChip(
                                    selected = uiState.filter.topic == null,
                                    onClick = { viewModel.onTopicSelected(null) },
                                    label = { Text("All") }
                                )
                            }
                            items(ProblemFilter.ALL_TOPICS) { topic ->
                                FilterChip(
                                    selected = uiState.filter.topic == topic,
                                    onClick = { viewModel.onTopicSelected(topic) },
                                    label = { Text(topic) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

                        Text(
                            text = "Difficulty",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = Dimens.SpacingSmall)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                        ) {
                            items(ProblemFilter.ALL_DIFFICULTIES) { difficulty ->
                                FilterChip(
                                    selected = difficulty in uiState.filter.difficulties,
                                    onClick = { viewModel.onDifficultyToggled(difficulty) },
                                    label = { Text(difficulty) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Showing ${uiState.problems.size} problem${if (uiState.problems.size != 1) "s" else ""}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            if (uiState.filter.isActive()) {
                                TextButton(onClick = viewModel::clearFilters) {
                                    Text("Clear Filters")
                                }
                            }
                        }

                        Divider(modifier = Modifier.padding(top = Dimens.SpacingSmall))
                    }
                }
            }

            item {
                MarkedProblemsSection(
                    nativeProblems = uiState.markedNativeProblems,
                    leetCodeProblems = uiState.markedLeetCodeProblems,
                    solvedNativeProblemIds = uiState.solvedNativeProblemIds,
                    solvedLeetCodeProblemIds = uiState.solvedLeetCodeProblemIds,
                    onProblemClick = onProblemClick,
                    onLeetCodeClick = onLeetCodeClick
                )
            }

            item {
                Text(
                    text = "Common Problems",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
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
                uiState.error != null -> {
                    item {
                        EmptyStates.GenericError(
                            message = uiState.error ?: "Unknown error occurred",
                            onRetry = { }
                        )
                    }
                }
                uiState.problems.isEmpty() -> {
                    item {
                        if (uiState.filter.searchQuery.isNotEmpty()) {
                            EmptyStates.SearchNoResults(query = uiState.filter.searchQuery)
                        } else {
                            EmptyStates.NoProblems()
                        }
                    }
                }
                else -> {
                    itemsIndexed(
                        items = uiState.problems,
                        key = { _, problem -> problem.id }
                    ) { index, problem ->
                        StaggeredProblemCard(
                            problem = problem,
                            index = index + 1,
                            isSolved = uiState.solvedNativeProblemIds.contains(problem.id),
                            onClick = { onProblemClick(problem.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MarkedProblemsSection(
    nativeProblems: List<Problem>,
    leetCodeProblems: List<LeetCodeProblemItem>,
    solvedNativeProblemIds: Set<Int>,
    solvedLeetCodeProblemIds: Set<String>,
    onProblemClick: (Int) -> Unit,
    onLeetCodeClick: () -> Unit
) {
    if (nativeProblems.isEmpty() && leetCodeProblems.isEmpty()) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        Text(
            text = "Marked",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Your saved and solved app problems plus marked LeetCode problems in one place.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        nativeProblems.forEachIndexed { index, problem ->
            StaggeredProblemCard(
                problem = problem,
                index = index,
                isSolved = solvedNativeProblemIds.contains(problem.id),
                onClick = { onProblemClick(problem.id) }
            )
        }

        leetCodeProblems.take(8).forEachIndexed { index, problem ->
            MarkedLeetCodeCard(
                problem = problem,
                index = nativeProblems.size + index,
                isSolved = solvedLeetCodeProblemIds.contains(problem.id),
                onClick = onLeetCodeClick
            )
        }

        if (leetCodeProblems.size > 8) {
            TextButton(onClick = onLeetCodeClick) {
                Text("View all marked LeetCode problems")
            }
        }

        Divider()
    }
}

@Composable
private fun MarkedLeetCodeCard(
    problem: LeetCodeProblemItem,
    index: Int,
    isSolved: Boolean,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(Motion.staggeredDelay(index).toLong())
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = Motion.TweenMedium,
        label = "marked_leetcode_card_alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clickable(onClick = onClick),
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
                } else {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Marked",
                        tint = MaterialTheme.colorScheme.primary
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
        }
    }
}

@Composable
private fun ProblemsCollectionsSection(
    onCommonProblemsClick: () -> Unit,
    onLeetCodeClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        CollectionEntryCard(
            title = "Common Problems",
            description = "Practice the core app problem set with filters and marked progress.",
            supportingText = "Native collection",
            icon = {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Common Problems",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = onCommonProblemsClick
        )

        CollectionEntryCard(
            title = "LeetCode Problems",
            description = "Browse searchable LeetCode questions and open them in your browser.",
            supportingText = "External collection",
            icon = {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = "LeetCode",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = onLeetCodeClick
        )
    }
}

@Composable
private fun CollectionEntryCard(
    title: String,
    description: String,
    supportingText: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.outlinedCardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    icon()
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingXSmall)
            ) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open collection",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Made with Bob

@Composable
private fun StaggeredProblemCard(
    problem: Problem,
    index: Int,
    isSolved: Boolean = false,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(Motion.staggeredDelay(index).toLong())
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = Motion.TweenMedium,
        label = "problem_card_alpha"
    )

    ProblemCard(
        problem = problem,
        onClick = onClick,
        modifier = Modifier.alpha(alpha),
        isSolved = isSolved
    )
}
