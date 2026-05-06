package com.dsafun.app.ui.components.timer

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BreathingAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing_scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing_alpha"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    Canvas(modifier = modifier.size(200.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxRadius = size.minDimension / 2

        // Outer circle
        drawCircle(
            color = primaryColor.copy(alpha = alpha * 0.3f),
            radius = maxRadius * scale,
            center = androidx.compose.ui.geometry.Offset(centerX, centerY)
        )

        // Middle circle
        drawCircle(
            color = secondaryColor.copy(alpha = alpha * 0.5f),
            radius = maxRadius * scale * 0.7f,
            center = androidx.compose.ui.geometry.Offset(centerX, centerY)
        )

        // Inner circle
        drawCircle(
            color = primaryColor.copy(alpha = alpha),
            radius = maxRadius * scale * 0.4f,
            center = androidx.compose.ui.geometry.Offset(centerX, centerY)
        )
    }
}

// Made with Bob
