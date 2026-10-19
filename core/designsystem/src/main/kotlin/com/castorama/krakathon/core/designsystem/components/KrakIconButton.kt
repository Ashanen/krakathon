package com.castorama.krakathon.core.designsystem.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** [contentDescription] is mandatory: an icon-only control needs an accessible name. */
@Composable
fun KrakIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = KrakTheme.colors.onSurface,
    containerColor: Color = Color.Transparent,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor,
            contentColor = tint,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = KrakTheme.colors.onSurfaceDisabled,
        ),
    ) {
        Icon(icon, contentDescription = contentDescription)
    }
}
