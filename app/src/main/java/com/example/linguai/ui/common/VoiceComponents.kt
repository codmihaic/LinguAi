package com.example.linguai.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.linguai.model.Language
import com.example.linguai.model.VoiceInputLanguage
import com.example.linguai.ui.theme.GlassBorder
import com.example.linguai.ui.theme.GlassSurfaceStrong


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceLanguageBar(
    inputLanguage: VoiceInputLanguage,
    onInputLanguageSelected: (VoiceInputLanguage) -> Unit,
    targetLanguage: Language,
    onTargetLanguageSelected: (Language) -> Unit,
    supportedTargets: List<Language>,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(28.dp)
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        border = BorderStroke(1.dp, GlassBorder),
        containerColor = GlassSurfaceStrong,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VoiceInputLanguageDropdown(
                current = inputLanguage,
                onChange = onInputLanguageSelected,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            LanguagePillDropdownForVoice(
                current = targetLanguage,
                languages = supportedTargets,
                onChange = onTargetLanguageSelected,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VoiceInputLanguageDropdown(
    current: VoiceInputLanguage,
    onChange: (VoiceInputLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val anchorShape = RoundedCornerShape(18.dp)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = anchorShape,
            color = Color.White.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, GlassBorder.copy(alpha = 0.70f))
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val emoji = LanguageUtils.emojiForVoiceLanguage(current)
                Text(
                    text = "$emoji ${current.displayName}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            VoiceInputLanguage.values().forEach { lang ->
                val emoji = LanguageUtils.emojiForVoiceLanguage(lang)
                DropdownMenuItem(
                    text = { Text("$emoji ${lang.displayName}") },
                    onClick = {
                        expanded = false
                        onChange(lang)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguagePillDropdownForVoice(
    current: Language,
    languages: List<Language>,
    onChange: (Language) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val anchorShape = RoundedCornerShape(18.dp)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = anchorShape,
            color = Color.White.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, GlassBorder.copy(alpha = 0.70f))
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val emoji = LanguageUtils.emojiForLanguage(current)
                Text(
                    text = "$emoji ${current.name}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { lang ->
                val emoji = LanguageUtils.emojiForLanguage(lang)
                DropdownMenuItem(
                    text = { Text("$emoji ${lang.name}") },
                    onClick = {
                        expanded = false
                        onChange(lang)
                    }
                )
            }
        }
    }
}

@Composable
fun VoiceMicButton(
    isListening: Boolean,
    isTranslating: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val outerColors = if (isListening) {
        listOf(
            Color(0xFF7E5AFF),
            Color(0xFF5BC9FF)
        )
    } else {
        listOf(
            Color(0xFF6078FF),
            Color(0xFF4E92FF)
        )
    }
    val disabledAlpha = 0.5f

    val size = 180.dp

    Surface(
        onClick = onClick,
        enabled = !isTranslating,
        shape = CircleShape,
        color = Color.Transparent,
        shadowElevation = if (isListening) 20.dp else 12.dp,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = modifier
                .size(size)
                .background(
                    brush = Brush.horizontalGradient(colors = outerColors),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            val innerSize = size * 0.55f
            Box(
                modifier = Modifier
                    .size(innerSize)
                    .background(
                        color = if (isTranslating) Color(0xFF6175FF).copy(alpha = disabledAlpha) else Color(0xFF6175FF),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = "Microphone",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}