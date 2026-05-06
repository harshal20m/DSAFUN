package com.dsafun.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Horizontal XP progress bar with level badge
 */
@Composable
fun XpBar(
    currentXp: Int,
    xpToNextLevel: Int,
    level: Int,
    modifier: Modifier = Modifier
) {
    // Calculate XP progress within current level
    // currentXp is the total XP, we need to find XP in current level
    val xpInCurrentLevel = currentXp % xpToNextLevel
    val progress = if (xpToNextLevel > 0) {
        (xpInCurrentLevel.toFloat() / xpToNextLevel).coerceIn(0f, 1f)
    } else {
        1f // Max level reached
    }
    
    // Animate progress
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "xp_progress"
    )
    
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val onSurface = MaterialTheme.colorScheme.onSurface
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Level badge
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryColor, secondaryColor)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "LVL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 8.sp
                    ),
                    color = onPrimary.copy(alpha = 0.8f)
                )
                Text(
                    text = level.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = onPrimary
                )
            }
        }
        
        // Progress bar
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // XP text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "XP Progress",
                    style = MaterialTheme.typography.labelSmall,
                    color = onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = if (xpToNextLevel > 0) {
                        "$xpToNextLevel XP to next level"
                    } else {
                        "Max Level!"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = onSurface.copy(alpha = 0.7f)
                )
            }
            
            // Progress bar background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(surfaceVariant)
            ) {
                // Progress fill
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(primaryColor, secondaryColor)
                            )
                        )
                )
            }
        }
    }
}

// Made with Bob