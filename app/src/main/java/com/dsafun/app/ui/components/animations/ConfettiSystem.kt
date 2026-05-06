package com.dsafun.app.ui.components.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Confetti particle system for celebrations
 */
@Composable
fun ConfettiSystem(
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {}
) {
    val density = LocalDensity.current
    val particles = remember {
        List(60) { index ->
            ConfettiParticle(
                color = confettiColors[index % confettiColors.size],
                startX = Random.nextFloat(),
                startY = -0.1f,
                velocityX = Random.nextFloat() * 4f - 2f,
                velocityY = Random.nextFloat() * 2f + 3f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 10f - 5f,
                size = Random.nextFloat() * 8f + 4f
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2500f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )
    
    LaunchedEffect(Unit) {
        delay(2500)
        onComplete()
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val progress = time / 2500f
        
        particles.forEach { particle ->
            val x = width * particle.startX + particle.velocityX * time * density.density / 10f
            val y = height * particle.startY + particle.velocityY * time * density.density / 10f + 
                    0.5f * 9.8f * (time / 100f) * (time / 100f) * density.density
            
            if (y < height + 50f) {
                val rotation = particle.rotation + particle.rotationSpeed * time / 10f
                val alpha = (1f - progress).coerceIn(0f, 1f)
                
                rotate(rotation, pivot = Offset(x, y)) {
                    drawRect(
                        color = particle.color.copy(alpha = alpha),
                        topLeft = Offset(x - particle.size / 2, y - particle.size / 2),
                        size = androidx.compose.ui.geometry.Size(particle.size, particle.size)
                    )
                }
            }
        }
    }
}

private data class ConfettiParticle(
    val color: Color,
    val startX: Float,
    val startY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val size: Float
)

private val confettiColors = listOf(
    Color(0xFFE91E63), // Pink
    Color(0xFF9C27B0), // Purple
    Color(0xFF3F51B5), // Indigo
    Color(0xFF2196F3), // Blue
    Color(0xFF00BCD4), // Cyan
    Color(0xFF4CAF50), // Green
    Color(0xFFFFEB3B), // Yellow
    Color(0xFFFF9800), // Orange
    Color(0xFFFF5722), // Deep Orange
)

// Made with Bob
