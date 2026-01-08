package com.example.linguai.ui

import androidx.compose.runtime.compositionLocalOf

data class AppThemeState(
    val darkTheme: Boolean,
    val setDarkTheme: (Boolean) -> Unit
)

val LocalAppThemeState = compositionLocalOf<AppThemeState> {
    error("LocalAppThemeState is not provided")
}