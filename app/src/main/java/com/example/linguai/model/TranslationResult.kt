package com.example.linguai.model

data class TranslationResult(
    val sourceCode: String?,   // ex. "en" – limba detectată
    val targetCode: String,    // ex. "ro"
    val translatedText: String
)