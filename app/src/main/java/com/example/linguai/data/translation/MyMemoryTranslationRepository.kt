package com.example.linguai.data.translation

import com.example.linguai.data.language.LanguageIdService
import com.example.linguai.data.language.MlKitLanguageIdService
import com.example.linguai.model.Language
import com.example.linguai.model.TranslationResult

class MyMemoryTranslationRepository(
    private val api: MyMemoryApi = MyMemoryApiClient.api,
    private val langId: LanguageIdService = MlKitLanguageIdService()
) : TranslationRepository {

    override suspend fun translate(text: String, target: Language): TranslationResult {
        // 1. Detectăm limba sursă local
        val detected = langId.detectLanguage(text)

        // 2. MyMemory are nevoie de un cod concret, nu "auto"
        val sourceForApi = when {
            detected.isNullOrBlank() -> "en"      // fallback simplu
            detected == "und"        -> "en"
            else                     -> detected
        }

        val pair = "${sourceForApi}|${target.code}".lowercase()

        // 3. Cerere HTTP
        val resp = api.getTranslation(q = text, langPair = pair)

        if (resp.status != 200) {
            // aruncăm o eroare clară, o prinde ViewModel-ul
            throw IllegalStateException("MyMemory status ${resp.status}")
        }

        // 4. Întoarcem către UI
        return TranslationResult(
            sourceCode = detected,
            targetCode = target.code,
            translatedText = resp.data.translatedText
        )
    }
}
