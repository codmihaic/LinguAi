package com.example.linguai.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linguai.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.linguai.ui.UiTextKey
import com.example.linguai.ui.t

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        refreshAuthStatus()
    }

    fun refreshAuthStatus() {
        val user = repo.currentUser()
        _uiState.update {
            it.copy(
                status = if (user == null) AuthStatus.Guest else AuthStatus.LoggedIn(user.uid, user.email)
            )
        }
    }

    fun onEmailChanged(v: String) {
        _uiState.update { it.copy(email = v, errorKey = null, errorMessageRaw = null) }
    }

    fun onPasswordChanged(v: String) {
        _uiState.update { it.copy(password = v, errorKey = null, errorMessageRaw = null) }
    }

    fun toggleMode() {
        _uiState.update { it.copy(isRegisterMode = !it.isRegisterMode, errorKey = null, errorMessageRaw = null) }
    }

    fun submit() {
        val email = _uiState.value.email.trim()
        val pass = _uiState.value.password

        if (email.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(errorKey = UiTextKey.AuthFillEmailPassword, errorMessageRaw = null) }
            return
        }

        if (_uiState.value.isRegisterMode && pass.length < 6) {
            _uiState.update { it.copy(errorKey = UiTextKey.AuthPasswordMin8, errorMessageRaw = null) }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorKey = null, errorMessageRaw = null) }

                if (_uiState.value.isRegisterMode) {
                    repo.register(email, pass)
                } else {
                    repo.login(email, pass)
                }

                _uiState.update { it.copy(isLoading = false) }
                refreshAuthStatus()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorKey = UiTextKey.AuthGenericError,
                        errorMessageRaw = e.message
                    )
                }
            }
        }
    }

    fun submitGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorKey = null, errorMessageRaw = null) }
                repo.loginWithGoogle(idToken)
                _uiState.update { it.copy(isLoading = false) }
                refreshAuthStatus()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorKey = UiTextKey.AuthGenericErrorGoogle,
                        errorMessageRaw = e.message
                    )
                }
            }
        }
    }

    fun logout() {
        repo.logout()
        refreshAuthStatus()
    }

    /**
     * Clears any current error keys or raw error messages. This is used by
     * the UI to dismiss error dialogs.
     */
    fun clearError() {
        _uiState.update { it.copy(errorKey = null, errorMessageRaw = null) }
    }
}
