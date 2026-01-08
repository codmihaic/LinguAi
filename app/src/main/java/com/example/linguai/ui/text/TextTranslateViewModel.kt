package com.example.linguai.ui.text

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

data class TextTranslateUiState(
    val input: String = "",
    val translated: String = "",
    val detectedSource: String? = null,
    val isLoading: Boolean = false,

    val messageKey: UiTextKey? = null,
    val messageArgs: List<String> = emptyList(),
    val messageIsError: Boolean = false,

    val targetLanguages: List<Language> = Language.defaultSupported,
    val selectedTarget: Language = Language.defaultSupported.first(),
    val selectedSource: Language? = null // null = Auto
)

class TextTranslateViewModel(
    private val repository: TranslationRepository = MyMemoryTranslationRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TextTranslateUiState())
    val uiState: StateFlow<TextTranslateUiState> = _uiState.asStateFlow()

    private val historyRepo = HistoryRepository()
    private val authRepo = AuthRepository()

    fun onInputChanged(newText: String) {
        _uiState.update { it.copy(input = newText, messageKey = null) }
    }

    fun onTargetSelected(language: Language) {
        _uiState.update { it.copy(selectedTarget = language, messageKey = null) }
    }

    fun onSourceSelected(lang: Language?) {
        _uiState.update { it.copy(selectedSource = lang, messageKey = null) }
    }

    fun onTranslateClicked() {
        val current = _uiState.value

        if (current.input.isBlank()) {
            _uiState.update {
                it.copy(
                    messageKey = UiTextKey.ErrorTextEmptyInput,
                    messageArgs = emptyList(),
                    messageIsError = true,
                    translated = "",
                    detectedSource = null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, messageKey = null) }

            try {
                val result = repository.translate(
                    text = current.input,
                    target = current.selectedTarget
                )

                _uiState.update {
                    it.copy(
                        translated = result.translatedText,
                        detectedSource = result.sourceCode,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
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
            if (s.translated.isBlank()) return@launch

            val user = authRepo.currentUser()
            if (user == null) {
                _uiState.update {
                    it.copy(
                        messageKey = UiTextKey.SaveRequiresLogin,
                        messageArgs = emptyList(),
                        messageIsError = true
                    )
                }
                return@launch
            }

            try {
                historyRepo.add(
                    TranslationHistoryItem(
                        mode = "text",
                        inputText = s.input,
                        translatedText = s.translated,
                        sourceLang = s.detectedSource ?: s.selectedSource?.code ?: "auto",
                        targetLang = s.selectedTarget.code
                    )
                )
                _uiState.update {
                    it.copy(
                        messageKey = UiTextKey.SaveSuccess,
                        messageArgs = emptyList(),
                        messageIsError = false
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        messageKey = UiTextKey.SaveFailed,
                        messageArgs = emptyList(),
                        messageIsError = true
                    )
                }
            }
        }
    }
}
