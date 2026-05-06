package com.dsafun.app.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * Motion specifications for consistent animations across the app
 */
object Motion {
    
    // Spring specifications
    val SpringDefault = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    val SpringSoft = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    val SpringStiff = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessHigh
    )
    
    // Tween specifications
    val TweenFast = tween<Float>(
        durationMillis = 150,
        easing = FastOutSlowInEasing
    )
    
    val TweenMedium = tween<Float>(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )
    
    val TweenSlow = tween<Float>(
        durationMillis = 500,
        easing = FastOutSlowInEasing
    )
    
    // Screen transition specs
    fun screenEnterTransition(): EnterTransition {
        return fadeIn(animationSpec = TweenFast) +
                slideInHorizontally(
                    initialOffsetX = { it / 4 },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
    }
    
    fun screenExitTransition(): ExitTransition {
        return fadeOut(animationSpec = TweenFast)
    }
    
    fun screenPopEnterTransition(): EnterTransition {
        return fadeIn(animationSpec = TweenFast)
    }
    
    fun screenPopExitTransition(): ExitTransition {
        return fadeOut(animationSpec = TweenFast) +
                slideOutHorizontally(
                    targetOffsetX = { it / 4 },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
    }
    
    // Bottom sheet transition
    fun bottomSheetEnterTransition(): EnterTransition {
        return fadeIn(animationSpec = TweenFast) +
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
    }
    
    fun bottomSheetExitTransition(): ExitTransition {
        return fadeOut(animationSpec = TweenFast) +
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
    }
    
    // Staggered list animation
    fun staggeredDelay(index: Int, baseDelayMs: Int = 30): Int {
        return index * baseDelayMs
    }
    
    // Scale animation for selections
    val ScaleIn = scaleIn(
        initialScale = 0.8f,
        animationSpec = TweenFast
    )
    
    val ScaleOut = scaleOut(
        targetScale = 0.8f,
        animationSpec = TweenFast
    )
    
    // Expand/Collapse animations
    fun expandVerticallyAnim(): EnterTransition {
        return androidx.compose.animation.expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            expandFrom = Alignment.Top
        ) + fadeIn(animationSpec = TweenFast)
    }
    
    fun shrinkVerticallyAnim(): ExitTransition {
        return androidx.compose.animation.shrinkVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            shrinkTowards = Alignment.Top
        ) + fadeOut(animationSpec = TweenFast)
    }
    
    // Slide offset for horizontal transitions
    val SlideOffset = 300.dp
}

// Made with Bob