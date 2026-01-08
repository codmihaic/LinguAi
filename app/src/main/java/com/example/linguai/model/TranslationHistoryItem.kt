package com.example.linguai.model

import com.google.firebase.Timestamp

data class TranslationHistoryItem(
    val id: String = "",
    val createdAt: Timestamp? = null,
    val mode: String = "text", // text | voice | image
    val inputText: String = "",
    val translatedText: String = "",
    val sourceLang: String = "auto",
    val targetLang: String = "ro",
)
