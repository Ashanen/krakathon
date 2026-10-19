package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

@Composable
internal fun StateLayout(
    icon: ImageVector,
    iconTint: Color,
    iconBackground: Color,
    title: String,
    description: String?,
    actionText: String?,
    onAction: (() -> Unit)?,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(KrakTheme.spacing.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small, Alignment.CenterVertically),
    ) {
        Box(
            modifier = Modifier.size(72.dp).clip(CircleShape).background(iconBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(KrakTheme.iconSize.large))
        }
        Text(
            text = title,
            style = KrakTheme.typography.titleMedium,
            color = KrakTheme.colors.onSurface,
            textAlign = TextAlign.Center,
        )
        if (description != null) {
            Text(
                text = description,
                style = KrakTheme.typography.bodyMedium,
                color = KrakTheme.colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        if (actionText != null && onAction != null) {
            KrakButton(
                text = actionText,
                onClick = onAction,
                fullWidth = false,
                modifier = Modifier.padding(top = KrakTheme.spacing.small),
            )
        }
    }
}

/** Empty list / no results. Offer a [actionText] whenever the user can fix the emptiness. */
@Composable
fun KrakEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector = Icons.Outlined.Inbox,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    StateLayout(
        icon = icon,
        iconTint = KrakTheme.colors.onPrimaryContainer,
        iconBackground = KrakTheme.colors.primaryContainer,
        title = title,
        description = description,
        actionText = actionText,
        onAction = onAction,
        modifier = modifier,
    )
}
