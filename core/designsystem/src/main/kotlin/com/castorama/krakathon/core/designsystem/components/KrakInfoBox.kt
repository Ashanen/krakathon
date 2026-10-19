package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.designsystem.theme.KrakTheme
import com.castorama.krakathon.core.designsystem.theme.colors.StatusTone
import com.castorama.krakathon.core.designsystem.theme.colors.colors

private fun StatusTone.defaultIcon(): ImageVector = when (this) {
    StatusTone.Info, StatusTone.Neutral -> Icons.Outlined.Info
    StatusTone.Success -> Icons.Outlined.CheckCircle
    StatusTone.Warning -> Icons.Outlined.Warning
    StatusTone.Error -> Icons.Outlined.ErrorOutline
}

/** Inline hint or banner. Explain the benefit and offer an [actionText] that fixes the situation. */
@Composable
fun KrakInfoBox(
    text: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    tone: StatusTone = StatusTone.Info,
    icon: ImageVector = tone.defaultIcon(),
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val toneColors = tone.colors
    val shape = KrakTheme.shapes.medium
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(toneColors.surface)
            .border(1.dp, toneColors.border, shape)
            .padding(KrakTheme.spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(KrakTheme.spacing.semiMedium),
    ) {
        Icon(icon, contentDescription = null, tint = toneColors.content, modifier = Modifier.size(KrakTheme.iconSize.medium))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.extraSmall)) {
            if (title != null) {
                Text(title, style = KrakTheme.typography.titleSmall, color = toneColors.content)
            }
            Text(text, style = KrakTheme.typography.bodyMedium, color = toneColors.content)
            if (actionText != null && onAction != null) {
                KrakTextButton(text = actionText, onClick = onAction)
            }
        }
    }
}
