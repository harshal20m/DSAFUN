package com.dsafun.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class AppTheme {
    SYSTEM, LIGHT, DARK, MONOKAI, DRACULA, NORD
}

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnBackground,
    primaryContainer = DarkPrimaryVariant,
    onPrimaryContainer = DarkOnBackground,
    secondary = DarkSecondary,
    onSecondary = DarkOnBackground,
    secondaryContainer = DarkSecondary,
    onSecondaryContainer = DarkOnBackground,
    tertiary = DarkTertiary,
    onTertiary = DarkOnBackground,
    tertiaryContainer = DarkTertiary,
    onTertiaryContainer = DarkOnBackground,
    error = DarkError,
    onError = DarkOnBackground,
    errorContainer = DarkError,
    onErrorContainer = DarkOnBackground,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkMuted,
    outline = DarkMuted,
    outlineVariant = DarkSurfaceVariant,
    scrim = DarkBackground,
    inverseSurface = DarkOnSurface,
    inverseOnSurface = DarkSurface,
    inversePrimary = DarkPrimaryVariant,
    surfaceTint = DarkPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = LightPrimaryVariant,
    onPrimaryContainer = LightOnBackground,
    secondary = LightSecondary,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = LightSecondary,
    onSecondaryContainer = LightOnBackground,
    tertiary = LightTertiary,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = LightTertiary,
    onTertiaryContainer = LightOnBackground,
    error = LightError,
    onError = Color(0xFFFFFFFF),
    errorContainer = LightError,
    onErrorContainer = LightOnBackground,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightMuted,
    outline = LightMuted,
    outlineVariant = LightSurfaceVariant,
    scrim = LightOnBackground,
    inverseSurface = LightOnSurface,
    inverseOnSurface = LightSurface,
    inversePrimary = LightPrimaryVariant,
    surfaceTint = LightPrimary
)

private val MonokaiColorScheme = darkColorScheme(
    primary = MonokaiPrimary,
    onPrimary = MonokaiOnBackground,
    primaryContainer = MonokaiPrimaryVariant,
    onPrimaryContainer = MonokaiOnBackground,
    secondary = MonokaiSecondary,
    onSecondary = MonokaiOnBackground,
    secondaryContainer = MonokaiSecondary,
    onSecondaryContainer = MonokaiOnBackground,
    tertiary = MonokaiTertiary,
    onTertiary = MonokaiOnBackground,
    tertiaryContainer = MonokaiTertiary,
    onTertiaryContainer = MonokaiOnBackground,
    error = MonokaiError,
    onError = MonokaiOnBackground,
    errorContainer = MonokaiError,
    onErrorContainer = MonokaiOnBackground,
    background = MonokaiBackground,
    onBackground = MonokaiOnBackground,
    surface = MonokaiSurface,
    onSurface = MonokaiOnSurface,
    surfaceVariant = MonokaiSurfaceVariant,
    onSurfaceVariant = MonokaiMuted,
    outline = MonokaiMuted,
    outlineVariant = MonokaiSurfaceVariant,
    scrim = MonokaiBackground,
    inverseSurface = MonokaiOnSurface,
    inverseOnSurface = MonokaiSurface,
    inversePrimary = MonokaiPrimaryVariant,
    surfaceTint = MonokaiPrimary
)

private val DraculaColorScheme = darkColorScheme(
    primary = DraculaPrimary,
    onPrimary = DraculaOnBackground,
    primaryContainer = DraculaPrimaryVariant,
    onPrimaryContainer = DraculaOnBackground,
    secondary = DraculaSecondary,
    onSecondary = DraculaOnBackground,
    secondaryContainer = DraculaSecondary,
    onSecondaryContainer = DraculaOnBackground,
    tertiary = DraculaTertiary,
    onTertiary = DraculaOnBackground,
    tertiaryContainer = DraculaTertiary,
    onTertiaryContainer = DraculaOnBackground,
    error = DraculaError,
    onError = DraculaOnBackground,
    errorContainer = DraculaError,
    onErrorContainer = DraculaOnBackground,
    background = DraculaBackground,
    onBackground = DraculaOnBackground,
    surface = DraculaSurface,
    onSurface = DraculaOnSurface,
    surfaceVariant = DraculaSurfaceVariant,
    onSurfaceVariant = DraculaMuted,
    outline = DraculaMuted,
    outlineVariant = DraculaSurfaceVariant,
    scrim = DraculaBackground,
    inverseSurface = DraculaOnSurface,
    inverseOnSurface = DraculaSurface,
    inversePrimary = DraculaPrimaryVariant,
    surfaceTint = DraculaPrimary
)

private val NordColorScheme = darkColorScheme(
    primary = NordPrimary,
    onPrimary = NordOnBackground,
    primaryContainer = NordPrimaryVariant,
    onPrimaryContainer = NordOnBackground,
    secondary = NordSecondary,
    onSecondary = NordOnBackground,
    secondaryContainer = NordSecondary,
    onSecondaryContainer = NordOnBackground,
    tertiary = NordTertiary,
    onTertiary = NordOnBackground,
    tertiaryContainer = NordTertiary,
    onTertiaryContainer = NordOnBackground,
    error = NordError,
    onError = NordOnBackground,
    errorContainer = NordError,
    onErrorContainer = NordOnBackground,
    background = NordBackground,
    onBackground = NordOnBackground,
    surface = NordSurface,
    onSurface = NordOnSurface,
    surfaceVariant = NordSurfaceVariant,
    onSurfaceVariant = NordMuted,
    outline = NordMuted,
    outlineVariant = NordSurfaceVariant,
    scrim = NordBackground,
    inverseSurface = NordOnSurface,
    inverseOnSurface = NordSurface,
    inversePrimary = NordPrimaryVariant,
    surfaceTint = NordPrimary
)

fun getColorScheme(theme: AppTheme): ColorScheme {
    return when (theme) {
        AppTheme.SYSTEM -> LightColorScheme // Will be resolved in DsaAppTheme
        AppTheme.LIGHT -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.MONOKAI -> MonokaiColorScheme
        AppTheme.DRACULA -> DraculaColorScheme
        AppTheme.NORD -> NordColorScheme
    }
}

@Composable
fun DsaAppTheme(
    theme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemInDarkTheme = isSystemInDarkTheme()
    
    // Determine actual theme based on SYSTEM setting
    val actualTheme = if (theme == AppTheme.SYSTEM) {
        if (systemInDarkTheme) AppTheme.DARK else AppTheme.LIGHT
    } else {
        theme
    }
    
    val colorScheme = getColorScheme(actualTheme)
    val view = LocalView.current
    val isLight = actualTheme == AppTheme.LIGHT
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = isLight
                isAppearanceLightNavigationBars = isLight
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

// Made with Bob
