package com.example.linguai.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linguai.data.history.HistoryRepository
import com.example.linguai.model.Language
import com.example.linguai.ui.*
import com.example.linguai.ui.auth.AuthStatus
import com.example.linguai.ui.auth.AuthViewModel
import com.example.linguai.ui.common.*
import com.example.linguai.ui.t
import kotlinx.coroutines.launch
import com.example.linguai.ui.theme.GlassSurfaceStrong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(nav: NavController) {
    val appLangState = LocalAppLanguageState.current
    val appThemeState = LocalAppThemeState.current
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()
    val isLoggedIn = authState.status is AuthStatus.LoggedIn


    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showClearHistoryConfirm by remember { mutableStateOf(false) }
    var infoDialogMessage by remember { mutableStateOf<String?>(null) }


    val supportedUiLanguages = Language.uiSupported
    var selectedLang by remember { mutableStateOf(appLangState.current) }

    val historyRepo = remember { HistoryRepository() }
    val coroutineScope = rememberCoroutineScope()
    val clearHistorySuccessMsg = t(UiTextKey.SettingsClearHistory)

    fun clearHistory() {
        coroutineScope.launch {
            try {
                val items = historyRepo.listLatest(500)
                items.forEach { item -> historyRepo.delete(item.id) }
                infoDialogMessage = "$clearHistorySuccessMsg √"
            } catch (e: Exception) {
                infoDialogMessage = e.message ?: "Error"
            }
        }
    }


    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(t(UiTextKey.SettingsTitle), color = Color.White) },
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
                    .padding(horizontal = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    SettingsOptionRow(
                        icon = Icons.Outlined.Language,
                        iconTint = Color(0xFF72D5FF),
                        label = t(UiTextKey.SettingsAppLanguageLabel),
                        onClick = {
                            selectedLang = appLangState.current
                            showLanguageSheet = true
                        }
                    )
                    Divider(color = Color.White.copy(alpha = 0.15f), thickness = 0.5.dp)

                    SettingsToggleRow(
                        icon = Icons.Outlined.DarkMode,
                        iconTint = Color(0xFF9F84FF),
                        label = t(UiTextKey.SettingsDarkMode),
                        checked = appThemeState.darkTheme,
                        onCheckedChange = { checked ->
                            appThemeState.setDarkTheme(checked)
                        }
                    )
                    Divider(color = Color.White.copy(alpha = 0.15f), thickness = 0.5.dp)

                    SettingsToggleRow(
                        icon = Icons.Outlined.Notifications,
                        iconTint = Color(0xFF9F84FF),
                        label = t(UiTextKey.SettingsNotifications),
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )

                    if (isLoggedIn) {
                        Divider(color = Color.White.copy(alpha = 0.15f), thickness = 0.5.dp)
                        SettingsOptionRow(
                            icon = Icons.Outlined.Delete,
                            iconTint = Color(0xFFFF6B6B),
                            label = t(UiTextKey.SettingsClearHistory),
                            onClick = { showClearHistoryConfirm = true }
                        )
                    }
                    Divider(color = Color.White.copy(alpha = 0.15f), thickness = 0.5.dp)

                    SettingsOptionRow(
                        icon = Icons.Outlined.Info,
                        iconTint = Color(0xFF72D5FF),
                        label = t(UiTextKey.SettingsAbout),
                        onClick = { showAboutDialog = true }
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))

                if (isLoggedIn) {
                    GlassColoredButton(
                        text = t(UiTextKey.SettingsLogout),
                        gradientColors = listOf(Color(0xFFFE6B8B), Color(0xFFFD5D69), Color(0xFFFE6B8B)),
                        onClick = {
                            authViewModel.logout()
                            nav.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }


    if (showLanguageSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showLanguageSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Text(
                    text = t(UiTextKey.SettingsLanguagePanelTitle),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                supportedUiLanguages.forEach { lang ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedLang = lang },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedLang.code == lang.code,
                            onClick = { selectedLang = lang },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF6C5DD3),
                                unselectedColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = lang.name,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
                GlassPrimaryButton(
                    text = t(UiTextKey.SettingsApplyButton),
                    onClick = {
                        appLangState.setLanguage(selectedLang)
                        showLanguageSheet = false
                    }
                )
            }
        }
    }


    if (showAboutDialog) {
        ErrorDialog(
            message = t(UiTextKey.SettingsAboutDescription),
            onDismiss = { showAboutDialog = false }
        )
    }


    if (showClearHistoryConfirm) {
        AlertDialog(
            onDismissRequest = { showClearHistoryConfirm = false },
            confirmButton = {
                TextButton(onClick = {
                    showClearHistoryConfirm = false
                    clearHistory()
                }) {
                    Text(t(UiTextKey.SettingsClearHistory), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryConfirm = false }) {
                    Text(t(UiTextKey.CommonCancel), color = Color.White)
                }
            },
            title = {
                Text(
                    text = t(UiTextKey.SettingsClearHistory),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = t(UiTextKey.SettingsClearHistory) + "?",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            containerColor = GlassSurfaceStrong,
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp
        )
    }

    infoDialogMessage?.let { msg ->
        ErrorDialog(
            message = msg,
            onDismiss = { infoDialogMessage = null }
        )
    }
}