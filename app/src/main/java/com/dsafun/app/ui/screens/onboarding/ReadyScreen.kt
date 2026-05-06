package com.dsafun.app.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dsafun.app.ui.theme.Dimens

@Composable
fun ReadyScreen(
    userName: String,
    dailyGoal: Int,
    onGetStarted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.SpacingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Success Icon
        Text(
            text = "🎉",
            style = MaterialTheme.typography.displayLarge
        )
        
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        
        Text(
            text = "You're All Set!",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
        
        Text(
            text = "Welcome aboard, $userName!",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))
        
        // Summary Cards
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SpacingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your Daily Goal",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$dailyGoal",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(Dimens.SpacingSmall))
                    Text(
                        text = "problem${if (dailyGoal != 1) "s" else ""}/day",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        
        // Features List
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SpacingLarge),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
            ) {
                Text(
                    text = "What's Next?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                FeatureItem(
                    emoji = "📚",
                    text = "Browse 100+ DSA problems"
                )
                
                FeatureItem(
                    emoji = "💻",
                    text = "Code in multiple languages"
                )
                
                FeatureItem(
                    emoji = "🔥",
                    text = "Build your streak & earn XP"
                )
                
                FeatureItem(
                    emoji = "📊",
                    text = "Track your progress"
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Get Started Button
        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun FeatureItem(
    emoji: String,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Made with Bob