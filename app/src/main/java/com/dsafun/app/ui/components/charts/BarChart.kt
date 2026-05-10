package com.dsafun.app.ui.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsafun.app.domain.usecase.LanguageCount
import kotlin.math.max

@Composable
fun BarChart(
    data: List<LanguageCount>,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color(0xFF10B981), // Emerald
        Color(0xFF3B82F6), // Blue
        Color(0xFFF59E0B), // Amber
        Color(0xFFEF4444), // Red
        Color(0xFF8B5CF6)  // Purple
    )
    
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(
        color = onSurfaceColor.copy(alpha = 0.8f),
        fontSize = 12.sp
    )
    val valueStyle = TextStyle(
        color = onSurfaceColor,
        fontSize = 11.sp
    )
    
    // Animation
    val animationProgress = remember { Animatable(0f) }
    
    LaunchedEffect(data) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
    }
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height((data.size * 50).dp)
            .padding(16.dp)
    ) {
        if (data.isEmpty()) return@Canvas
        
        val maxValue = data.maxOfOrNull { it.count }?.toFloat() ?: 1f
        val scaledMax = max(maxValue, 1f)
        
        val barHeight = 32.dp.toPx()
        val barSpacing = 18.dp.toPx()
        val labelWidth = 100.dp.toPx() // Increased from 80dp to 100dp for longer language names
        val chartWidth = size.width - labelWidth - 60.dp.toPx()
        
        data.forEachIndexed { index, item ->
            val y = index * (barHeight + barSpacing)
            val barWidth = (item.count / scaledMax) * chartWidth * animationProgress.value
            val color = colors[index % colors.size]
            
            // Draw language label
            drawText(
                textMeasurer = textMeasurer,
                text = item.language,
                topLeft = Offset(0f, y + barHeight / 2 - 8.dp.toPx()),
                style = labelStyle
            )
            
            // Draw bar
            drawRoundRect(
                color = color.copy(alpha = 0.2f),
                topLeft = Offset(labelWidth, y),
                size = Size(chartWidth, barHeight),
                cornerRadius = CornerRadius(8.dp.toPx())
            )
            
            drawRoundRect(
                color = color,
                topLeft = Offset(labelWidth, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8.dp.toPx())
            )
            
            // Draw value label
            if (barWidth > 40.dp.toPx()) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = item.count.toString(),
                    topLeft = Offset(
                        labelWidth + barWidth - 30.dp.toPx(),
                        y + barHeight / 2 - 7.dp.toPx()
                    ),
                    style = valueStyle.copy(color = Color.White)
                )
            } else {
                drawText(
                    textMeasurer = textMeasurer,
                    text = item.count.toString(),
                    topLeft = Offset(
                        labelWidth + barWidth + 8.dp.toPx(),
                        y + barHeight / 2 - 7.dp.toPx()
                    ),
                    style = valueStyle
                )
            }
        }
    }
}

// Made with Bob