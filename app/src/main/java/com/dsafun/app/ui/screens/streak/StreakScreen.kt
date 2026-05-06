package com.dsafun.app.ui.screens.streak

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.alpha
import androidx.hilt.navigation.compose.hiltViewModel
import com.dsafun.app.data.local.entity.BadgeEntity
import com.dsafun.app.ui.components.gamification.BadgeIcon
import com.dsafun.app.ui.components.XpBar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Streak Screen - Shows streak stats, calendar, badges, and gamification
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreakScreen(
    viewModel: StreakViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedBadge by remember { mutableStateOf<BadgeEntity?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Streak & Progress") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Hero Section - Giant Flame
            StreakHeroSection(
                currentStreak = uiState.currentStreak,
                longestStreak = uiState.longestStreak
            )
            
            // 2. XP Card
            XpProgressCard(
                currentLevel = uiState.currentLevel,
                totalXp = uiState.totalXp,
                xpForNextLevel = uiState.xpForNextLevel
            )
            
            // 3. Monthly Calendar
            MonthlyCalendarSection(
                selectedMonth = uiState.selectedMonth,
                monthlyProgress = uiState.monthlyProgress,
                onMonthChanged = viewModel::onMonthChanged,
                onDaySelected = viewModel::onDaySelected
            )
            
            // 4. Freeze Tokens Card
            FreezeTokensCard(
                freezeTokens = uiState.freezeTokens,
                onUseFreezeToken = viewModel::onUseFreezeToken
            )
            
            // 5. Daily Goal Slider
            DailyGoalSection(
                dailyGoal = uiState.dailyGoal,
                onDailyGoalChanged = viewModel::onDailyGoalChanged
            )
            
            // 6. Streak Milestones
            StreakMilestonesSection(
                currentStreak = uiState.currentStreak
            )
            
            // 7. Badge Grid
            BadgeGridSection(
                badges = uiState.badges,
                onBadgeSelected = { badge ->
                    selectedBadge = badge
                }
            )
        }
    }
    
    // Badge Detail Dialog
    selectedBadge?.let { badge ->
        BadgeDetailDialog(
            badge = badge,
            onDismiss = { selectedBadge = null }
        )
    }
    
    // Show freeze token success snackbar
    if (uiState.showFreezeTokenSuccess) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000)
            viewModel.onDismissFreezeTokenSuccess()
        }
    }
}

/**
 * Section 1: Hero with giant pulsing flame
 */
@Composable
fun StreakHeroSection(
    currentStreak: Int,
    longestStreak: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Giant pulsing flame
            val infiniteTransition = rememberInfiniteTransition(label = "flame_pulse")
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "flame_scale"
            )
            
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(120.dp * scale)
                ) {
                    val flameColors = listOf(
                        Color(0xFFFF6B35),
                        Color(0xFFFF8C42),
                        Color(0xFFFFA500),
                        Color(0xFFFFD700)
                    )
                    
                    drawCircle(
                        brush = Brush.radialGradient(flameColors),
                        radius = size.minDimension / 2
                    )
                }
                
                Text(
                    text = "🔥",
                    fontSize = 64.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "$currentStreak Day Streak",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Text(
                text = "Best: $longestStreak days",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * Section 2: XP Progress Card
 */
@Composable
fun XpProgressCard(
    currentLevel: Int,
    totalXp: Int,
    xpForNextLevel: Int
) {
    // Calculate XP in current level
    val xpInCurrentLevel = totalXp % xpForNextLevel
    val xpRemaining = xpForNextLevel - xpInCurrentLevel
    val progress = if (xpForNextLevel > 0) {
        (xpInCurrentLevel.toFloat() / xpForNextLevel).coerceIn(0f, 1f)
    } else {
        1f
    }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with level and total XP
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Level badge
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$currentLevel",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    
                    Column {
                        Text(
                            text = "Level $currentLevel",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$totalXp Total XP",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            
            // Progress bar
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$xpInCurrentLevel / $xpForNextLevel XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$xpRemaining XP to Level ${currentLevel + 1}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Progress bar background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    // Progress fill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}

/**
 * Section 3: Monthly Calendar
 */
@Composable
fun MonthlyCalendarSection(
    selectedMonth: YearMonth,
    monthlyProgress: List<com.dsafun.app.data.local.entity.DailyProgressEntity?>,
    onMonthChanged: (Int) -> Unit,
    onDaySelected: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Month navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onMonthChanged(-1) }) {
                    Icon(Icons.Filled.ArrowBack, "Previous month")
                }
                
                Text(
                    text = selectedMonth.getDisplayName(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(onClick = { onMonthChanged(1) }) {
                    Icon(Icons.Filled.ArrowForward, "Next month")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Day labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Calendar grid (simplified - would need proper calendar logic)
            CalendarGrid(
                selectedMonth = selectedMonth,
                monthlyProgress = monthlyProgress,
                onDaySelected = onDaySelected
            )
        }
    }
}

@Composable
fun CalendarGrid(
    selectedMonth: YearMonth,
    monthlyProgress: List<com.dsafun.app.data.local.entity.DailyProgressEntity?>,
    onDaySelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    calendar.set(selectedMonth.year, selectedMonth.month - 1, 1)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        var dayCounter = 1
        for (week in 0..5) {
            if (dayCounter > daysInMonth) break
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (dayOfWeek in 0..6) {
                    val shouldShowDay = if (week == 0) {
                        dayOfWeek >= firstDayOfWeek
                    } else {
                        dayCounter <= daysInMonth
                    }
                    
                    if (shouldShowDay && dayCounter <= daysInMonth) {
                        val date = "${selectedMonth.year}-${selectedMonth.month.toString().padStart(2, '0')}-${dayCounter.toString().padStart(2, '0')}"
                        val progress = monthlyProgress.getOrNull(dayCounter - 1)
                        val isToday = date == today
                        val isFuture = date > today
                        
                        CalendarDay(
                            day = dayCounter,
                            isComplete = progress?.streakActive == true,
                            isMissed = progress != null && !progress.streakActive,
                            isToday = isToday,
                            isFuture = isFuture,
                            onClick = { onDaySelected(date) }
                        )
                        dayCounter++
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.CalendarDay(
    day: Int,
    isComplete: Boolean,
    isMissed: Boolean,
    isToday: Boolean,
    isFuture: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isComplete -> Color(0xFF10B981) // Emerald
        isMissed -> Color(0xFFF43F5E) // Rose
        isFuture -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.surface
    }
    
    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .then(
                if (isToday) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                } else Modifier
            )
            .clickable(enabled = !isFuture, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = if (isComplete || isMissed) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Section 4: Freeze Tokens Card
 */
@Composable
fun FreezeTokensCard(
    freezeTokens: Int,
    onUseFreezeToken: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Freeze Tokens",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Use a freeze token to save your streak if you miss a day",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(2) { index ->
                    Text(
                        text = "❄️",
                        fontSize = 32.sp,
                        modifier = Modifier.alpha(if (index < freezeTokens) 1f else 0.3f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onUseFreezeToken,
                enabled = freezeTokens > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Use Freeze Token")
            }
        }
    }
}

/**
 * Section 5: Daily Goal Slider
 */
@Composable
fun DailyGoalSection(
    dailyGoal: Int,
    onDailyGoalChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Daily Goal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Set your daily problem-solving target",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$dailyGoal problems/day",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Slider(
                value = dailyGoal.toFloat(),
                onValueChange = { onDailyGoalChanged(it.toInt()) },
                valueRange = 1f..10f,
                steps = 8
            )
        }
    }
}

/**
 * Section 6: Streak Milestones
 */
@Composable
fun StreakMilestonesSection(
    currentStreak: Int
) {
    val milestones = listOf(3, 7, 14, 30, 60, 100, 200, 365)
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Streak Milestones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(milestones) { milestone ->
                    MilestoneItem(
                        milestone = milestone,
                        isUnlocked = currentStreak >= milestone
                    )
                }
            }
        }
    }
}

@Composable
fun MilestoneItem(
    milestone: Int,
    isUnlocked: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    if (isUnlocked) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isUnlocked) "🔥" else "🔒",
                fontSize = 28.sp
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "$milestone days",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = if (isUnlocked) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

/**
 * Section 7: Badge Grid
 */
@Composable
fun BadgeGridSection(
    badges: List<BadgeEntity>,
    onBadgeSelected: (BadgeEntity) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Badges",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Grid of badges (3 columns)
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                badges.chunked(3).forEach { rowBadges ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowBadges.forEach { badge ->
                            BadgeItem(
                                badge = badge,
                                onClick = { onBadgeSelected(badge) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Fill remaining slots
                        repeat(3 - rowBadges.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeItem(
    badge: BadgeEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            BadgeIcon(
                iconType = badge.iconType,
                isLocked = !badge.isUnlocked,
                modifier = Modifier.size(48.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = badge.name,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            color = if (badge.isUnlocked) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

// Made with Bob

/**
 * Badge Detail Dialog - Shows badge information
 */
@Composable
fun BadgeDetailDialog(
    badge: BadgeEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        if (badge.isUnlocked) 
                            MaterialTheme.colorScheme.primaryContainer
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                BadgeIcon(
                    iconType = badge.iconType,
                    isLocked = !badge.isUnlocked,
                    size = 64f
                )
            }
        },
        title = {
            Text(
                text = badge.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                
                if (badge.isUnlocked && badge.unlockedAt != null) {
                    Divider()
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Unlocked on ${formatDate(badge.unlockedAt)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Divider()
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Keep going to unlock this badge!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

/**
 * Format date for badge unlock display
 */
private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}