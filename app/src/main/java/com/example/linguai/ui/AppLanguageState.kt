package com.example.linguai.ui

import androidx.compose.runtime.compositionLocalOf
import com.example.linguai.model.Language

data class AppLanguageState(
    val current: Language,
    val setLanguage: (Language) -> Unit
)

val LocalAppLanguageState = compositionLocalOf<AppLanguageState> {
    error(UiTextDictionary.resolve(UiTextKey.InternalMissingAppLanguageState, "en"))
}
