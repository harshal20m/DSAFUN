package com.dsafun.app.ui.components.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Full-screen overlay that shows when user levels up
 */
@Composable
fun LevelUpOverlay(
    newLevel: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )
    
    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        visible = false
        delay(300)
        onDismiss()
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = alpha * 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .scale(scale)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Gradient background for text
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "LEVEL UP!",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 48.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Level $newLevel",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 64.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "🎉",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 72.sp
                        )
                    )
                }
            }
        }
    }
}

// Made with Bob
