package com.example.linguai.ui

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.linguai.model.Language
import com.example.linguai.nav.AppNavHost

@Composable
fun LinguAIApp() {
    val navController = rememberNavController()
    var appLanguage by remember { mutableStateOf(Language.uiDefault()) }

    var darkTheme by remember { mutableStateOf(true) }

    CompositionLocalProvider(
        LocalAppLanguageState provides AppLanguageState(
            current = appLanguage,
            setLanguage = { appLanguage = it }
        ),
        LocalAppThemeState provides AppThemeState(
            darkTheme = darkTheme,
            setDarkTheme = { darkTheme = it }
        )
    ) {
        // Apply theme based on our provided darkTheme state
        com.example.linguai.ui.theme.LinguAITheme(darkTheme = darkTheme) {
            AppNavHost(navController = navController)
        }
    }
}
