package com.dsafun.app.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dsafun.app.ui.theme.Dimens

@Composable
fun WelcomeScreen(
    onContinue: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dailyGoal by remember { mutableIntStateOf(3) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.SpacingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Welcome Header
        Text(
            text = "👋",
            style = MaterialTheme.typography.displayLarge
        )
        
        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        
        Text(
            text = "Welcome to DSA Fun!",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
        
        Text(
            text = "Let's get you started on your coding journey",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Dimens.SpacingXLarge))
        
        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("What's your name?") },
            placeholder = { Text("Enter your name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        
        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
        
        // Daily Goal Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Daily Goal",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
            
            Text(
                text = "How many problems do you want to solve daily?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            
            // Goal Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.SpacingMedium),
                    horizontalArrangement = Arrangement.Center,
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
            
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            
            // Slider
            Slider(
                value = dailyGoal.toFloat(),
                onValueChange = { dailyGoal = it.toInt() },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "1",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
                Text(
                    text = "10",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Continue Button
        Button(
            onClick = { 
                if (name.isNotBlank()) {
                    onContinue(name.trim(), dailyGoal)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = name.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = "Continue",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

// Made with Bob