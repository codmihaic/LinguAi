package com.example.linguai.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.linguai.model.TranslationHistoryItem
import com.example.linguai.ui.UiTextKey
import com.example.linguai.ui.common.*
import com.example.linguai.ui.t
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(nav: NavController) {
    val vm: HistoryViewModel = viewModel()
    val state by vm.uiState.collectAsState()
    val user = FirebaseAuth.getInstance().currentUser
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(user) {
        if (user != null) {
            vm.load()
        }
    }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(t(UiTextKey.HistoryTitle), color = Color.White) },
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
                Spacer(modifier = Modifier.height(16.dp))
                if (user == null) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Text(
                            text = t(UiTextKey.HistoryLoginRequired),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        GlassPrimaryButton(
                            text = t(UiTextKey.HistoryGoToLogin),
                            onClick = { nav.navigate(com.example.linguai.nav.Screen.Auth.route) }
                        )
                    }
                    return@Column
                }

                if (state.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                }

                state.error?.let { err ->
                    errorMessage = err
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.items) { item ->
                        HistoryItemCard(item = item, onDelete = { vm.delete(it) })
                    }
                }
            }
        }
    }

    errorMessage?.let { msg ->
        ErrorDialog(
            message = msg,
            onDismiss = { errorMessage = null }
        )
    }
}

@Composable
private fun HistoryItemCard(item: TranslationHistoryItem, onDelete: (String) -> Unit) {
    val dateStr = remember(item.createdAt) {
        val ts = item.createdAt?.toDate() ?: Date()
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(ts)
    }
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.sourceLang?.uppercase() ?: "AUTO"} → ${item.targetLang.uppercase()} • $dateStr",
                    color = Color.White.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onDelete(item.id) }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = t(UiTextKey.HistoryDelete),
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            Text(
                text = item.inputText,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = item.translatedText,
                color = Color(0xFFB29AFF),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}