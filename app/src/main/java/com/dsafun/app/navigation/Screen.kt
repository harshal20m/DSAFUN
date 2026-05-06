package com.dsafun.app.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Problems : Screen("problems")
    object Streak : Screen("streak")
    object Timer : Screen("timer")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
    object ProblemDetail : Screen("problem_detail/{problemId}") {
        fun createRoute(problemId: Int) = "problem_detail/$problemId"
    }
    object CodeEditor : Screen("code_editor/{problemId}") {
        fun createRoute(problemId: Int) = "code_editor/$problemId"
    }
}

// Made with Bob
