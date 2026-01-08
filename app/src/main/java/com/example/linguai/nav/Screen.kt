package com.example.linguai.nav

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Text : Screen("text")
    data object Voice : Screen("voice")
    data object Image : Screen("image")
    data object History : Screen("history")
    data object Settings : Screen("settings")
    data object Auth : Screen("auth")
}
