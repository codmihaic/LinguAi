package com.example.linguai.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.linguai.ui.theme.GlassBorder
import com.example.linguai.ui.theme.GlassSurfaceStrong

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", color = Color.White)
            }
        },
        text = {
            Text(
                text = message,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        },
        containerColor = GlassSurfaceStrong,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 8.dp,
        iconContentColor = Color.White,
        textContentColor = Color.White
    )
}