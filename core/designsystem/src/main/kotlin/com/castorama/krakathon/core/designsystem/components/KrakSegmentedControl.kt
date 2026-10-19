package com.castorama.krakathon.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.designsystem.theme.KrakTheme
import com.castorama.krakathon.core.designsystem.theme.motion.Motion

@Immutable
data class KrakSegment<T>(val value: T, val label: String)

private const val DisabledAlpha = 0.4f

/** Equal-width pill selector for 2-4 mutually exclusive options. */
@Composable
fun <T> KrakSegmentedControl(
    options: List<KrakSegment<T>>,
    selectedValue: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(KrakTheme.dimensions.segmentedControlHeight)
            .alpha(if (enabled) 1f else DisabledAlpha)
            .clip(KrakTheme.shapes.full)
            .background(KrakTheme.colors.surfaceVariant)
            .padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        options.forEach { option ->
            val selected = option.value == selectedValue
            val background by animateColorAsState(
                targetValue = if (selected) KrakTheme.colors.surface else Color.Transparent,
                animationSpec = tween(Motion.Duration.short),
                label = "segmentBackground",
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(KrakTheme.shapes.full)
                    .background(background)
                    .selectable(selected = selected, enabled = enabled, role = Role.RadioButton) { onSelected(option.value) }
                    .padding(horizontal = KrakTheme.spacing.small),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option.label,
                    style = KrakTheme.typography.labelLarge,
                    color = if (selected) KrakTheme.colors.onSurface else KrakTheme.colors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
