package com.example.linguai.data.language

interface LanguageIdService {
    /**
     * Întoarce codul de limbă (ex. "en", "ro", "it") sau null dacă nu poate identifica.
     */
    suspend fun detectLanguage(text: String): String?
}
