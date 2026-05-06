package com.dsafun.app.ui.screens.problemdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.data.local.model.Example
import com.dsafun.app.ui.components.DifficultyBadge
import com.dsafun.app.ui.components.TopicTag
import com.dsafun.app.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemDetailScreen(
    onNavigateBack: () -> Unit,
    onOpenEditor: () -> Unit,
    viewModel: ProblemDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Problem Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                uiState.problem != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(Dimens.SpacingMedium),
                            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
                        ) {
                            val problem = uiState.problem!!

                            // Title and badges
                            Text(
                                text = problem.title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                            ) {
                                DifficultyBadge(difficulty = problem.difficulty)
                                TopicTag(topic = problem.topic)
                            }

                            Divider()

                            // Description
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = problem.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Examples
                            problem.examples.forEachIndexed { index, example ->
                                ExampleCard(
                                    example = example,
                                    index = index + 1
                                )
                            }

                            // Constraints
                            Text(
                                text = "Constraints",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = problem.constraints,
                                    modifier = Modifier.padding(Dimens.SpacingMedium),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Hints
                            if (problem.hints.isNotEmpty()) {
                                Text(
                                    text = "Hints",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                problem.hints.forEachIndexed { index, hint ->
                                    HintCard(
                                        hint = hint,
                                        index = index,
                                        isRevealed = uiState.revealedHints.contains(index),
                                        onReveal = { viewModel.revealHint(index) }
                                    )
                                }
                            }
                        }

                        // Bottom button
                        Button(
                            onClick = onOpenEditor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimens.SpacingMedium),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Open Editor")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExampleCard(
    example: Example,
    index: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Text(
            text = "Example $index",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier.padding(Dimens.SpacingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
            ) {
                Text(
                    text = "Input: ${example.input}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = com.dsafun.app.ui.theme.firaCodeFontFamily
                )
                Text(
                    text = "Output: ${example.output}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = com.dsafun.app.ui.theme.firaCodeFontFamily
                )
                if (example.explanation != null) {
                    Text(
                        text = "Explanation: ${example.explanation}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun HintCard(
    hint: String,
    index: Int,
    isRevealed: Boolean,
    onReveal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !isRevealed, onClick = onReveal),
        shape = MaterialTheme.shapes.medium,
        color = if (isRevealed) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Row(
            modifier = Modifier.padding(Dimens.SpacingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Hint ${index + 1}",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isRevealed) {
                        MaterialTheme.colorScheme.onTertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                if (isRevealed) {
                    Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
            if (!isRevealed) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Reveal hint",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Hide hint",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }
}

// Made with Bob
