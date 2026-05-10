package com.dsafun.app

import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dsafun.app.data.repository.PreferencesRepository
import com.dsafun.app.domain.model.TimerState
import com.dsafun.app.navigation.Screen
import com.dsafun.app.ui.components.BottomNavigationBar
import com.dsafun.app.ui.components.DsaNavigationRail
import com.dsafun.app.ui.components.FloatingTimerWidget
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dsafun.app.data.local.datastore.UserPreferencesDataStore
import com.dsafun.app.ui.screens.AnalyticsScreen
import com.dsafun.app.ui.screens.ApnaCollegeScreen
import com.dsafun.app.ui.screens.FrazScreen
import com.dsafun.app.ui.screens.HomeScreen
import com.dsafun.app.ui.screens.LoveBabbarScreen
import com.dsafun.app.ui.screens.LeetCodeScreen
import com.dsafun.app.ui.screens.ProblemsScreen
import com.dsafun.app.ui.screens.SettingsScreen
import com.dsafun.app.ui.screens.codeeditor.CodeEditorScreen
import com.dsafun.app.ui.screens.onboarding.OnboardingFlow
import com.dsafun.app.ui.screens.problemdetail.ProblemDetailScreen
import com.dsafun.app.ui.screens.streak.StreakScreen
import com.dsafun.app.ui.screens.timer.FocusTimerScreen
import com.dsafun.app.ui.screens.timer.FocusTimerViewModel
import com.dsafun.app.ui.theme.DsaAppTheme
import com.dsafun.app.ui.theme.Motion
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
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
            val appTheme by userPreferences.appTheme.collectAsState(initial = "SYSTEM")
            val theme = when (appTheme) {
                "SYSTEM" -> com.dsafun.app.ui.theme.AppTheme.SYSTEM
                "DARK" -> com.dsafun.app.ui.theme.AppTheme.DARK
                "MONOKAI" -> com.dsafun.app.ui.theme.AppTheme.MONOKAI
                "DRACULA" -> com.dsafun.app.ui.theme.AppTheme.DRACULA
                "NORD" -> com.dsafun.app.ui.theme.AppTheme.NORD
                else -> com.dsafun.app.ui.theme.AppTheme.LIGHT
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
    windowSizeClass: WindowSizeClass,
    preferencesRepository: PreferencesRepository = hiltViewModel<com.dsafun.app.ui.viewmodels.SettingsViewModel>().preferencesRepository
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Get FocusTimerViewModel scoped to the activity to share state globally
    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? ComponentActivity
    val timerViewModel: FocusTimerViewModel = hiltViewModel(
        viewModelStoreOwner = activity ?: context as ComponentActivity
    )
    val timerUiState by timerViewModel.uiState.collectAsState()
    
    // Milestone congratulations dialog state
    val totalProblemsSolved by preferencesRepository.totalProblemsSolved.collectAsState(initial = 0)
    val lastMilestoneShown by preferencesRepository.lastMilestoneShown.collectAsState(initial = 0)
    var showMilestoneDialog by remember { mutableStateOf(false) }
    var currentMilestone by remember { mutableStateOf(0) }
    
    // Check for milestone
    LaunchedEffect(totalProblemsSolved) {
        if (totalProblemsSolved > 0) {
            val milestone = if (totalProblemsSolved == 1) 1
                           else if (totalProblemsSolved % 30 == 0) totalProblemsSolved
                           else 0
            
            if (milestone > 0 && milestone > lastMilestoneShown) {
                currentMilestone = milestone
                showMilestoneDialog = true
                preferencesRepository.setLastMilestoneShown(milestone)
            }
        }
    }
    
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

    Box(modifier = Modifier.fillMaxSize()) {
        // Adaptive Scaffold: NavigationRail for tablets, BottomNav for phones
        if (useNavigationRail) {
            Row(modifier = Modifier.fillMaxSize()) {
                DsaNavigationRail(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Pop everything up to the start destination
                            popUpTo(Screen.Home.route) {
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
                    timerViewModel = timerViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    val hideBottomNavigation = currentRoute?.startsWith("problem_detail/") == true ||
                        currentRoute?.startsWith("code_editor/") == true ||
                        currentRoute == Screen.LeetCode.route ||
                        currentRoute == Screen.ApnaCollege.route ||
                        currentRoute == Screen.Fraz.route ||
                        currentRoute == Screen.LoveBabbar.route ||
                        currentRoute == Screen.CommonProblems.route

                    if (!hideBottomNavigation) {
                        // Bottom bar with floating timer widget on top
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Floating Timer Widget - Positioned above bottom nav
                            if (currentRoute != Screen.Timer.route) {
                                FloatingTimerWidget(
                                    timerSeconds = timerUiState.remainingSeconds,  // Show remaining time (countdown)
                                    isVisible = timerUiState.isFloatingWidgetVisible &&
                                               (timerUiState.timerState == TimerState.RUNNING || timerUiState.timerState == TimerState.PAUSED),
                                    onDismiss = { timerViewModel.hideFloatingWidget() },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            BottomNavigationBar(
                                currentRoute = currentRoute,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        // Pop everything up to the start destination
                                        popUpTo(Screen.Home.route) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                NavigationGraph(
                    navController = navController,
                    timerViewModel = timerViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
    
    // Milestone Congratulations Dialog - App-wide
    if (showMilestoneDialog) {
        val context = LocalContext.current
        val milestoneMessage = when (currentMilestone) {
            1 -> "You've solved your first problem!"
            30 -> "Amazing! You've solved 30 problems!"
            60 -> "Incredible! You've solved 60 problems!"
            90 -> "Outstanding! You've solved 90 problems!"
            else -> "Fantastic! You've solved $currentMilestone problems!"
        }
        
        AlertDialog(
            onDismissRequest = { showMilestoneDialog = false },
            title = {
                Text(
                    text = "🎉 Congratulations! 🎉",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = milestoneMessage,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Hope you enjoy the app! It takes a lot of effort to build and maintain.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Please give us a ⭐ on GitHub and keep coding!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showMilestoneDialog = false }) {
                    Text("Keep Coding! 💪")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/harshal20m/DSAFUN"))
                        context.startActivity(intent)
                        showMilestoneDialog = false
                    }
                ) {
                    Text("⭐ Star on GitHub")
                }
            }
        )
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    timerViewModel: FocusTimerViewModel,
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
                },
                onNavigateToProblems = {
                    navController.navigate(Screen.Problems.route)
                },
                onNavigateToTimer = {
                    navController.navigate(Screen.Timer.route)
                },
                onNavigateToAnalytics = {
                    navController.navigate(Screen.Analytics.route)
                },
                onNavigateToStreak = {
                    navController.navigate(Screen.Streak.route)
                }
            )
        }
        composable(Screen.Problems.route) {
            ProblemsScreen(
                onProblemClick = { problemId ->
                    navController.navigate(Screen.ProblemDetail.createRoute(problemId))
                },
                onLeetCodeClick = {
                    navController.navigate(Screen.LeetCode.route)
                },
                onCommonProblemsClick = {
                    navController.navigate(Screen.CommonProblems.route)
                },
                onApnaCollegeClick = {
                    navController.navigate(Screen.ApnaCollege.route)
                },
                onFrazClick = {
                    navController.navigate(Screen.Fraz.route)
                },
                onLoveBabbarClick = {
                    navController.navigate(Screen.LoveBabbar.route)
                },
                showCollectionsOnly = true
            )
        }
        composable(Screen.CommonProblems.route) {
            ProblemsScreen(
                onProblemClick = { problemId ->
                    navController.navigate(Screen.ProblemDetail.createRoute(problemId))
                },
                onLeetCodeClick = {
                    navController.navigate(Screen.LeetCode.route)
                },
                onCommonProblemsClick = {},
                onApnaCollegeClick = {
                    navController.navigate(Screen.ApnaCollege.route)
                },
                onFrazClick = {
                    navController.navigate(Screen.Fraz.route)
                },
                onLoveBabbarClick = {
                    navController.navigate(Screen.LoveBabbar.route)
                },
                showCollectionsOnly = false
            )
        }
        composable(Screen.LeetCode.route) {
            LeetCodeScreen()
        }
        composable(Screen.ApnaCollege.route) {
            ApnaCollegeScreen()
        }
        composable(Screen.Fraz.route) {
            FrazScreen()
        }
        composable(Screen.LoveBabbar.route) {
            LoveBabbarScreen(
                onNavigateBack = { navController.popBackStack() }
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
            FocusTimerScreen(viewModel = timerViewModel)
        }
        composable(Screen.Analytics.route) {
            AnalyticsScreen(
                onNavigateToStreak = {
                    navController.navigate(Screen.Streak.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}

// Made with Bob
