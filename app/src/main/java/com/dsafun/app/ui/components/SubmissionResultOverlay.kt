package com.dsafun.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsafun.app.ui.theme.Dimens

/**
 * Full-screen overlay showing submission results
 */
@Composable
fun SubmissionResultOverlay(
    status: String, // "SOLVED", "ATTEMPTED", "FAILED"
    passedCount: Int,
    totalCount: Int,
    xpGained: Int,
    timeTaken: Long,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )
    
    // Animated XP counter
    val animatedXp by animateIntAsState(
        targetValue = if (visible) xpGained else 0,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "xp"
    )
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    val (backgroundColor, iconColor, icon, title) = when (status) {
        "SOLVED" -> Tuple4(
            Color(0xFF10B981).copy(alpha = 0.1f),
            Color(0xFF10B981),
            Icons.Default.CheckCircle,
            "All Tests Passed! 🎉"
        )
        "ATTEMPTED" -> Tuple4(
            Color(0xFFF59E0B).copy(alpha = 0.1f),
            Color(0xFFF59E0B),
            Icons.Default.Warning,
            "$passedCount/$totalCount Tests Passed"
        )
        else -> Tuple4(
            Color(0xFFEF4444).copy(alpha = 0.1f),
            Color(0xFFEF4444),
            Icons.Default.Close,
            "Tests Failed"
        )
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = alpha * 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .scale(scale)
                .padding(32.dp)
                .fillMaxWidth(0.9f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(backgroundColor)
                    .padding(Dimens.SpacingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = iconColor
                )
                
                Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
                
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
                
                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // XP Gained
                    if (xpGained > 0) {
                        StatItem(
                            label = "XP Gained",
                            value = "+$animatedXp",
                            color = Color(0xFF10B981)
                        )
                    }
                    
                    // Time Taken
                    StatItem(
                        label = "Time",
                        value = formatTime(timeTaken),
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    // Tests Passed
                    StatItem(
                        label = "Tests",
                        value = "$passedCount/$totalCount",
                        color = if (passedCount == totalCount) Color(0xFF10B981) else Color(0xFFF59E0B)
                    )
                }
                
                Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
                
                // Dismiss Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", minutes, secs)
}

private data class Tuple4<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

// Made with Bob
