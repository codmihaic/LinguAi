package com.example.linguai.ui.common

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.linguai.ui.UiTextKey
import com.example.linguai.ui.t

@Composable
fun TranslationActionsRow(
    textToCopyShare: String,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = {
            clipboard.setText(AnnotatedString(textToCopyShare))
        }) { Text(t(UiTextKey.CommonCopy)) }

        Spacer(Modifier.width(8.dp))

        TextButton(onClick = {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, textToCopyShare)
            }
            context.startActivity(Intent.createChooser(sendIntent, null))
        }) { Text(t(UiTextKey.CommonShare)) }

        Spacer(Modifier.width(8.dp))

        TextButton(onClick = onSave) { Text(t(UiTextKey.CommonSave)) }
    }
}
