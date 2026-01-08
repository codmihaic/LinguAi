package com.example.linguai.data.language

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import kotlinx.coroutines.tasks.await

class MlKitLanguageIdService(
    private val languageIdentifier: LanguageIdentifier =
        LanguageIdentification.getClient()
) : LanguageIdService {

    override suspend fun detectLanguage(text: String): String? {
        if (text.isBlank()) return null

        return try {
            val code = languageIdentifier.identifyLanguage(text).await()
            // "und" = undetermined
            if (code == "und") null else code
        } catch (e: Exception) {
            null
        }
    }
}
