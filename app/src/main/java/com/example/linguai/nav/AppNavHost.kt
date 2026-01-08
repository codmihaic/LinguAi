package com.example.linguai.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.linguai.ui.auth.AuthScreen
import com.example.linguai.ui.history.HistoryScreen
import com.example.linguai.ui.home.HomeScreen
import com.example.linguai.ui.image.ImageOcrScreen
import com.example.linguai.ui.settings.SettingsScreen
import com.example.linguai.ui.splash.SplashScreen
import com.example.linguai.ui.text.TextTranslateScreen
import com.example.linguai.ui.voice.VoiceScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route)  { SplashScreen(navController) }

        composable(Screen.Home.route)     { HomeScreen(navController) }
        composable(Screen.Text.route)     { TextTranslateScreen(navController) }
        composable(Screen.Voice.route)    { VoiceScreen(navController) }
        composable(Screen.Image.route)    { ImageOcrScreen(navController) }
        composable(Screen.History.route)  { HistoryScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
        composable(Screen.Auth.route)     { AuthScreen(navController) }
    }
}
