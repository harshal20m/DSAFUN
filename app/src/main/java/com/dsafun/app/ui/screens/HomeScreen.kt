package com.dsafun.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.domain.usecase.HomeStats
import com.dsafun.app.domain.usecase.TopicProgress
import com.dsafun.app.ui.components.StreakRing
import com.dsafun.app.ui.components.XpBar
import com.dsafun.app.ui.screens.home.HomeViewModel
import com.dsafun.app.ui.theme.Dimens
import java.util.*

@Composable
fun HomeScreen(
    onNavigateToProblem: (Int) -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToProblems: () -> Unit = {},
    onNavigateToTimer: () -> Unit = {},
    onNavigateToAnalytics: () -> Unit = {},
    onNavigateToStreak: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeStats by viewModel.homeStats.collectAsState()
    
    // Animation state
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    if (homeStats == null) {
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
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        // Hero Section with Greeting & Stats
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInVertically()
            ) {
                HeroSection(
                    userName = stats.userName,
                    level = stats.level,
                    totalXp = stats.totalXp,
                    xpToNextLevel = stats.xpToNextLevel,
                    onSettingsClick = onNavigateToSettings
                )
            }
        }
        
        // Quick Stats Cards Row
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(delayMillis = 100)) + 
                        slideInVertically(animationSpec = tween(delayMillis = 100))
            ) {
                QuickStatsRow(
                    currentStreak = stats.currentStreak,
                    bestStreak = stats.bestStreak,
                    todaySolved = stats.todaySolved,
                    dailyGoal = stats.dailyGoal,
                    freezeTokens = stats.freezeTokens,
                    onStreakClick = onNavigateToStreak
                )
            }
        }
        
        // Daily Challenge Card
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(delayMillis = 200)) + 
                        slideInVertically(animationSpec = tween(delayMillis = 200))
            ) {
                stats.dailyChallenge?.let { challenge ->
                    DailyChallengeCard(
                        problem = challenge,
                        isCompleted = stats.isDailyChallengeCompleted,
                        onClick = { onNavigateToProblem(challenge.id) }
                    )
                }
            }
        }
        
        // Quick Actions
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(delayMillis = 300)) + 
                        slideInVertically(animationSpec = tween(delayMillis = 300))
            ) {
                QuickActionsSection(
                    onNavigateToProblems = onNavigateToProblems,
                    onNavigateToTimer = onNavigateToTimer,
                    onNavigateToAnalytics = onNavigateToAnalytics
                )
            }
        }
        
        // Topic Progress
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(delayMillis = 400)) + 
                        slideInVertically(animationSpec = tween(delayMillis = 400))
            ) {
                TopicProgressSection(topicProgresses = stats.topicProgresses)
            }
        }
        
        // Recent Activity
        if (stats.recentSolutions.isNotEmpty()) {
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(delayMillis = 500)) + 
                            slideInVertically(animationSpec = tween(delayMillis = 500))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.SpacingMedium),
                        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
                    ) {
                        Text(
                            text = "Recent Activity",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        
                        stats.recentSolutions.take(3).forEach { solution ->
                            RecentActivityItem(solution = solution)
                        }
                    }
                }
            }
        }
        
        // Motivational Quote
        item {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(delayMillis = 600)) + 
                        slideInVertically(animationSpec = tween(delayMillis = 600))
            ) {
                MotivationalQuote(
                    modifier = Modifier.padding(horizontal = Dimens.SpacingMedium)
                )
            }
        }
    }
}

@Composable
private fun HeroSection(
    userName: String,
    level: Int,
    totalXp: Int,
    xpToNextLevel: Int,
    onSettingsClick: () -> Unit
) {
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }
    
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(Dimens.SpacingMedium)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$greeting,",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        
        // XP Bar
        XpBar(
            currentXp = totalXp,
            xpToNextLevel = xpToNextLevel,
            level = level
        )
    }
}

@Composable
private fun QuickStatsRow(
    currentStreak: Int,
    bestStreak: Int,
    todaySolved: Int,
    dailyGoal: Int,
    freezeTokens: Int,
    onStreakClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = Dimens.SpacingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        // Streak Card
        item {
            StatCard(
                icon = "🔥",
                title = "Streak",
                value = "$currentStreak",
                subtitle = "Best: $bestStreak",
                color = Color(0xFFFF6B35),
                onClick = onStreakClick
            )
        }
        
        // Today's Progress Card
        item {
            StatCard(
                icon = "✓",
                title = "Today",
                value = "$todaySolved/$dailyGoal",
                subtitle = if (todaySolved >= dailyGoal) "Goal reached!" else "${dailyGoal - todaySolved} to go",
                color = if (todaySolved >= dailyGoal) Color(0xFF10B981) else MaterialTheme.colorScheme.primary,
                onClick = {}
            )
        }
        
        // Freeze Tokens Card
        item {
            StatCard(
                icon = "❄️",
                title = "Freeze",
                value = "$freezeTokens",
                subtitle = "Tokens",
                color = Color(0xFF3B82F6),
                onClick = {}
            )
        }
    }
}

@Composable
private fun StatCard(
    icon: String,
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    
    Card(
        modifier = Modifier
            .width(140.dp)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = color
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
private fun DailyChallengeCard(
    problem: com.dsafun.app.domain.model.Problem,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isCompleted) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    
    val contentColor = if (isCompleted) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCompleted) "✓" else "⭐",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Column {
                        Text(
                            text = "Daily Challenge",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = contentColor
                        )
                        if (isCompleted) {
                            Text(
                                text = "Completed!",
                                style = MaterialTheme.typography.labelSmall,
                                color = contentColor.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
                
                if (!isCompleted) {
                    Surface(
                        color = when (problem.difficulty) {
                            "Easy" -> Color(0xFF10B981)
                            "Medium" -> Color(0xFFF59E0B)
                            else -> Color(0xFFEF4444)
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = problem.difficulty,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = problem.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = contentColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = contentColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = problem.topic,
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                if (!isCompleted) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionsSection(
    onNavigateToProblems: () -> Unit,
    onNavigateToTimer: () -> Unit,
    onNavigateToAnalytics: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
        ) {
            QuickActionButton(
                icon = Icons.Default.List,
                label = "Browse Problems",
                onClick = onNavigateToProblems,
                modifier = Modifier.weight(1f)
            )
            
            QuickActionButton(
                icon = Icons.Default.Timer,
                label = "Focus Timer",
                onClick = onNavigateToTimer,
                modifier = Modifier.weight(1f)
            )
        }
        
        QuickActionButton(
            icon = Icons.Default.Analytics,
            label = "View Analytics",
            onClick = onNavigateToAnalytics,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun TopicProgressSection(topicProgresses: List<TopicProgress>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Topic Progress",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Text(
                text = "Top 5",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        
        topicProgresses.take(5).forEach { topicProgress ->
            TopicProgressBar(topicProgress = topicProgress)
        }
    }
}

@Composable
private fun TopicProgressBar(topicProgress: TopicProgress) {
    val animatedProgress by animateFloatAsState(
        targetValue = topicProgress.progress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = topicProgress.topic,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = "${topicProgress.solved}/${topicProgress.total}",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
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
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when (solution.status) {
                                "SOLVED" -> Color(0xFF10B981).copy(alpha = 0.2f)
                                "ATTEMPTED" -> Color(0xFFF59E0B).copy(alpha = 0.2f)
                                else -> Color(0xFFEF4444).copy(alpha = 0.2f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (solution.status) {
                            "SOLVED" -> "✓"
                            "ATTEMPTED" -> "⚡"
                            else -> "✗"
                        },
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
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
            }
            
            Surface(
                color = when (solution.status) {
                    "SOLVED" -> Color(0xFF10B981)
                    "ATTEMPTED" -> Color(0xFFF59E0B)
                    else -> Color(0xFFEF4444)
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = solution.status,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun MotivationalQuote(modifier: Modifier = Modifier) {
    val quotes = listOf(
        "The only way to do great work is to love what you do." to "Steve Jobs",
        "Code is like humor. When you have to explain it, it's bad." to "Cory House",
        "First, solve the problem. Then, write the code." to "John Johnson",
        "Make it work, make it right, make it fast." to "Kent Beck",
        "Simplicity is the soul of efficiency." to "Austin Freeman",
        "Any fool can write code that a computer can understand. Good programmers write code that humans can understand." to "Martin Fowler",
        "Experience is the name everyone gives to their mistakes." to "Oscar Wilde",
        "The best error message is the one that never shows up." to "Thomas Fuchs"
    )
    
    val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    val (quote, author) = quotes[dayOfYear % quotes.size]
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FormatQuote,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
            
            Text(
                text = quote,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    lineHeight = 24.sp
                ),
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            
            Text(
                text = "— $author",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

// Made with Bob
