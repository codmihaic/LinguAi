package com.example.linguai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BrandPurple,
    secondary = BrandPurpleDark,
    tertiary = BrandPurple,

    background = androidx.compose.ui.graphics.Color.Transparent,
    surface = androidx.compose.ui.graphics.Color.Transparent,

    onBackground = OffWhite,
    onSurface = OffWhite,
    onPrimary = OffWhite,
    onSecondary = OffWhite,
    onTertiary = OffWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = BrandPurple,
    secondary = BrandPurpleDark,
    tertiary = BrandPurple,

    background = androidx.compose.ui.graphics.Color.Transparent,
    surface = androidx.compose.ui.graphics.Color.Transparent,

    onBackground = OffWhite,
    onSurface = OffWhite,
    onPrimary = OffWhite,
    onSecondary = OffWhite,
    onTertiary = OffWhite,
)

@Composable
fun LinguAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
