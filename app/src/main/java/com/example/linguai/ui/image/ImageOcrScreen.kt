package com.example.linguai.ui.image

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linguai.model.Language
import com.example.linguai.ui.UiTextKey
import com.example.linguai.ui.common.*
import com.example.linguai.ui.t
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageOcrScreen(nav: NavController) {
    val viewModel: ImageOcrViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val toastNoTextFound = t(UiTextKey.ToastNoTextFound)
    val toastPhotoFailed = t(UiTextKey.ToastPhotoFailed)
    val errCameraPermissionDenied = t(UiTextKey.ErrorCameraPermissionDenied)
    val tplErrOcr = t(UiTextKey.ErrorOcrFormat1)
    val tplErrReadImage = t(UiTextKey.ErrorReadImageFormat1)

    val textRecognizer = remember {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    fun runOcrOnImage(image: InputImage) {
        viewModel.clearMessage()
        textRecognizer
            .process(image)
            .addOnSuccessListener { visionText ->
                val text = visionText.text
                if (text.isNullOrBlank()) {
                    Toast.makeText(context, toastNoTextFound, Toast.LENGTH_SHORT).show()
                }
                viewModel.onOcrResult(text)
            }
            .addOnFailureListener { e ->
                viewModel.onOcrResult("")
                val msg = String.format(Locale.getDefault(), tplErrOcr, e.message.orEmpty())
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val image = InputImage.fromFilePath(context, uri)
                runOcrOnImage(image)
            } catch (e: Exception) {
                val msg = String.format(Locale.getDefault(), tplErrReadImage, e.message.orEmpty())
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val image = InputImage.fromBitmap(bitmap, 0)
            runOcrOnImage(image)
        } else {
            Toast.makeText(context, toastPhotoFailed, Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, errCameraPermissionDenied, Toast.LENGTH_SHORT).show()
        }
    }

    fun startCameraFlow() {
        val hasPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED

        if (hasPermission) cameraLauncher.launch(null)
        else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(t(UiTextKey.ImageTitle), color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

                GlassLanguageBar(
                    from = state.selectedSource,
                    to = state.targetLanguage,
                    languages = Language.defaultSupported,
                    includeAuto = true,
                    onFromChange = { viewModel.onSourceSelected(it) },
                    onToChange = { viewModel.onTargetLanguageSelected(it) },
                    onSwap = {
                        val oldFrom = state.selectedSource
                        val oldTo = state.targetLanguage
                        viewModel.onTargetLanguageSelected(oldFrom ?: oldTo)
                        if (oldFrom == null) {
                            viewModel.onSourceSelected(null)
                        } else {
                            viewModel.onSourceSelected(oldTo)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GlassActionTile(
                        icon = Icons.Outlined.CameraAlt,
                        label = t(UiTextKey.ImageTakePhoto),
                        onClick = { startCameraFlow() },
                        modifier = Modifier.weight(1f)
                    )
                    GlassActionTile(
                        icon = Icons.Outlined.Image,
                        label = t(UiTextKey.ImagePickFromGallery),
                        onClick = {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Preview area for extracted text / image
                if (state.recognizedText.isBlank()) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = t(UiTextKey.ImageHint),
                                color = Color.White.copy(alpha = 0.55f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        GlassTextArea(
                            value = state.recognizedText,
                            onValueChange = { viewModel.onRecognizedTextChanged(it) },
                            placeholder = "",
                            minHeight = 180.dp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Translate button
                GlassPrimaryButton(
                    text = if (state.isTranslating) t(UiTextKey.ImageTranslateButtonLoading) else t(UiTextKey.ImageTranslateButtonIdle),
                    onClick = { viewModel.onTranslateClicked() },
                    enabled = !state.isTranslating
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Translation result
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

                // Show any informational or error messages as a dialog
                state.messageKey?.let { key ->
                    val msg = t(key, *state.messageArgs.toTypedArray())
                    ErrorDialog(
                        message = msg,
                        onDismiss = { viewModel.clearMessage() }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}