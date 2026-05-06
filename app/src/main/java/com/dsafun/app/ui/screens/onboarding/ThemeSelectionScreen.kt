package com.dsafun.app.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dsafun.app.ui.theme.AppTheme
import com.dsafun.app.ui.theme.DsaAppTheme
import kotlinx.coroutines.delay

data class AppThemeOption(
    val name: String,
    val displayName: String,
    val emoji: String,
    val description: String
)

@Composable
fun ThemeSelectionScreen(
    onContinue: (String) -> Unit
) {
    var selectedTheme by remember { mutableStateOf("SYSTEM") }
    var isVisible by remember { mutableStateOf(false) }
    
    val themes = remember {
        listOf(
            AppThemeOption("SYSTEM", "System Default", "🔄", "Follow device"),
            AppThemeOption("LIGHT", "Light", "☀️", "Bright & clean"),
            AppThemeOption("DARK", "Dark", "🌙", "Easy on eyes"),
            AppThemeOption("MONOKAI", "Monokai", "🎨", "Code editor"),
            AppThemeOption("DRACULA", "Dracula", "🧛", "Dark purple"),
            AppThemeOption("NORD", "Nord", "❄️", "Cool blue")
        )
    }
    
    // Convert selected theme string to AppTheme enum
    val currentTheme = remember(selectedTheme) {
        when (selectedTheme) {
            "SYSTEM" -> AppTheme.SYSTEM
            "LIGHT" -> AppTheme.LIGHT
            "DARK" -> AppTheme.DARK
            "MONOKAI" -> AppTheme.MONOKAI
            "DRACULA" -> AppTheme.DRACULA
            "NORD" -> AppTheme.NORD
            else -> AppTheme.SYSTEM
        }
    }
    
    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }
    
    // Apply the selected theme to the entire screen for live preview
    DsaAppTheme(theme = currentTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ThemeSelectionContent(
                themes = themes,
                selectedTheme = selectedTheme,
                isVisible = isVisible,
                onThemeSelected = { selectedTheme = it },
                onContinue = { onContinue(selectedTheme) }
            )
        }
    }
}

@Composable
private fun ThemeSelectionContent(
    themes: List<AppThemeOption>,
    selectedTheme: String,
    isVisible: Boolean,
    onThemeSelected: (String) -> Unit,
    onContinue: () -> Unit
) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Header
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInVertically { -it }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎨",
                        style = MaterialTheme.typography.displayMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Pick Your Theme",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Choose how the app looks",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Theme Grid
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(600, delayMillis = 300))
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(themes) { theme ->
                        ThemeCard(
                            theme = theme,
                            isSelected = selectedTheme == theme.name,
                            onClick = { onThemeSelected(theme.name) }
                        )
                    }
                }
            }
            
            // Continue Button
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(600, delayMillis = 600))
            ) {
                Column {
                    Button(
                        onClick = onContinue,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Continue",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Step 3 of 4 • You can change this later",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeCard(
    theme: AppThemeOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isSelected)
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 3.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = theme.emoji,
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = theme.displayName,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Text(
                text = theme.description,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

// Made with Bob