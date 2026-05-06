package com.dsafun.app.ui.components.gamification

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dsafun.app.data.local.entity.BadgeEntity
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneCelebrationSheet(
    badges: List<BadgeEntity>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentBadgeIndex by remember { mutableStateOf(0) }
    val currentBadge = badges.getOrNull(currentBadgeIndex)
    
    // Auto-advance to next badge after 3 seconds
    LaunchedEffect(currentBadgeIndex) {
        if (currentBadgeIndex < badges.size) {
            delay(3000)
            if (currentBadgeIndex < badges.size - 1) {
                currentBadgeIndex++
            } else {
                onDismiss()
            }
        }
    }
    
    if (currentBadge != null) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = modifier
        ) {
            BadgeCelebrationContent(
                badge = currentBadge,
                badgeNumber = currentBadgeIndex + 1,
                totalBadges = badges.size,
                onDismiss = onDismiss
            )
        }
    }
}

@Composable
private fun BadgeCelebrationContent(
    badge: BadgeEntity,
    badgeNumber: Int,
    totalBadges: Int,
    onDismiss: () -> Unit
) {
    // Scale animation
    val scale by rememberInfiniteTransition(label = "scale").animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Badge count indicator
        if (totalBadges > 1) {
            Text(
                text = "Badge $badgeNumber of $totalBadges",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        
        // "Just Unlocked!" text
        Text(
            text = "🎉 Just Unlocked! 🎉",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        // Badge icon with animation
        Box(
            modifier = Modifier
                .size(120.dp)
                .scale(scale)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(60.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            BadgeIcon(
                iconType = badge.iconType,
                isLocked = false,
                size = 80f
            )
        }
        
        // Badge name
        Text(
            text = badge.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        // Badge description
        Text(
            text = badge.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Dismiss button
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = if (badgeNumber < totalBadges) "Next Badge" else "Awesome!",
                style = MaterialTheme.typography.labelLarge
            )
        }
        
        // Skip all button if multiple badges
        if (totalBadges > 1 && badgeNumber < totalBadges) {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Skip All")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Made with Bob