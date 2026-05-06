package com.dsafun.app.ui.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsafun.app.domain.usecase.WeeklyEntry
import kotlin.math.max

@Composable
fun LineChart(
    data: List<WeeklyEntry>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val surfaceColor = MaterialTheme.colorScheme.surface
    
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(
        color = onSurfaceColor.copy(alpha = 0.6f),
        fontSize = 10.sp
    )
    
    // Animation
    val animationProgress = remember { Animatable(0f) }
    
    LaunchedEffect(data) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        if (data.isEmpty()) return@Canvas
        
        val padding = 40.dp.toPx()
        val chartWidth = size.width - padding * 2
        val chartHeight = size.height - padding * 2
        
        // Find max value for scaling
        val maxValue = data.maxOfOrNull { it.problemsSolved }?.toFloat() ?: 1f
        val scaledMax = max(maxValue, 1f)
        
        // Calculate points
        val points = data.mapIndexed { index, entry ->
            val x = padding + (index.toFloat() / (data.size - 1).coerceAtLeast(1)) * chartWidth
            val y = padding + chartHeight - (entry.problemsSolved / scaledMax) * chartHeight
            Offset(x, y)
        }
        
        // Draw Y-axis labels
        val ySteps = 5
        for (i in 0..ySteps) {
            val value = (scaledMax * i / ySteps).toInt()
            val y = padding + chartHeight - (i.toFloat() / ySteps) * chartHeight
            
            drawText(
                textMeasurer = textMeasurer,
                text = value.toString(),
                topLeft = Offset(0f, y - 6.dp.toPx()),
                style = labelStyle
            )
            
            // Grid line
            drawLine(
                color = onSurfaceColor.copy(alpha = 0.1f),
                start = Offset(padding, y),
                end = Offset(size.width - padding, y),
                strokeWidth = 1.dp.toPx()
            )
        }
        
        // Draw X-axis labels (week numbers)
        data.forEachIndexed { index, entry ->
            if (index % 2 == 0 || data.size <= 6) { // Show every other label or all if few points
                val x = padding + (index.toFloat() / (data.size - 1).coerceAtLeast(1)) * chartWidth
                drawText(
                    textMeasurer = textMeasurer,
                    text = "W${entry.weekNumber}",
                    topLeft = Offset(x - 10.dp.toPx(), size.height - padding + 10.dp.toPx()),
                    style = labelStyle
                )
            }
        }
        
        if (points.size < 2) return@Canvas
        
        // Animate the path
        val animatedPoints = points.take((points.size * animationProgress.value).toInt().coerceAtLeast(2))
        
        // Create gradient fill
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(
                primaryColor.copy(alpha = 0.3f),
                primaryColor.copy(alpha = 0.0f)
            ),
            startY = padding,
            endY = padding + chartHeight
        )
        
        // Draw gradient fill under the line
        val fillPath = Path().apply {
            moveTo(animatedPoints.first().x, padding + chartHeight)
            animatedPoints.forEach { point ->
                lineTo(point.x, point.y)
            }
            lineTo(animatedPoints.last().x, padding + chartHeight)
            close()
        }
        
        drawPath(
            path = fillPath,
            brush = gradientBrush
        )
        
        // Draw smooth bezier curve line
        val linePath = Path().apply {
            moveTo(animatedPoints.first().x, animatedPoints.first().y)
            
            for (i in 0 until animatedPoints.size - 1) {
                val current = animatedPoints[i]
                val next = animatedPoints[i + 1]
                
                // Calculate control points for smooth curve
                val controlX1 = current.x + (next.x - current.x) / 3
                val controlY1 = current.y
                val controlX2 = current.x + 2 * (next.x - current.x) / 3
                val controlY2 = next.y
                
                cubicTo(
                    controlX1, controlY1,
                    controlX2, controlY2,
                    next.x, next.y
                )
            }
        }
        
        drawPath(
            path = linePath,
            color = primaryColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
        
        // Draw points
        animatedPoints.forEach { point ->
            drawCircle(
                color = primaryColor,
                radius = 4.dp.toPx(),
                center = point
            )
            drawCircle(
                color = surfaceColor,
                radius = 2.dp.toPx(),
                center = point
            )
        }
    }
}

// Made with Bob