package com.example.linguai.ui.voice

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguai.data.auth.AuthRepository
import com.example.linguai.data.history.HistoryRepository
import com.example.linguai.data.translation.MyMemoryTranslationRepository
import com.example.linguai.data.translation.TranslationRepository
import com.example.linguai.model.Language
import com.example.linguai.model.TranslationHistoryItem
import com.example.linguai.model.VoiceInputLanguage
import com.example.linguai.ui.UiTextKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class VoiceUiState(
    val recognizedText: String = "",
    val translatedText: String = "",
    val detectedSource: String? = null,
    val isListening: Boolean = false,
    val isTranslating: Boolean = false,

    val messageKey: UiTextKey? = null,
    val messageArgs: List<String> = emptyList(),
    val messageIsError: Boolean = false,

    val inputLanguage: VoiceInputLanguage = VoiceInputLanguage.AUTO,
    val targetLanguage: Language = Language.defaultSupported.first()
)

class VoiceTranslateViewModel(
    private val repository: TranslationRepository = MyMemoryTranslationRepository()
) : ViewModel() {

    private val supportedTargets: List<Language> = Language.defaultSupported

    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    private val historyRepo = HistoryRepository()
    private val authRepo = AuthRepository()

    fun getSupportedTargets(): List<Language> = supportedTargets

    fun onInputLanguageSelected(lang: VoiceInputLanguage) {
        _uiState.update { it.copy(inputLanguage = lang, messageKey = null) }
    }

    fun onTargetLanguageSelected(lang: Language) {
        _uiState.update { it.copy(targetLanguage = lang, messageKey = null) }
    }

    fun createRecognizerIntent(): Intent {
        val langTag = when (val sel = _uiState.value.inputLanguage) {
            VoiceInputLanguage.AUTO -> Locale.getDefault().toLanguageTag()
            else -> sel.langTag ?: Locale.getDefault().toLanguageTag()
        }

        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }
    }

    fun onListeningStateChanged(isListening: Boolean) {
        _uiState.update { it.copy(isListening = isListening) }
    }

    fun onRecognitionMessage(key: UiTextKey, args: List<String> = emptyList(), isError: Boolean = true) {
        _uiState.update { it.copy(isListening = false, messageKey = key, messageArgs = args, messageIsError = isError) }
    }

    fun onSpeechResult(rawText: String) {
        val cleaned = rawText.trim().replace("\\s+".toRegex(), " ")
        if (cleaned.isBlank()) {
            _uiState.update { it.copy(recognizedText = "", translatedText = "", messageKey = null) }
            return
        }

        _uiState.update { it.copy(recognizedText = cleaned, messageKey = null) }

        val target = _uiState.value.targetLanguage

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isTranslating = true, messageKey = null) }

                val result = repository.translate(text = cleaned, target = target)

                _uiState.update {
                    it.copy(
                        isTranslating = false,
                        translatedText = result.translatedText,
                        detectedSource = result.sourceCode
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isTranslating = false,
                        messageKey = UiTextKey.ErrorTranslateFormat2,
                        messageArgs = listOf(
                            e::class.simpleName ?: "Exception",
                            e.message.orEmpty()
                        ),
                        messageIsError = true
                    )
                }
            }
        }
    }

    fun saveCurrent() {
        viewModelScope.launch {
            val s = _uiState.value
            if (s.translatedText.isBlank()) return@launch

            val user = authRepo.currentUser()
            if (user == null) {
                _uiState.update { it.copy(messageKey = UiTextKey.SaveRequiresLogin, messageIsError = true) }
                return@launch
            }

            try {
                historyRepo.add(
                    TranslationHistoryItem(
                        mode = "voice",
                        inputText = s.recognizedText,
                        translatedText = s.translatedText,
                        sourceLang = s.detectedSource ?: "auto",
                        targetLang = s.targetLanguage.code
                    )
                )
                _uiState.update { it.copy(messageKey = UiTextKey.SaveSuccess, messageIsError = false) }
            } catch (_: Exception) {
                _uiState.update { it.copy(messageKey = UiTextKey.SaveFailed, messageIsError = true) }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(messageKey = null, messageArgs = emptyList()) }
    }
}
