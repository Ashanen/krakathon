package com.castorama.krakathon.core.designsystem.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.castorama.krakathon.core.designsystem.R
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Two-button dialog; pass `dismissText = null` for a single-button info dialog. */
@Composable
fun KrakConfirmDialog(
    title: String?,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String? = stringResource(R.string.ds_cancel),
    destructive: Boolean = false,
    icon: ImageVector? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        confirmButton = { KrakTextButton(text = confirmText, onClick = onConfirm, destructive = destructive) },
        dismissButton = dismissText?.let { { KrakTextButton(text = it, onClick = onDismiss) } },
        icon = icon?.let { { Icon(it, contentDescription = null) } },
        title = title?.let { { Text(it, style = KrakTheme.typography.titleLarge) } },
        text = { Text(message, style = KrakTheme.typography.bodyMedium) },
        shape = KrakTheme.shapes.extraLarge,
        containerColor = KrakTheme.colors.surfaceContainerHigh,
        iconContentColor = if (destructive) KrakTheme.colors.error else KrakTheme.colors.primary,
        titleContentColor = KrakTheme.colors.onSurface,
        textContentColor = KrakTheme.colors.onSurfaceVariant,
    )
}
