package com.example.linguai.ui.auth

import com.example.linguai.ui.UiTextKey

sealed class AuthStatus {
    data object Guest : AuthStatus()
    data class LoggedIn(val uid: String, val email: String?) : AuthStatus()
}

data class AuthUiState(
    val status: AuthStatus = AuthStatus.Guest,
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorKey: UiTextKey? = null,
    val errorMessageRaw: String? = null,
    val isRegisterMode: Boolean = false
)
