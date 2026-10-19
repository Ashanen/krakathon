package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.designsystem.theme.KrakTheme
import com.castorama.krakathon.core.designsystem.theme.colors.StatusTone
import com.castorama.krakathon.core.designsystem.theme.colors.colors

/** Read-only status pill; the [tone] decides every colour. */
@Composable
fun KrakBadge(
    text: String,
    modifier: Modifier = Modifier,
    tone: StatusTone = StatusTone.Neutral,
    showDot: Boolean = false,
) {
    val toneColors = tone.colors
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(toneColors.background)
            .padding(horizontal = KrakTheme.spacing.semiMedium, vertical = KrakTheme.spacing.extraSmall + 2.dp),
        horizontalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showDot) {
            Box(Modifier.size(6.dp).clip(CircleShape).background(toneColors.content))
        }
        Text(text = text, style = KrakTheme.typography.labelMedium, color = toneColors.content, maxLines = 1)
    }
}
