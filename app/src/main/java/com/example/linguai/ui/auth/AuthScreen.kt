package com.example.linguai.ui.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linguai.R
import com.example.linguai.ui.UiTextKey
import com.example.linguai.ui.common.*
import com.example.linguai.ui.t
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(nav: NavController) {
    val vm: AuthViewModel = viewModel()
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current

    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successShown by remember { mutableStateOf(false) }

    LaunchedEffect(state.status) {
        if (state.status is AuthStatus.LoggedIn && !successShown) {
            successShown = true
            showSuccessDialog = true
        }
    }

    val translatedError = state.errorKey?.let { t(it) }
    val finalErrorMessage = translatedError ?: state.errorMessageRaw

    LaunchedEffect(finalErrorMessage) {
        if (!finalErrorMessage.isNullOrBlank()) {
            errorMessage = finalErrorMessage
        }
    }

    // Google sign-in launcher setup
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    val googleClient = remember { GoogleSignIn.getClient(context, gso) }
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.result
                val idToken = account.idToken
                if (!idToken.isNullOrBlank()) {
                    vm.submitGoogle(idToken)
                }
            } catch (_: Exception) {
            }
        }
    }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                val canNavigateBack = nav.previousBackStackEntry != null

                TopAppBar(
                    title = {
                        Text(
                            text = if (state.isRegisterMode) t(UiTextKey.AuthTitleRegister) else t(UiTextKey.AuthTitleLogin),
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        if (canNavigateBack) {
                            IconButton(onClick = { nav.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(64.dp))
                GlassTextField(
                    value = state.email,
                    onValueChange = vm::onEmailChanged,
                    placeholder = t(UiTextKey.AuthEmailLabel),
                    leadingIcon = Icons.Outlined.Email,
                    isPassword = false
                )
                Spacer(modifier = Modifier.height(16.dp))
                GlassTextField(
                    value = state.password,
                    onValueChange = vm::onPasswordChanged,
                    placeholder = t(UiTextKey.AuthPasswordLabel),
                    leadingIcon = Icons.Outlined.Lock,
                    isPassword = true
                )
                Spacer(modifier = Modifier.height(24.dp))
                GlassPrimaryButton(
                    text = if (state.isLoading) t(UiTextKey.AuthProcessing) else if (state.isRegisterMode) t(
                        UiTextKey.AuthButtonRegister
                    ) else t(UiTextKey.AuthButtonLogin),
                    onClick = vm::submit,
                    enabled = !state.isLoading
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = vm::toggleMode) {
                    Text(
                        text = if (state.isRegisterMode) t(UiTextKey.AuthSwitchToLogin) else t(
                            UiTextKey.AuthSwitchToRegister
                        ),
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { googleLauncher.launch(googleClient.signInIntent) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    Text(t(UiTextKey.AuthGoogleButton))
                }
            }
        }
    }

    if (showSuccessDialog) {
        ErrorDialog(
            message = t(UiTextKey.AuthLoginSuccess),
            onDismiss = {
                showSuccessDialog = false
                nav.popBackStack()
            }
        )
    }

    errorMessage?.let { msg ->
        ErrorDialog(
            message = msg,
            onDismiss = {
                errorMessage = null
                vm.clearError()
            }
        )
    }
}