package com.dsafun.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.domain.usecase.HomeStats
import com.dsafun.app.domain.usecase.TopicProgress
import com.dsafun.app.ui.components.ProblemCard
import com.dsafun.app.ui.components.StreakRing
import com.dsafun.app.ui.components.XpBar
import com.dsafun.app.ui.screens.home.HomeViewModel
import com.dsafun.app.ui.theme.Dimens
import java.util.*

@Composable
fun HomeScreen(
    onNavigateToProblem: (Int) -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeStats by viewModel.homeStats.collectAsState()
    
    if (homeStats == null) {
        // Loading state
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    
    val stats = homeStats!!
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(Dimens.SpacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        // 1. Greeting Header
        item {
            GreetingHeader(
                userName = stats.userName,
                onSettingsClick = onNavigateToSettings
            )
        }
        
        // 2. Streak + XP Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
            ) {
                // Streak Counter
                StreakCounter(
                    currentStreak = stats.currentStreak,
                    bestStreak = stats.bestStreak,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // XP Bar
        item {
            XpBar(
                currentXp = stats.totalXp,
                xpToNextLevel = stats.xpToNextLevel,
                level = stats.level
            )
        }
        
        // 3. Daily Challenge Card
        item {
            stats.dailyChallenge?.let { challenge ->
                DailyChallengeCard(
                    problem = challenge,
                    onClick = { onNavigateToProblem(challenge.id) }
                )
            }
        }
        
        // 4. Today's Progress
        item {
            TodaysProgressCard(
                solved = stats.todaySolved,
                goal = stats.dailyGoal,
                streak = stats.currentStreak
            )
        }
        
        // 5. Topic Progress
        item {
            TopicProgressSection(topicProgresses = stats.topicProgresses)
        }
        
        // 6. Recent Activity
        if (stats.recentSolutions.isNotEmpty()) {
            item {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            items(stats.recentSolutions.take(5)) { solution ->
                RecentActivityItem(solution = solution)
            }
        }
        
        // 7. Motivational Quote
        item {
            MotivationalQuote()
        }
    }
}

@Composable
private fun GreetingHeader(
    userName: String,
    onSettingsClick: () -> Unit
) {
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "$greeting,",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun StreakCounter(
    currentStreak: Int,
    bestStreak: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔥",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "$currentStreak Day${if (currentStreak != 1) "s" else ""}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Best: $bestStreak",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun DailyChallengeCard(
    problem: com.dsafun.app.domain.model.Problem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
                Text(
                    text = "⭐ Daily Challenge",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Surface(
                    color = when (problem.difficulty) {
                        "Easy" -> Color(0xFF10B981)
                        "Medium" -> Color(0xFFF59E0B)
                        else -> Color(0xFFEF4444)
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = problem.difficulty,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
            
            Text(
                text = problem.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Text(
                text = problem.topic,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun TodaysProgressCard(
    solved: Int,
    goal: Int,
    streak: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Streak Ring
            StreakRing(
                solved = solved,
                goal = goal,
                streak = streak,
                modifier = Modifier.size(120.dp)
            )
            
            // Progress Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Today's Progress",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$solved",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "/ $goal problems",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                if (solved >= goal) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✓",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF10B981)
                        )
                        Text(
                            text = "Goal completed!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF10B981)
                        )
                    }
                } else {
                    val remaining = goal - solved
                    Text(
                        text = "$remaining more to reach your goal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TopicProgressSection(topicProgresses: List<TopicProgress>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Text(
            text = "Topic Progress",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        topicProgresses.take(5).forEach { topicProgress ->
            TopicProgressBar(topicProgress = topicProgress)
        }
    }
}

@Composable
private fun TopicProgressBar(topicProgress: TopicProgress) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = topicProgress.topic,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${topicProgress.solved}/${topicProgress.total}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
        
        LinearProgressIndicator(
            progress = topicProgress.progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun RecentActivityItem(
    solution: com.dsafun.app.data.local.entity.UserSolutionEntity
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Problem #${solution.problemId}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = solution.language,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            
            Surface(
                color = when (solution.status) {
                    "SOLVED" -> Color(0xFF10B981)
                    "ATTEMPTED" -> Color(0xFFF59E0B)
                    else -> Color(0xFFEF4444)
                },
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = solution.status,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun MotivationalQuote() {
    val quotes = listOf(
        "The only way to do great work is to love what you do.",
        "Code is like humor. When you have to explain it, it's bad.",
        "First, solve the problem. Then, write the code.",
        "Experience is the name everyone gives to their mistakes.",
        "In order to be irreplaceable, one must always be different.",
        "Simplicity is the soul of efficiency.",
        "Make it work, make it right, make it fast.",
        "The best error message is the one that never shows up.",
        "Debugging is twice as hard as writing the code in the first place.",
        "Any fool can write code that a computer can understand. Good programmers write code that humans can understand."
    )
    
    val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    val quote = quotes[dayOfYear % quotes.size]
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(
            text = "\"$quote\"",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(Dimens.SpacingMedium)
        )
    }
}

// Made with Bob
