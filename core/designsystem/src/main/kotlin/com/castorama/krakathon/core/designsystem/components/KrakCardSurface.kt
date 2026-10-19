package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.designsystem.theme.KrakTheme
import com.castorama.krakathon.core.designsystem.theme.elevation.ShadowSpec
import com.castorama.krakathon.core.designsystem.theme.elevation.Shadows
import com.castorama.krakathon.core.designsystem.theme.elevation.krakShadow

/** Elevated cards separate with a shadow, flat ones with a hairline border. */
enum class KrakCardSurface { Elevated, Flat }

val LocalKrakCardSurface = staticCompositionLocalOf { KrakCardSurface.Elevated }

@Composable
fun Modifier.krakCardSurface(
    shape: Shape,
    shadow: ShadowSpec = Shadows.card,
    backgroundColor: Color = KrakTheme.colors.surface,
    borderColor: Color = KrakTheme.colors.outlineVariant,
): Modifier = when (LocalKrakCardSurface.current) {
    KrakCardSurface.Elevated -> this
        .krakShadow(shadow, shape)
        .clip(shape)
        .background(backgroundColor)
    KrakCardSurface.Flat -> this
        .clip(shape)
        .background(backgroundColor)
        .border(1.dp, borderColor, shape)
}
