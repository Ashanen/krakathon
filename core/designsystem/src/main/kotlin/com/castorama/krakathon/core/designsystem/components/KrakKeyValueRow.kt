package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Label takes at most 40% of the row; the value fills the rest and stays end-aligned, wrapping instead of overlapping. */
@Composable
fun KrakKeyValueRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    dimmed: Boolean = false,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            style = KrakTheme.typography.bodyMedium,
            color = KrakTheme.colors.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(LabelWidthFraction),
        )
        Text(
            text = value,
            style = KrakTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = if (dimmed) KrakTheme.colors.onSurfaceVariant else KrakTheme.colors.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}

private const val LabelWidthFraction = 0.4f
