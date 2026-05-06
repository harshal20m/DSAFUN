package com.dsafun.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Reusable empty state component with icon, title, description, and optional action button.
 * Includes fade-in animation for smooth appearance.
 */
@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "empty_state_alpha"
    )
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .alpha(alpha)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 400.dp)
        )
        
        if (actionLabel != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onActionClick,
                modifier = Modifier.height(48.dp)
            ) {
                Text(text = actionLabel)
            }
        }
    }
}

/**
 * Pre-configured empty states for common scenarios
 */
object EmptyStates {
    
    @Composable
    fun NoProblems(
        onAddProblem: (() -> Unit)? = null
    ) {
        EmptyState(
            icon = Icons.Outlined.Code,
            title = "No Problems Yet",
            description = "Start your DSA journey by adding your first problem to solve.",
            actionLabel = if (onAddProblem != null) "Add Problem" else null,
            onActionClick = onAddProblem
        )
    }
    
    @Composable
    fun NoSubmissions() {
        EmptyState(
            icon = Icons.Outlined.CheckCircle,
            title = "No Submissions Yet",
            description = "Complete your first problem to see your submission history and track your progress."
        )
    }
    
    @Composable
    fun NoAnalytics() {
        EmptyState(
            icon = Icons.Outlined.BarChart,
            title = "No Data Available",
            description = "Start solving problems to see your analytics and performance insights."
        )
    }
    
    @Composable
    fun NoBadges() {
        EmptyState(
            icon = Icons.Outlined.EmojiEvents,
            title = "No Badges Earned",
            description = "Keep solving problems and maintaining streaks to unlock achievement badges."
        )
    }
    
    @Composable
    fun NoStreak() {
        EmptyState(
            icon = Icons.Outlined.LocalFireDepartment,
            title = "Start Your Streak",
            description = "Solve at least one problem today to begin your daily streak and earn rewards."
        )
    }
    
    @Composable
    fun NoTimerSessions() {
        EmptyState(
            icon = Icons.Outlined.Timer,
            title = "No Focus Sessions",
            description = "Start a focus timer session to track your study time and improve concentration."
        )
    }
    
    @Composable
    fun SearchNoResults(query: String) {
        EmptyState(
            icon = Icons.Outlined.SearchOff,
            title = "No Results Found",
            description = "No problems match \"$query\". Try a different search term or filter."
        )
    }
    
    @Composable
    fun NetworkError(onRetry: () -> Unit) {
        EmptyState(
            icon = Icons.Outlined.CloudOff,
            title = "Connection Error",
            description = "Unable to load data. Please check your internet connection and try again.",
            actionLabel = "Retry",
            onActionClick = onRetry
        )
    }
    
    @Composable
    fun GenericError(
        message: String = "Something went wrong",
        onRetry: (() -> Unit)? = null
    ) {
        EmptyState(
            icon = Icons.Outlined.ErrorOutline,
            title = "Oops!",
            description = message,
            actionLabel = if (onRetry != null) "Try Again" else null,
            onActionClick = onRetry
        )
    }
}

// Made with Bob
