package com.example.linguai.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.linguai.ui.theme.GlassBorder
import com.example.linguai.ui.theme.GlassSurfaceStrong

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    border: BorderStroke = BorderStroke(1.dp, GlassBorder),
    containerColor: Color = GlassSurfaceStrong,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = border
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minHeight: Dp = 170.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val shape = RoundedCornerShape(18.dp)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = singleLine,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight),
        shape = shape,
        textStyle = textStyle,
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.55f)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = GlassSurfaceStrong,
            unfocusedContainerColor = GlassSurfaceStrong,
            disabledContainerColor = GlassSurfaceStrong.copy(alpha = 0.55f),

            focusedBorderColor = GlassBorder,
            unfocusedBorderColor = GlassBorder.copy(alpha = 0.85f),
            disabledBorderColor = GlassBorder.copy(alpha = 0.40f),

            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.White.copy(alpha = 0.55f),

            focusedPlaceholderColor = Color.White.copy(alpha = 0.55f),
            unfocusedPlaceholderColor = Color.White.copy(alpha = 0.55f),

            cursorColor = Color.White
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = VisualTransformation.None
    )
}

@Composable
fun GlassPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(28.dp)

    val gradient = Brush.horizontalGradient(
        listOf(
            Color(0xFF6F5BFF),
            Color(0xFF5B7CFF),
            Color(0xFF6F5BFF)
        )
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(shape)
            .background(gradient)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        shape = shape,
        color = Color.Transparent,
        shadowElevation = 12.dp,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

/**
 * A customizable button with a horizontal gradient. This behaves similarly to
 * [GlassPrimaryButton] but lets callers supply the gradient colors used.
 * It still uses a glass-like elevated surface and rounded shape. The default
 * height matches the primary button so that buttons can be easily aligned.
 *
 * @param text The text displayed on the button
 * @param gradientColors A list of colours used to build the horizontal gradient
 * @param onClick Callback when the button is tapped
 * @param modifier Modifier applied to the button
 * @param enabled Whether the button is interactive
 */
@Composable
fun GlassColoredButton(
    text: String,
    gradientColors: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(28.dp)
    val gradient = Brush.horizontalGradient(gradientColors)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(shape)
            .background(gradient)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        shape = shape,
        color = Color.Transparent,
        shadowElevation = 12.dp,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    val shape = RoundedCornerShape(22.dp)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        shape = shape,
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = {
            Text(
                placeholder,
                color = Color.White.copy(alpha = 0.55f),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f)
                )
            }
        } else null,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = GlassSurfaceStrong,
            unfocusedContainerColor = GlassSurfaceStrong,
            disabledContainerColor = GlassSurfaceStrong.copy(alpha = 0.55f),

            focusedBorderColor = GlassBorder,
            unfocusedBorderColor = GlassBorder.copy(alpha = 0.85f),
            disabledBorderColor = GlassBorder.copy(alpha = 0.40f),

            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.White.copy(alpha = 0.55f),

            focusedPlaceholderColor = Color.White.copy(alpha = 0.55f),
            unfocusedPlaceholderColor = Color.White.copy(alpha = 0.55f),

            cursorColor = Color.White,
            focusedLeadingIconColor = Color.White.copy(alpha = 0.8f),
            unfocusedLeadingIconColor = Color.White.copy(alpha = 0.8f),
            disabledLeadingIconColor = Color.White.copy(alpha = 0.5f)
        )
    )
}
