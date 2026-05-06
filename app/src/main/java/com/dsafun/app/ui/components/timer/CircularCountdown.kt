package com.dsafun.app.ui.components.timer

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsafun.app.domain.model.TimerState

@Composable
fun CircularCountdown(
    remainingSeconds: Long,
    totalSeconds: Long,
    timerState: TimerState,
    currentSession: Int,
    targetSessions: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (totalSeconds > 0) {
        (remainingSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "timer_progress"
    )

    val arcColor = when (timerState) {
        TimerState.RUNNING -> MaterialTheme.colorScheme.primary
        TimerState.BREAK -> Color(0xFFF59E0B) // Amber
        TimerState.PAUSED -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.primary
    }

    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = modifier.size(280.dp),
        contentAlignment = Alignment.Center
    ) {
        // Canvas for circular progress
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 24.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val radius = diameter / 2
            val topLeft = Offset(
                x = (size.width - diameter) / 2,
                y = (size.height - diameter) / 2
            )

            // Background track
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                color = arcColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Center content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Time remaining
            Text(
                text = formatTime(remainingSeconds),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Session dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(targetSessions) { index ->
                    val dotColor = if (index < currentSession - 1) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    }

                    Canvas(modifier = Modifier.size(12.dp)) {
                        drawCircle(
                            color = dotColor,
                            radius = size.minDimension / 2
                        )
                    }
                }
            }

            // Session label
            Text(
                text = "Session $currentSession of $targetSessions",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}

// Made with Bob
