package com.example.linguai.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.linguai.data.auth.AuthRepository
import com.example.linguai.nav.Screen
import com.example.linguai.ui.UiTextKey
import com.example.linguai.ui.common.AppBackground
import com.example.linguai.ui.t
import com.example.linguai.ui.theme.GlassBorder
import com.example.linguai.ui.theme.GlassSurfaceStrong
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nav: NavController) {
    val authRepo = remember { AuthRepository() }
    val firebaseAuth = remember { FirebaseAuth.getInstance() }

    var menuExpanded by remember { mutableStateOf(false) }
    var currentUser by remember { mutableStateOf(firebaseAuth.currentUser) }

    // FIX: isLoggedIn nu e static în AuthRepository; îl derivăm din FirebaseAuth
    val isLoggedIn = currentUser != null

    DisposableEffect(firebaseAuth) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        firebaseAuth.addAuthStateListener(listener)
        onDispose { firebaseAuth.removeAuthStateListener(listener) }
    }

    val backStackEntry by nav.currentBackStackEntryAsState()
    val openOnce = backStackEntry
        ?.savedStateHandle
        ?.getStateFlow("openAccountMenu", false)
        ?.collectAsState()

    LaunchedEffect(openOnce?.value) {
        if (openOnce?.value == true) {
            menuExpanded = true
            backStackEntry?.savedStateHandle?.set("openAccountMenu", false)
        }
    }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("LinguAI", color = MaterialTheme.colorScheme.onBackground) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    actions = {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Account",
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GlassTile(
                        icon = Icons.Outlined.Keyboard,
                        label = t(UiTextKey.HomeTileText),
                        onClick = { nav.navigate(Screen.Text.route) },
                        modifier = Modifier.weight(1f)
                    )
                    GlassTile(
                        icon = Icons.Outlined.Mic,
                        label = t(UiTextKey.HomeTileVoice),
                        onClick = { nav.navigate(Screen.Voice.route) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GlassTile(
                        icon = Icons.Outlined.CameraAlt,
                        label = t(UiTextKey.HomeTileImage),
                        onClick = { nav.navigate(Screen.Image.route) },
                        modifier = Modifier.weight(1f)
                    )
                    GlassTile(
                        icon = Icons.Outlined.History,
                        label = t(UiTextKey.HomeTileHistory),
                        onClick = { nav.navigate(Screen.History.route) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }

        AccountMenuOverlay(
            expanded = menuExpanded,
            onDismiss = { menuExpanded = false },
            isLoggedIn = isLoggedIn,
            onSettings = {
                menuExpanded = false
                nav.navigate(Screen.Settings.route)
            },
            onAuth = {
                menuExpanded = false
                if (isLoggedIn) {
                    authRepo.logout()
                } else {
                    nav.navigate(Screen.Auth.route)
                }
            }
        )
    }
}

@Composable
private fun GlassTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(22.dp)

    Surface(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        shape = shape,
        color = Color.White.copy(alpha = 0.10f),
        tonalElevation = 0.dp,
        shadowElevation = 10.dp,
        border = BorderStroke(1.5.dp, Color(0xFF72D5FF).copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(44.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = label,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AccountMenuOverlay(
    expanded: Boolean,
    onDismiss: () -> Unit,
    isLoggedIn: Boolean,
    onSettings: () -> Unit,
    onAuth: () -> Unit,
) {
    if (!expanded) return

    val scrimInteraction = remember { MutableInteractionSource() }
    val itemInteraction = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
            .clickable(
                interactionSource = scrimInteraction,
                indication = null
            ) { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 10.dp, end = 14.dp)
                .align(Alignment.TopEnd),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = GlassSurfaceStrong
            ),
            border = BorderStroke(1.dp, GlassBorder),
        ) {
            Column(modifier = Modifier.width(210.dp).padding(vertical = 6.dp)) {
                MenuItemRow(
                    text = t(UiTextKey.HomeTileSettings),
                    onClick = onSettings,
                    interaction = itemInteraction
                )

                Spacer(Modifier.height(6.dp))
                Divider(color = Color.White.copy(alpha = 0.15f), thickness = 0.5.dp)
                Spacer(Modifier.height(6.dp))

                MenuItemRow(
                    text = if (isLoggedIn) t(UiTextKey.AuthLogout) else t(UiTextKey.HomeTileAuth),
                    onClick = onAuth,
                    interaction = itemInteraction
                )
            }
        }
    }
}

@Composable
private fun MenuItemRow(
    text: String,
    onClick: () -> Unit,
    interaction: MutableInteractionSource
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = interaction, indication = null) { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

