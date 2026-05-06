package com.dsafun.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Circular progress ring showing daily goal progress
 */
@Composable
fun StreakRing(
    solved: Int,
    goal: Int,
    streak: Int,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    val progress = if (goal > 0) (solved.toFloat() / goal).coerceIn(0f, 1f) else 0f
    
    // Animate progress
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )
    
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurface = MaterialTheme.colorScheme.onSurface
    val flameColor = Color(0xFFFF6B35) // Orange flame color
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Canvas for ring
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = size.toPx()
            val strokeWidth = 12.dp.toPx()
            val radius = (canvasSize - strokeWidth) / 2
            val center = Offset(canvasSize / 2, canvasSize / 2)
            
            // Background ring
            drawArc(
                color = surfaceVariant,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            
            // Progress ring
            if (animatedProgress > 0f) {
                drawArc(
                    color = primaryColor,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
            
            // Flame icon if streak > 0
            if (streak > 0) {
                val flameSize = 16.dp.toPx()
                val flameOffset = Offset(
                    center.x - flameSize / 2,
                    center.y - radius - strokeWidth - flameSize - 4.dp.toPx()
                )
                
                // Simple flame shape (triangle)
                drawCircle(
                    color = flameColor,
                    radius = flameSize / 2,
                    center = Offset(flameOffset.x + flameSize / 2, flameOffset.y + flameSize / 2)
                )
            }
        }
        
        // Center text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$solved/$goal",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = onSurface
            )
            if (streak > 0) {
                Text(
                    text = "$streak day${if (streak != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = flameColor
                )
            }
        }
    }
}

// Made with Bob