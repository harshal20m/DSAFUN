package com.dsafun.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.domain.model.ProblemFilter
import com.dsafun.app.ui.components.EmptyStates
import com.dsafun.app.ui.components.ProblemCard
import com.dsafun.app.ui.screens.problemlist.ProblemListViewModel
import com.dsafun.app.ui.theme.Dimens
import com.dsafun.app.ui.theme.Motion
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemsScreen(
    onProblemClick: (Int) -> Unit,
    viewModel: ProblemListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search Bar
        SearchBar(
            query = uiState.filter.searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged,
            onSearch = { },
            active = uiState.isSearchActive,
            onActiveChange = viewModel::onSearchActiveChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (uiState.isSearchActive) 0.dp else Dimens.SpacingMedium),
            placeholder = { Text("Search problems...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (uiState.filter.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            }
        ) {
            // Empty search suggestions for now
        }

        // Filter Section
        AnimatedVisibility(
            visible = !uiState.isSearchActive,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.SpacingMedium)
            ) {
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

                // Topic Chips
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

                // Difficulty Chips
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

                // Stats Row
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

                Divider(modifier = Modifier.padding(vertical = Dimens.SpacingSmall))
            }
        }

        // Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    EmptyStates.GenericError(
                        message = uiState.error ?: "Unknown error occurred",
                        onRetry = { /* Retry logic if needed */ }
                    )
                }
                uiState.problems.isEmpty() -> {
                    if (uiState.filter.searchQuery.isNotEmpty()) {
                        EmptyStates.SearchNoResults(query = uiState.filter.searchQuery)
                    } else {
                        EmptyStates.NoProblems()
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(Dimens.SpacingMedium),
                        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
                    ) {
                        itemsIndexed(
                            items = uiState.problems,
                            key = { _, problem -> problem.id }
                        ) { index, problem ->
                            StaggeredProblemCard(
                                problem = problem,
                                index = index,
                                onClick = { onProblemClick(problem.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Made with Bob


@Composable
private fun StaggeredProblemCard(
    problem: com.dsafun.app.domain.model.Problem,
    index: Int,
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
        modifier = Modifier.alpha(alpha)
    )
}
