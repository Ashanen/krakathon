package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

@Composable
private fun Modifier.buttonSize(fullWidth: Boolean): Modifier =
    (if (fullWidth) fillMaxWidth() else this).heightIn(min = KrakTheme.dimensions.buttonHeight)

@Composable
private fun RowScope.ButtonLabel(text: String, leadingIcon: ImageVector?, loading: Boolean, indicatorColor: Color) {
    if (loading) {
        CircularProgressIndicator(Modifier.size(18.dp), color = indicatorColor, strokeWidth = 2.dp)
        Spacer(Modifier.width(KrakTheme.spacing.small))
    } else if (leadingIcon != null) {
        Icon(leadingIcon, contentDescription = null, modifier = Modifier.size(KrakTheme.iconSize.small))
        Spacer(Modifier.width(KrakTheme.spacing.small))
    }
    Text(text, style = KrakTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
}

/** Primary filled button; flat at every state. While [loading] it shows a spinner and ignores clicks. */
@Composable
fun KrakButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    fullWidth: Boolean = true,
    loading: Boolean = false,
    destructive: Boolean = false,
) {
    val container = if (destructive) KrakTheme.colors.error else KrakTheme.colors.primary
    val content = if (destructive) KrakTheme.colors.onError else KrakTheme.colors.onPrimary
    Button(
        onClick = { if (!loading) onClick() },
        modifier = modifier.buttonSize(fullWidth),
        enabled = enabled,
        shape = KrakTheme.shapes.large,
        elevation = null,
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = container.copy(alpha = 0.5f),
            disabledContentColor = content,
        ),
    ) { ButtonLabel(text, leadingIcon, loading, content) }
}

@Composable
fun KrakOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    fullWidth: Boolean = true,
    loading: Boolean = false,
    destructive: Boolean = false,
) {
    val content = if (destructive) KrakTheme.colors.error else KrakTheme.colors.primaryText
    OutlinedButton(
        onClick = { if (!loading) onClick() },
        modifier = modifier.buttonSize(fullWidth),
        enabled = enabled,
        shape = KrakTheme.shapes.large,
        border = BorderStroke(1.dp, if (enabled) content else KrakTheme.colors.surfaceDisabled),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = content,
            disabledContentColor = KrakTheme.colors.onSurfaceDisabled,
        ),
    ) { ButtonLabel(text, leadingIcon, loading, content) }
}

@Composable
fun KrakTonalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    fullWidth: Boolean = true,
    loading: Boolean = false,
) {
    FilledTonalButton(
        onClick = { if (!loading) onClick() },
        modifier = modifier.buttonSize(fullWidth),
        enabled = enabled,
        shape = KrakTheme.shapes.large,
        elevation = null,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = KrakTheme.colors.primaryContainer,
            contentColor = KrakTheme.colors.onPrimaryContainer,
            disabledContainerColor = KrakTheme.colors.surfaceDisabled,
            disabledContentColor = KrakTheme.colors.onSurfaceDisabled,
        ),
    ) { ButtonLabel(text, leadingIcon, loading, KrakTheme.colors.onPrimaryContainer) }
}

@Composable
fun KrakTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    destructive: Boolean = false,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = KrakTheme.shapes.medium,
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (destructive) KrakTheme.colors.error else KrakTheme.colors.primaryText,
            disabledContentColor = KrakTheme.colors.onSurfaceDisabled,
        ),
    ) { ButtonLabel(text, leadingIcon, loading = false, indicatorColor = Color.Unspecified) }
}
