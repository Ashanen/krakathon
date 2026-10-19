package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Tappable action chip. */
@Composable
fun KrakChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
) {
    AssistChip(
        onClick = onClick,
        label = { Text(text, style = KrakTheme.typography.labelLarge, maxLines = 1) },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = leadingIcon?.let {
            { Icon(it, contentDescription = null, modifier = Modifier.size(KrakTheme.iconSize.extraSmall)) }
        },
        shape = KrakTheme.shapes.small,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = KrakTheme.colors.surface,
            labelColor = KrakTheme.colors.onSurface,
            leadingIconContentColor = KrakTheme.colors.primary,
            disabledContainerColor = KrakTheme.colors.surfaceDisabled,
            disabledLabelColor = KrakTheme.colors.onSurfaceDisabled,
        ),
        border = BorderStroke(1.dp, KrakTheme.colors.outlineVariant),
    )
}

/** Toggleable chip; shows a check while selected. */
@Composable
fun KrakFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text, style = KrakTheme.typography.labelLarge, maxLines = 1) },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = if (selected) {
            { Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(KrakTheme.iconSize.extraSmall)) }
        } else {
            null
        },
        shape = KrakTheme.shapes.small,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = KrakTheme.colors.surface,
            labelColor = KrakTheme.colors.onSurfaceVariant,
            selectedContainerColor = KrakTheme.colors.primaryContainer,
            selectedLabelColor = KrakTheme.colors.onPrimaryContainer,
            selectedLeadingIconColor = KrakTheme.colors.onPrimaryContainer,
        ),
        border = BorderStroke(1.dp, if (selected) KrakTheme.colors.primary else KrakTheme.colors.outlineVariant),
    )
}
