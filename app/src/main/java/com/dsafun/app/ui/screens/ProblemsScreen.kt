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
import androidx.compose.material.icons.filled.Star
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
    onApnaCollegeClick: () -> Unit = {},
    onFrazClick: () -> Unit = {},
    onLoveBabbarClick: () -> Unit = {},
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
                    onLeetCodeClick = onLeetCodeClick,
                    onApnaCollegeClick = onApnaCollegeClick,
                    onFrazClick = onFrazClick,
                    onLoveBabbarClick = onLoveBabbarClick
                )
            }
        } else {
            // Global Stats Card
            item {
                GlobalStatsCard(
                    totalSolved = uiState.solvedNativeProblemIds.size + uiState.solvedLeetCodeProblemIds.size,
                    nativeSolved = uiState.solvedNativeProblemIds.size,
                    leetCodeSolved = uiState.solvedLeetCodeProblemIds.size,
                    totalMarked = uiState.markedNativeProblems.size + uiState.markedLeetCodeProblems.size
                )
            }

            // Tab Row
            item {
                TabRow(
                    selectedTabIndex = uiState.selectedTab,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = uiState.selectedTab == 0,
                        onClick = { viewModel.onTabSelected(0) },
                        text = { Text("All Problems") }
                    )
                    Tab(
                        selected = uiState.selectedTab == 1,
                        onClick = { viewModel.onTabSelected(1) },
                        text = { Text("Marked") }
                    )
                }
            }

            // Show content based on selected tab
            if (uiState.selectedTab == 0) {
                // All Problems Tab
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
                    Text(
                        text = "Common Problems",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                when {
                    uiState.isLoading -> {
                        item {
                            ProblemsLoadingScreen()
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
            } else {
                // Marked Tab
                item {
                    Text(
                        text = "Marked Problems",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Text(
                        text = "Your saved and solved problems from both collections.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (uiState.markedNativeProblems.isEmpty() && uiState.markedLeetCodeProblems.isEmpty()) {
                    item {
                        EmptyStates.NoProblems()
                    }
                } else {
                    // Native Problems Section
                    if (uiState.markedNativeProblems.isNotEmpty()) {
                        item {
                            Text(
                                text = "Common Problems (${uiState.markedNativeProblems.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = Dimens.SpacingMedium)
                            )
                        }

                        itemsIndexed(
                            items = uiState.markedNativeProblems,
                            key = { _, problem -> "native_${problem.id}" }
                        ) { index, problem ->
                            StaggeredProblemCard(
                                problem = problem,
                                index = index,
                                isSolved = uiState.solvedNativeProblemIds.contains(problem.id),
                                onClick = { onProblemClick(problem.id) }
                            )
                        }
                    }

                    // LeetCode Problems Section
                    if (uiState.markedLeetCodeProblems.isNotEmpty()) {
                        item {
                            Text(
                                text = "LeetCode Problems (${uiState.markedLeetCodeProblems.size})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = Dimens.SpacingMedium)
                            )
                        }

                        itemsIndexed(
                            items = uiState.markedLeetCodeProblems,
                            key = { _, problem -> "leetcode_${problem.id}" }
                        ) { index, problem ->
                            MarkedLeetCodeCard(
                                problem = problem,
                                index = uiState.markedNativeProblems.size + index,
                                isSolved = uiState.solvedLeetCodeProblemIds.contains(problem.id),
                                onClick = onLeetCodeClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GlobalStatsCard(
    totalSolved: Int,
    nativeSolved: Int,
    leetCodeSolved: Int,
    totalMarked: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
                    text = "Your Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = Dimens.SpacingXSmall)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Total Solved",
                    value = totalSolved.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Common",
                    value = nativeSolved.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "LeetCode",
                    value = leetCodeSolved.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    label = "Marked",
                    value = totalMarked.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
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
    onLeetCodeClick: () -> Unit,
    onApnaCollegeClick: () -> Unit,
    onFrazClick: () -> Unit,
    onLoveBabbarClick: () -> Unit
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

        CollectionEntryCard(
            title = "Apna College DSA Sheet",
            description = "Curated by Shradha Didi & Aman Bhaiya. 375 problems across various topics.",
            supportingText = "External collection",
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Apna College",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = onApnaCollegeClick
        )

        CollectionEntryCard(
            title = "Fraz DSA Sheet",
            description = "Comprehensive DSA practice by Fraz Mohammad with video tutorials.",
            supportingText = "External collection",
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Fraz DSA Sheet",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = onFrazClick
        )

        CollectionEntryCard(
            title = "Love Babbar 450",
            description = "450 most important DSA problems curated by Love Babbar for interview prep.",
            supportingText = "External collection",
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Love Babbar 450",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            onClick = onLoveBabbarClick
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

@Composable
private fun ProblemsLoadingScreen() {
    val loadingMessages = listOf(
        "🔥 Preparing some cooked codes...",
        "💡 Loading brilliant solutions...",
        "🚀 Fetching awesome problems...",
        "⚡ Compiling challenges...",
        "🎯 Getting ready for action...",
        "🧠 Loading brain teasers...",
        "💪 Preparing your coding journey...",
        "✨ Crafting perfect problems..."
    )
    
    var currentMessageIndex by remember { mutableStateOf(0) }
    var progress by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        // Cycle through messages
        while (true) {
            delay(1500)
            currentMessageIndex = (currentMessageIndex + 1) % loadingMessages.size
        }
    }
    
    LaunchedEffect(Unit) {
        // Animate progress
        while (progress < 1f) {
            delay(50)
            progress += 0.02f
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Animated message
        Text(
            text = loadingMessages[currentMessageIndex],
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        // Progress bar
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Circular progress indicator
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Hang tight! We're loading all the problems for you...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
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
