package com.example.linguai.ui.common

import com.example.linguai.model.Language
import com.example.linguai.model.VoiceInputLanguage

object LanguageUtils {
    fun emojiForLanguage(lang: Language?): String {
        return when (lang?.code?.lowercase()) {
            "ro" -> "🇷🇴"
            "en" -> "🇬🇧"
            "it" -> "🇮🇹"
            "fr" -> "🇫🇷"
            "de" -> "🇩🇪"
            "es" -> "🇪🇸"
            else -> "🌐"
        }
    }

    fun emojiForVoiceLanguage(lang: VoiceInputLanguage): String {
        return when (lang) {
            VoiceInputLanguage.ROMANIAN -> "🇷🇴"
            VoiceInputLanguage.ENGLISH -> "🇬🇧"
            VoiceInputLanguage.ITALIAN -> "🇮🇹"
            VoiceInputLanguage.FRENCH -> "🇫🇷"
            VoiceInputLanguage.GERMAN -> "🇩🇪"
            VoiceInputLanguage.SPANISH -> "🇪🇸"
            else -> "🌐"
        }
    }
}