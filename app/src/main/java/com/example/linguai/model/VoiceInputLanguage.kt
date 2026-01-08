package com.example.linguai.model

/**
 * Limba pentru input vocal (speech-to-text).
 * AUTO = lasă Android să decidă (localul device-ului).
 */
enum class VoiceInputLanguage(
    val displayName: String,
    val langTag: String?
) {
    AUTO("Auto", null),
    ENGLISH("English", "en-US"),
    ROMANIAN("Română", "ro-RO"),
    ITALIAN("Italiano", "it-IT"),
    FRENCH("Français", "fr-FR"),
    GERMAN("Deutsch", "de-DE"),
    SPANISH("Español", "es-ES")
}
