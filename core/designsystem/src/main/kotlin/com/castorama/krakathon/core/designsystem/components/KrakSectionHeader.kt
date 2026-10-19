package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Section title with an optional trailing "see all" action, or a passive [count] when there is none. */
@Composable
fun KrakSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingActionText: String? = null,
    onTrailingActionClick: (() -> Unit)? = null,
    count: String? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = KrakTheme.colors.primary,
                    modifier = Modifier.size(KrakTheme.iconSize.small),
                )
            }
            Text(
                text = title,
                style = KrakTheme.typography.titleMedium,
                color = KrakTheme.colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (trailingActionText != null && onTrailingActionClick != null) {
            Row(
                modifier = Modifier.clickable(role = Role.Button, onClick = onTrailingActionClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(trailingActionText, style = KrakTheme.typography.labelLarge, color = KrakTheme.colors.primaryText)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = KrakTheme.colors.primaryText,
                    modifier = Modifier.size(KrakTheme.iconSize.extraSmall),
                )
            }
        } else if (count != null) {
            Text(count, style = KrakTheme.typography.bodySmall, color = KrakTheme.colors.onSurfaceVariant, maxLines = 1)
        }
    }
}
