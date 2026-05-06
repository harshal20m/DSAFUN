package com.dsafun.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dsafun.app.navigation.Screen
import com.dsafun.app.ui.components.BottomNavigationBar
import com.dsafun.app.ui.components.DsaNavigationRail
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import com.dsafun.app.ui.screens.AnalyticsScreen
import com.dsafun.app.ui.screens.HomeScreen
import com.dsafun.app.ui.screens.ProblemsScreen
import com.dsafun.app.ui.screens.SettingsScreen
import com.dsafun.app.ui.screens.codeeditor.CodeEditorScreen
import com.dsafun.app.ui.screens.onboarding.OnboardingFlow
import com.dsafun.app.ui.screens.problemdetail.ProblemDetailScreen
import com.dsafun.app.ui.screens.streak.StreakScreen
import com.dsafun.app.ui.screens.timer.FocusTimerScreen
import com.dsafun.app.ui.theme.DsaAppTheme
import com.dsafun.app.ui.theme.Motion
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var userPreferences: UserPreferencesDataStore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val appTheme by userPreferences.appTheme.collectAsState(initial = "DARK")
            val theme = when (appTheme) {
                "LIGHT" -> com.dsafun.app.ui.theme.AppTheme.LIGHT
                "MONOKAI" -> com.dsafun.app.ui.theme.AppTheme.MONOKAI
                "DRACULA" -> com.dsafun.app.ui.theme.AppTheme.DRACULA
                "NORD" -> com.dsafun.app.ui.theme.AppTheme.NORD
                else -> com.dsafun.app.ui.theme.AppTheme.DARK
            }
            
            DsaAppTheme(theme = theme) {
                MainApp(
                    userPreferences = userPreferences,
                    deepLinkProblemId = getDeepLinkProblemId(),
                    windowSizeClass = windowSizeClass
                )
            }
        }
    }
    
    private fun getDeepLinkProblemId(): Long? {
        return intent?.data?.let { uri ->
            if (uri.scheme == "problem" && uri.host == "id") {
                uri.lastPathSegment?.toLongOrNull()
            } else {
                null
            }
        }
    }
}

@Composable
fun MainApp(
    userPreferences: UserPreferencesDataStore,
    deepLinkProblemId: Long? = null,
    windowSizeClass: WindowSizeClass
) {
    val onboardingComplete by userPreferences.onboardingComplete.collectAsState(initial = null)
    
    // Show loading while checking onboarding status
    if (onboardingComplete == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    
    // Show onboarding or main app based on status
    if (onboardingComplete == false) {
        OnboardingFlow(
            onComplete = {
                // Onboarding completion is handled by the ViewModel
                // The Flow will automatically update and show the main app
            }
        )
    } else {
        MainAppContent(
            deepLinkProblemId = deepLinkProblemId,
            windowSizeClass = windowSizeClass
        )
    }
}

@Composable
fun MainAppContent(
    deepLinkProblemId: Long? = null,
    windowSizeClass: WindowSizeClass
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Determine if we should use NavigationRail (tablets) or BottomNav (phones)
    val useNavigationRail = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    // Handle deep link navigation
    androidx.compose.runtime.LaunchedEffect(deepLinkProblemId) {
        deepLinkProblemId?.let { problemId ->
            navController.navigate(Screen.ProblemDetail.createRoute(problemId.toInt())) {
                launchSingleTop = true
            }
        }
    }

    // Adaptive Scaffold: NavigationRail for tablets, BottomNav for phones
    if (useNavigationRail) {
        Row(modifier = Modifier.fillMaxSize()) {
            DsaNavigationRail(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                showLabels = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
            )
            NavigationGraph(
                navController = navController,
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavigationGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = { Motion.screenEnterTransition() },
        exitTransition = { Motion.screenExitTransition() },
        popEnterTransition = { Motion.screenPopEnterTransition() },
        popExitTransition = { Motion.screenPopExitTransition() }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProblem = { problemId ->
                    navController.navigate(Screen.ProblemDetail.createRoute(problemId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        composable(Screen.Problems.route) {
            ProblemsScreen(
                onProblemClick = { problemId ->
                    navController.navigate(Screen.ProblemDetail.createRoute(problemId))
                }
            )
        }
        composable(
            route = Screen.ProblemDetail.route,
            arguments = listOf(
                navArgument("problemId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getInt("problemId") ?: 0
            ProblemDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenEditor = { navController.navigate(Screen.CodeEditor.createRoute(problemId)) }
            )
        }
        composable(
            route = Screen.CodeEditor.route,
            arguments = listOf(
                navArgument("problemId") { type = NavType.IntType }
            )
        ) {
            CodeEditorScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTimer = { navController.navigate(Screen.Timer.route) }
            )
        }
        composable(Screen.Streak.route) {
            StreakScreen()
        }
        composable(Screen.Timer.route) {
            FocusTimerScreen()
        }
        composable(Screen.Analytics.route) {
            AnalyticsScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}

// Made with Bob
