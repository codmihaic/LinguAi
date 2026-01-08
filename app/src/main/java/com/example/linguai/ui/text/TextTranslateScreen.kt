package com.example.linguai.ui.text

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.linguai.model.Language
import com.example.linguai.ui.UiTextKey
import com.example.linguai.ui.common.*
import com.example.linguai.ui.t

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextTranslateScreen(
    navController: NavHostController,
    vm: TextTranslateViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = t(UiTextKey.TextTitle),
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
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
                Spacer(Modifier.height(10.dp))

                GlassLanguageBar(
                    from = state.selectedSource,
                    to = state.selectedTarget,
                    languages = state.targetLanguages,
                    includeAuto = true,
                    onFromChange = { vm.onSourceSelected(it) },
                    onToChange = { vm.onTargetSelected(it) },
                    onSwap = {
                        val oldFrom = state.selectedSource
                        val oldTo = state.selectedTarget

                        if (oldFrom == null) {
                            vm.onSourceSelected(oldTo)
                            vm.onTargetSelected(Language.defaultSupported.first())
                        } else {
                            vm.onSourceSelected(oldTo)
                            vm.onTargetSelected(oldFrom)
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))

                GlassTextArea(
                    value = state.input,
                    onValueChange = { vm.onInputChanged(it) },
                    placeholder = t(UiTextKey.TextInputLabel),
                    minHeight = 180.dp
                )

                Spacer(Modifier.height(16.dp))

                GlassPrimaryButton(
                    text = if (state.isLoading)
                        t(UiTextKey.TextTranslateButtonLoading)
                    else
                        t(UiTextKey.TextTranslateButtonIdle),
                    onClick = { vm.onTranslateClicked() },
                    enabled = !state.isLoading
                )

                Spacer(Modifier.height(12.dp))

                state.messageKey?.let { key ->
                    Text(
                        text = t(key, *state.messageArgs.toTypedArray()),
                        color = if (state.messageIsError)
                            MaterialTheme.colorScheme.error
                        else
                            Color(0xFFB9FFDD),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))
                }

                if (state.translated.isNotBlank()) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Text(
                            text = state.translated,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(Modifier.height(10.dp))

                        TranslationActionsRow(
                            textToCopyShare = state.translated,
                            onSave = { vm.saveCurrent() }
                        )
                    }
                } else {
                    Spacer(Modifier.height(8.dp))
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
