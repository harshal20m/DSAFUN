package com.dsafun.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.ui.theme.Dimens

@Composable
fun ProblemCard(
    problem: Problem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSolved: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.CardElevation
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium)
        ) {
            // Title with Solved Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = problem.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                
                if (isSolved) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Solved",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            // Difficulty and Topic Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DifficultyBadge(difficulty = problem.difficulty)
                TopicTag(topic = problem.topic)
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
            ) {
                Text(
                    text = "${problem.acceptanceRate.toInt()}% acceptance",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "+${problem.xpReward} XP",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun DifficultyBadge(
    difficulty: String,
    modifier: Modifier = Modifier
) {
    val color = when (difficulty) {
        "Easy" -> MaterialTheme.colorScheme.secondary
        "Medium" -> MaterialTheme.colorScheme.tertiary
        "Hard" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = difficulty,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
fun TopicTag(
    topic: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = topic,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

// Made with Bob
