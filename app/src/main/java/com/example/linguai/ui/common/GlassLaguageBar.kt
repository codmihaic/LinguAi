package com.example.linguai.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.linguai.model.Language
import com.example.linguai.ui.theme.GlassBorder
import com.example.linguai.ui.theme.GlassSurfaceStrong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassLanguageBar(
    from: Language?,
    to: Language,
    languages: List<Language>,
    includeAuto: Boolean,
    onFromChange: (Language?) -> Unit,
    onToChange: (Language) -> Unit,
    onSwap: () -> Unit,
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
            LanguagePillDropdown(
                current = from,
                languages = languages,
                includeAuto = includeAuto,
                onChange = onFromChange,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onSwap,
                modifier = Modifier.padding(horizontal = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.SwapHoriz,
                    contentDescription = "Swap",
                    tint = Color.White
                )
            }

            LanguagePillDropdown(
                current = to,
                languages = languages,
                includeAuto = false,
                onChange = { onToChange(it ?: to) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguagePillDropdown(
    current: Language?,
    languages: List<Language>,
    includeAuto: Boolean,
    onChange: (Language?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val label = current?.name ?: "Auto"
    val currentEmoji = LanguageUtils.emojiForLanguage(current)
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
                Text(
                    text = "$currentEmoji $label",
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
            if (includeAuto) {
                DropdownMenuItem(
                    text = { Text("${LanguageUtils.emojiForLanguage(null)} Auto") },
                    onClick = {
                        expanded = false
                        onChange(null)
                    }
                )
            }

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
