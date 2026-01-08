package com.example.linguai.ui.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguai.data.auth.AuthRepository
import com.example.linguai.data.history.HistoryRepository
import com.example.linguai.data.translation.MyMemoryTranslationRepository
import com.example.linguai.data.translation.TranslationRepository
import com.example.linguai.model.Language
import com.example.linguai.model.TranslationHistoryItem
import com.example.linguai.ui.UiTextKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ImageOcrUiState(
    val recognizedText: String = "",
    val translatedText: String = "",
    val detectedSource: String? = null,
    val isTranslating: Boolean = false,

    val messageKey: UiTextKey? = null,
    val messageArgs: List<String> = emptyList(),
    val messageIsError: Boolean = false,

    val targetLanguage: Language = Language.defaultSupported.first(),
    val availableTargets: List<Language> = Language.defaultSupported,
    val selectedSource: Language? = null // null = Auto
)

class ImageOcrViewModel(
    private val repository: TranslationRepository = MyMemoryTranslationRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageOcrUiState())
    val uiState: StateFlow<ImageOcrUiState> = _uiState.asStateFlow()

    private val historyRepo = HistoryRepository()
    private val authRepo = AuthRepository()

    fun onRecognizedTextChanged(text: String) {
        _uiState.update {
            it.copy(recognizedText = text, translatedText = "", detectedSource = null, messageKey = null)
        }
    }

    fun onTargetLanguageSelected(lang: Language) {
        _uiState.update {
            it.copy(targetLanguage = lang, translatedText = "", detectedSource = null, messageKey = null)
        }
    }

    fun onSourceSelected(lang: Language?) {
        _uiState.update { it.copy(selectedSource = lang, messageKey = null) }
    }

    fun onOcrResult(text: String) {
        val cleaned = text.trim()
        _uiState.update {
            it.copy(recognizedText = cleaned, translatedText = "", detectedSource = null, messageKey = null)
        }
    }

    fun onTranslateClicked() {
        val current = _uiState.value
        val input = current.recognizedText.trim()

        if (input.isBlank()) {
            _uiState.update {
                it.copy(
                    messageKey = UiTextKey.ErrorNoRecognizedText,
                    messageArgs = emptyList(),
                    messageIsError = true,
                    translatedText = "",
                    detectedSource = null
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isTranslating = true, messageKey = null) }

                val result = repository.translate(text = input, target = current.targetLanguage)

                _uiState.update {
                    it.copy(
                        isTranslating = false,
                        translatedText = result.translatedText,
                        detectedSource = result.sourceCode,
                        messageKey = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isTranslating = false,
                        messageKey = UiTextKey.ErrorTranslateFormat2,
                        messageArgs = listOf(e::class.simpleName ?: "Exception", e.message.orEmpty()),
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
                        mode = "image",
                        inputText = s.recognizedText,
                        translatedText = s.translatedText,
                        sourceLang = s.detectedSource ?: s.selectedSource?.code ?: "auto",
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
