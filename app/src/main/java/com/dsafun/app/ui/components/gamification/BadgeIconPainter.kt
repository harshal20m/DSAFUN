package com.dsafun.app.ui.components.gamification

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BadgeIcon(
    iconType: String,
    isLocked: Boolean,
    modifier: Modifier = Modifier,
    size: Float = 48f
) {
    val color = if (isLocked) Color.Gray.copy(alpha = 0.4f) else getIconColor(iconType)
    
    Canvas(modifier = modifier.size(size.dp)) {
        when (iconType) {
            "FLAME" -> drawFlame(color, isLocked)
            "TROPHY" -> drawTrophy(color, isLocked)
            "CLOCK" -> drawClock(color, isLocked)
            "STAR" -> drawStar(color, isLocked)
            "OWL" -> drawOwl(color, isLocked)
            "GLOBE" -> drawGlobe(color, isLocked)
            "LIGHTNING" -> drawLightning(color, isLocked)
            else -> drawStar(color, isLocked)
        }
        
        // Draw lock overlay if locked
        if (isLocked) {
            drawLockOverlay()
        }
    }
}

private fun getIconColor(iconType: String): Color {
    return when (iconType) {
        "FLAME" -> Color(0xFFFF6B35) // Orange-red
        "TROPHY" -> Color(0xFFFFD700) // Gold
        "CLOCK" -> Color(0xFF3B82F6) // Blue
        "STAR" -> Color(0xFFFBBF24) // Yellow
        "OWL" -> Color(0xFF8B5CF6) // Purple
        "GLOBE" -> Color(0xFF10B981) // Emerald
        "LIGHTNING" -> Color(0xFFF59E0B) // Amber
        else -> Color(0xFFFBBF24)
    }
}

private fun DrawScope.drawFlame(color: Color, isLocked: Boolean) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    
    val flamePath = Path().apply {
        moveTo(centerX, centerY + size.height * 0.3f)
        cubicTo(
            centerX - size.width * 0.2f, centerY + size.height * 0.1f,
            centerX - size.width * 0.3f, centerY - size.height * 0.1f,
            centerX, centerY - size.height * 0.4f
        )
        cubicTo(
            centerX + size.width * 0.3f, centerY - size.height * 0.1f,
            centerX + size.width * 0.2f, centerY + size.height * 0.1f,
            centerX, centerY + size.height * 0.3f
        )
        close()
    }
    
    drawPath(flamePath, color)
    
    // Inner flame
    val innerFlamePath = Path().apply {
        moveTo(centerX, centerY + size.height * 0.15f)
        cubicTo(
            centerX - size.width * 0.1f, centerY,
            centerX - size.width * 0.15f, centerY - size.height * 0.15f,
            centerX, centerY - size.height * 0.25f
        )
        cubicTo(
            centerX + size.width * 0.15f, centerY - size.height * 0.15f,
            centerX + size.width * 0.1f, centerY,
            centerX, centerY + size.height * 0.15f
        )
        close()
    }
    
    drawPath(innerFlamePath, Color.Yellow.copy(alpha = if (isLocked) 0.3f else 0.8f))
}

private fun DrawScope.drawTrophy(color: Color, isLocked: Boolean) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    
    // Cup body
    val cupPath = Path().apply {
        moveTo(centerX - size.width * 0.25f, centerY - size.height * 0.2f)
        lineTo(centerX - size.width * 0.2f, centerY + size.height * 0.15f)
        lineTo(centerX + size.width * 0.2f, centerY + size.height * 0.15f)
        lineTo(centerX + size.width * 0.25f, centerY - size.height * 0.2f)
        close()
    }
    drawPath(cupPath, color)
    
    // Handles
    drawArc(
        color = color,
        startAngle = 90f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(centerX - size.width * 0.45f, centerY - size.height * 0.15f),
        size = Size(size.width * 0.2f, size.height * 0.2f),
        style = Stroke(width = 3.dp.toPx())
    )
    
    drawArc(
        color = color,
        startAngle = 270f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(centerX + size.width * 0.25f, centerY - size.height * 0.15f),
        size = Size(size.width * 0.2f, size.height * 0.2f),
        style = Stroke(width = 3.dp.toPx())
    )
    
    // Base
    drawRect(
        color = color,
        topLeft = Offset(centerX - size.width * 0.15f, centerY + size.height * 0.15f),
        size = Size(size.width * 0.3f, size.height * 0.05f)
    )
    
    drawRect(
        color = color,
        topLeft = Offset(centerX - size.width * 0.2f, centerY + size.height * 0.2f),
        size = Size(size.width * 0.4f, size.height * 0.08f)
    )
}

private fun DrawScope.drawClock(color: Color, isLocked: Boolean) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension * 0.35f
    
    // Clock face
    drawCircle(
        color = color,
        radius = radius,
        center = Offset(centerX, centerY),
        style = Stroke(width = 3.dp.toPx())
    )
    
    // Hour hand
    drawLine(
        color = color,
        start = Offset(centerX, centerY),
        end = Offset(centerX, centerY - radius * 0.5f),
        strokeWidth = 3.dp.toPx()
    )
    
    // Minute hand
    drawLine(
        color = color,
        start = Offset(centerX, centerY),
        end = Offset(centerX + radius * 0.6f, centerY),
        strokeWidth = 2.dp.toPx()
    )
    
    // Center dot
    drawCircle(
        color = color,
        radius = 3.dp.toPx(),
        center = Offset(centerX, centerY)
    )
}

private fun DrawScope.drawStar(color: Color, isLocked: Boolean) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val outerRadius = size.minDimension * 0.4f
    val innerRadius = outerRadius * 0.4f
    val points = 5
    
    val starPath = Path().apply {
        for (i in 0 until points * 2) {
            val radius = if (i % 2 == 0) outerRadius else innerRadius
            val angle = (i * PI / points - PI / 2).toFloat()
            val x = centerX + radius * cos(angle)
            val y = centerY + radius * sin(angle)
            
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
    
    drawPath(starPath, color)
}

private fun DrawScope.drawOwl(color: Color, isLocked: Boolean) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    
    // Head
    drawCircle(
        color = color,
        radius = size.minDimension * 0.35f,
        center = Offset(centerX, centerY)
    )
    
    // Eyes
    drawCircle(
        color = Color.White,
        radius = size.minDimension * 0.12f,
        center = Offset(centerX - size.width * 0.15f, centerY - size.height * 0.05f)
    )
    
    drawCircle(
        color = Color.White,
        radius = size.minDimension * 0.12f,
        center = Offset(centerX + size.width * 0.15f, centerY - size.height * 0.05f)
    )
    
    // Pupils
    drawCircle(
        color = Color.Black,
        radius = size.minDimension * 0.06f,
        center = Offset(centerX - size.width * 0.15f, centerY - size.height * 0.05f)
    )
    
    drawCircle(
        color = Color.Black,
        radius = size.minDimension * 0.06f,
        center = Offset(centerX + size.width * 0.15f, centerY - size.height * 0.05f)
    )
    
    // Beak
    val beakPath = Path().apply {
        moveTo(centerX, centerY + size.height * 0.05f)
        lineTo(centerX - size.width * 0.08f, centerY + size.height * 0.15f)
        lineTo(centerX + size.width * 0.08f, centerY + size.height * 0.15f)
        close()
    }
    drawPath(beakPath, Color(0xFFFFA500))
}

private fun DrawScope.drawGlobe(color: Color, isLocked: Boolean) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension * 0.35f
    
    // Circle
    drawCircle(
        color = color,
        radius = radius,
        center = Offset(centerX, centerY),
        style = Stroke(width = 3.dp.toPx())
    )
    
    // Vertical line
    drawLine(
        color = color,
        start = Offset(centerX, centerY - radius),
        end = Offset(centerX, centerY + radius),
        strokeWidth = 2.dp.toPx()
    )
    
    // Horizontal lines
    drawLine(
        color = color,
        start = Offset(centerX - radius, centerY),
        end = Offset(centerX + radius, centerY),
        strokeWidth = 2.dp.toPx()
    )
    
    // Curved lines
    drawArc(
        color = color,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(centerX - radius * 0.5f, centerY - radius),
        size = Size(radius, radius * 2),
        style = Stroke(width = 2.dp.toPx())
    )
    
    drawArc(
        color = color,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(centerX - radius * 0.5f, centerY - radius),
        size = Size(radius, radius * 2),
        style = Stroke(width = 2.dp.toPx())
    )
}

private fun DrawScope.drawLightning(color: Color, isLocked: Boolean) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    
    val lightningPath = Path().apply {
        moveTo(centerX - size.width * 0.1f, centerY - size.height * 0.35f)
        lineTo(centerX + size.width * 0.15f, centerY - size.height * 0.05f)
        lineTo(centerX - size.width * 0.05f, centerY - size.height * 0.05f)
        lineTo(centerX + size.width * 0.1f, centerY + size.height * 0.35f)
        lineTo(centerX - size.width * 0.15f, centerY + size.height * 0.05f)
        lineTo(centerX + size.width * 0.05f, centerY + size.height * 0.05f)
        close()
    }
    
    drawPath(lightningPath, color)
}

private fun DrawScope.drawLockOverlay() {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val lockSize = size.minDimension * 0.25f
    
    // Lock body
    drawRect(
        color = Color.Black.copy(alpha = 0.6f),
        topLeft = Offset(centerX - lockSize * 0.4f, centerY - lockSize * 0.1f),
        size = Size(lockSize * 0.8f, lockSize * 0.6f)
    )
    
    // Lock shackle
    drawArc(
        color = Color.Black.copy(alpha = 0.6f),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(centerX - lockSize * 0.3f, centerY - lockSize * 0.5f),
        size = Size(lockSize * 0.6f, lockSize * 0.5f),
        style = Stroke(width = 3.dp.toPx())
    )
}

// Made with Bob