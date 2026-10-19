package com.castorama.krakathon.core.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Diagonal band sweeping across the node; every loading placeholder must shimmer, never sit static. */
@Composable
fun Modifier.shimmerEffect(
    base: Color = KrakTheme.colors.surfaceContainer,
    highlight: Color = KrakTheme.colors.surfaceContainerHighest,
): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 1200, easing = LinearEasing), RepeatMode.Restart),
        label = "shimmerProgress",
    )
    return drawWithCache {
        val brushColors = listOf(base, highlight, base)
        onDrawBehind {
            val shift = (progress * 3f - 1f) * size.width
            drawRect(
                Brush.linearGradient(
                    colors = brushColors,
                    start = Offset(shift - size.width, 0f),
                    end = Offset(shift, size.height),
                ),
            )
        }
    }
}

@Composable
fun SkeletonBlock(
    modifier: Modifier = Modifier,
    shape: Shape = KrakTheme.shapes.medium,
) {
    Box(modifier = modifier.clip(shape).shimmerEffect())
}

@Composable
fun SkeletonLine(
    modifier: Modifier = Modifier,
    height: Dp = 14.dp,
) {
    SkeletonBlock(modifier = modifier.fillMaxWidth().height(height), shape = KrakTheme.shapes.small)
}
