package com.dsafun.app.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingFlow(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var currentScreen by remember { mutableIntStateOf(0) }
    var userName by remember { mutableStateOf("") }
    var preferredLanguage by remember { mutableStateOf("PYTHON") }
    var selectedTheme by remember { mutableStateOf("SYSTEM") }
    var dailyGoal by remember { mutableIntStateOf(3) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    
    // Animated content with slide transitions
    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            if (targetState > initialState) {
                // Forward navigation
                slideInHorizontally(
                    animationSpec = tween(400),
                    initialOffsetX = { it }
                ) + fadeIn(animationSpec = tween(400)) togetherWith
                slideOutHorizontally(
                    animationSpec = tween(400),
                    targetOffsetX = { -it }
                ) + fadeOut(animationSpec = tween(400))
            } else {
                // Backward navigation
                slideInHorizontally(
                    animationSpec = tween(400),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(400)) togetherWith
                slideOutHorizontally(
                    animationSpec = tween(400),
                    targetOffsetX = { it }
                ) + fadeOut(animationSpec = tween(400))
            }
        },
        label = "onboarding_transition"
    ) { screen ->
        when (screen) {
            0 -> {
                WelcomeScreen(
                    onContinue = { name ->
                        userName = name
                        currentScreen = 1
                    }
                )
            }
            1 -> {
                LanguageSelectionScreen(
                    onContinue = { language ->
                        preferredLanguage = language
                        currentScreen = 2
                    }
                )
            }
            2 -> {
                ThemeSelectionScreen(
                    onContinue = { theme ->
                        selectedTheme = theme
                        currentScreen = 3
                    }
                )
            }
            3 -> {
                GoalAndNotificationsScreen(
                    userName = userName,
                    onComplete = { goal, notifications ->
                        dailyGoal = goal
                        notificationsEnabled = notifications
                        viewModel.completeOnboarding(
                            userName = userName,
                            preferredLanguage = preferredLanguage,
                            theme = selectedTheme,
                            dailyGoal = dailyGoal,
                            notificationsEnabled = notificationsEnabled
                        )
                        onComplete()
                    }
                )
            }
        }
    }
}

// Made with Bob