package com.dsafun.app.ui.screens.onboarding

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingFlow(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var currentScreen by remember { mutableIntStateOf(0) }
    var userName by remember { mutableStateOf("") }
    var dailyGoal by remember { mutableIntStateOf(3) }
    
    when (currentScreen) {
        0 -> {
            WelcomeScreen(
                onContinue = { name, goal ->
                    userName = name
                    dailyGoal = goal
                    currentScreen = 1
                }
            )
        }
        1 -> {
            ReadyScreen(
                userName = userName,
                dailyGoal = dailyGoal,
                onGetStarted = {
                    viewModel.completeOnboarding(userName, dailyGoal)
                    onComplete()
                }
            )
        }
    }
}

// Made with Bob