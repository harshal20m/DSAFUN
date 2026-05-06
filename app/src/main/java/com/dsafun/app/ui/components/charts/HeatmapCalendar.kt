package com.dsafun.app.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.dsafun.app.domain.usecase.HeatmapEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

@Composable
fun HeatmapCalendar(
    data: List<HeatmapEntry>,
    modifier: Modifier = Modifier
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryColor = Color(0xFF10B981) // Emerald green
    
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(
        color = onSurfaceColor.copy(alpha = 0.6f),
        fontSize = 10.sp
    )
    
    Column(modifier = modifier) {
        // Month labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec").forEach { month ->
                Text(
                    text = month,
                    style = MaterialTheme.typography.labelSmall,
                    color = onSurfaceColor.copy(alpha = 0.6f)
                )
            }
        }
        
        // Heatmap grid
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            val cellSize = 12.dp.toPx()
            val gap = 2.dp.toPx()
            val startX = 24.dp.toPx()
            val startY = 0f
            
            // Draw day labels (M, W, F)
            val dayLabels = listOf("M", "W", "F")
            dayLabels.forEachIndexed { index, label ->
                val y = startY + (index * 2 + 1) * (cellSize + gap)
                drawText(
                    textMeasurer = textMeasurer,
                    text = label,
                    topLeft = Offset(0f, y - 6.dp.toPx()),
                    style = textStyle
                )
            }
            
            // Group data by date
            val dataMap = data.associateBy { it.date }
            
            // Calculate weeks
            val today = LocalDate.now()
            val startDate = today.minusDays(364)
            
            var currentDate = startDate
            var weekIndex = 0
            
            while (!currentDate.isAfter(today)) {
                val dayOfWeek = currentDate.dayOfWeek.value - 1 // 0 = Monday
                val dateStr = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                val count = dataMap[dateStr]?.count ?: 0
                
                // Determine color based on count
                val cellColor = when {
                    count == 0 -> surfaceColor
                    count == 1 -> primaryColor.copy(alpha = 0.3f)
                    count == 2 -> primaryColor.copy(alpha = 0.5f)
                    count == 3 -> primaryColor.copy(alpha = 0.7f)
                    else -> primaryColor
                }
                
                val x = startX + weekIndex * (cellSize + gap)
                val y = startY + dayOfWeek * (cellSize + gap)
                
                drawRoundRect(
                    color = cellColor,
                    topLeft = Offset(x, y),
                    size = Size(cellSize, cellSize),
                    cornerRadius = CornerRadius(2.dp.toPx())
                )
                
                // Move to next day
                currentDate = currentDate.plusDays(1)
                
                // If we've completed a week (Sunday), move to next column
                if (currentDate.dayOfWeek.value == 1 && !currentDate.isAfter(today)) {
                    weekIndex++
                }
            }
        }
        
        // Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Less",
                style = MaterialTheme.typography.labelSmall,
                color = onSurfaceColor.copy(alpha = 0.6f)
            )
            
            listOf(0, 1, 2, 3, 4).forEach { level ->
                val color = when (level) {
                    0 -> surfaceColor
                    1 -> Color(0xFF10B981).copy(alpha = 0.3f)
                    2 -> Color(0xFF10B981).copy(alpha = 0.5f)
                    3 -> Color(0xFF10B981).copy(alpha = 0.7f)
                    else -> Color(0xFF10B981)
                }
                
                Canvas(modifier = Modifier.size(12.dp)) {
                    drawRoundRect(
                        color = color,
                        size = Size(size.width, size.height),
                        cornerRadius = CornerRadius(2.dp.toPx())
                    )
                }
            }
            
            Text(
                text = "More",
                style = MaterialTheme.typography.labelSmall,
                color = onSurfaceColor.copy(alpha = 0.6f)
            )
        }
    }
}

// Made with Bob