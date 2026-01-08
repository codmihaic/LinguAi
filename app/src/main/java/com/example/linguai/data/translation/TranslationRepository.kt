package com.example.linguai.data.translation

import com.example.linguai.model.Language
import com.example.linguai.model.TranslationResult

interface TranslationRepository {
    suspend fun translate(text: String, target: Language): TranslationResult
}