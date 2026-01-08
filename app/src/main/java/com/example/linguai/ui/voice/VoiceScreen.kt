package com.example.linguai.ui.voice

import android.Manifest
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linguai.ui.UiTextKey
import com.example.linguai.ui.common.*
import com.example.linguai.ui.t
import com.example.linguai.ui.common.AppBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceScreen(nav: NavController) {
    val context = LocalContext.current
    val viewModel: VoiceTranslateViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()

    val supportedTargets = remember { viewModel.getSupportedTargets() }
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    DisposableEffect(Unit) { onDispose { speechRecognizer.destroy() } }

    LaunchedEffect(speechRecognizer) {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                viewModel.onListeningStateChanged(true)
            }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                viewModel.onListeningStateChanged(false)
                viewModel.onRecognitionMessage(
                    UiTextKey.ErrorSpeechCodeFormat1,
                    listOf(error.toString()),
                    isError = true
                )
            }

            override fun onResults(results: Bundle?) {
                viewModel.onListeningStateChanged(false)
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull().orEmpty()
                viewModel.onSpeechResult(text)
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.clearMessage()
            val intent = viewModel.createRecognizerIntent()
            speechRecognizer.startListening(intent)
        } else {
            viewModel.onRecognitionMessage(UiTextKey.ErrorMicPermissionDenied)
        }
    }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(t(UiTextKey.VoiceTitle), color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 18.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                VoiceLanguageBar(
                    inputLanguage = state.inputLanguage,
                    onInputLanguageSelected = { viewModel.onInputLanguageSelected(it) },
                    targetLanguage = state.targetLanguage,
                    onTargetLanguageSelected = { viewModel.onTargetLanguageSelected(it) },
                    supportedTargets = supportedTargets
                )
                Spacer(modifier = Modifier.height(32.dp))
                VoiceMicButton(
                    isListening = state.isListening,
                    isTranslating = state.isTranslating,
                    onClick = {
                        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
                            viewModel.onRecognitionMessage(UiTextKey.ErrorSpeechUnavailable)
                            return@VoiceMicButton
                        }
                        if (!state.isListening && !state.isTranslating) {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        } else if (state.isListening) {
                            speechRecognizer.stopListening()
                            viewModel.onListeningStateChanged(false)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Status or hint text
                Text(
                    text = when {
                        state.isListening -> t(UiTextKey.VoiceListening)
                        state.isTranslating -> t(UiTextKey.VoiceTranslatingLabel)
                        else -> t(UiTextKey.VoiceHint)
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                // Recognized text
                if (state.recognizedText.isNotBlank()) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Text(
                            text = state.recognizedText,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                if (state.isTranslating) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(24.dp))
                }
                if (state.translatedText.isNotBlank()) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Column {
                            Text(
                                text = state.translatedText,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            TranslationActionsRow(
                                textToCopyShare = state.translatedText,
                                onSave = { viewModel.saveCurrent() }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                state.messageKey?.let { key ->
                    val msg = t(key, *state.messageArgs.toTypedArray())
                    ErrorDialog(
                        message = msg,
                        onDismiss = { viewModel.clearMessage() }
                    )
                }
            }
        }
    }
}