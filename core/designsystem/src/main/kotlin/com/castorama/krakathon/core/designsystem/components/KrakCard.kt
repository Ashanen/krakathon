package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.designsystem.theme.KrakTheme
import com.castorama.krakathon.core.designsystem.theme.elevation.Shadows

@Composable
private fun CardContainer(
    modifier: Modifier,
    onClick: (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier,
        content = content,
    )
}

/** Default content card: level-1 shadow (hairline border when [LocalKrakCardSurface] is Flat). Content brings its own padding. */
@Composable
fun KrakCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    CardContainer(modifier.krakCardSurface(KrakTheme.shapes.card), onClick, content)
}

/** Overlay-level shadow, for the one card that must float above the page. */
@Composable
fun KrakElevatedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    CardContainer(modifier.krakCardSurface(KrakTheme.shapes.card, shadow = Shadows.overlay), onClick, content)
}

/** Border-only card with no shadow, regardless of [LocalKrakCardSurface]. */
@Composable
fun KrakOutlinedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = KrakTheme.shapes.card
    CardContainer(
        modifier = modifier
            .clip(shape)
            .background(KrakTheme.colors.surface)
            .border(1.dp, KrakTheme.colors.outlineVariant, shape),
        onClick = onClick,
        content = content,
    )
}
