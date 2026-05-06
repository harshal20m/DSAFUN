package com.dsafun.app.ui.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.dsafun.app.domain.usecase.TopicCount

@Composable
fun DonutChart(
    data: List<TopicCount>,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color(0xFF10B981), // Emerald
        Color(0xFF3B82F6), // Blue
        Color(0xFFF59E0B), // Amber
        Color(0xFFEF4444), // Red
        Color(0xFF8B5CF6), // Purple
        Color(0xFFEC4899), // Pink
        Color(0xFF14B8A6), // Teal
        Color(0xFFF97316)  // Orange
    )
    
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    
    // Animation
    val animationProgress = remember { Animatable(0f) }
    
    LaunchedEffect(data) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }
    
    val total = data.sumOf { it.count }.toFloat()
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Donut chart
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 40.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2
                val centerX = size.width / 2
                val centerY = size.height / 2
                
                var startAngle = -90f
                
                data.forEachIndexed { index, item ->
                    val sweepAngle = (item.count / total) * 360f * animationProgress.value
                    val color = colors[index % colors.size]
                    
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(centerX - radius, centerY - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Butt
                        )
                    )
                    
                    startAngle += sweepAngle
                }
            }
            
            // Center label
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = data.sumOf { it.count }.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = onSurfaceColor
                )
                Text(
                    text = "Problems",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurfaceColor.copy(alpha = 0.6f)
                )
            }
        }
        
        // Legend
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Color indicator
                        Canvas(modifier = Modifier.size(12.dp)) {
                            drawCircle(
                                color = colors[index % colors.size]
                            )
                        }
                        
                        Text(
                            text = item.topic,
                            style = MaterialTheme.typography.bodyMedium,
                            color = onSurfaceColor
                        )
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.count.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = onSurfaceColor
                        )
                        
                        Text(
                            text = "${((item.count / total) * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurfaceColor.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

// Made with Bob