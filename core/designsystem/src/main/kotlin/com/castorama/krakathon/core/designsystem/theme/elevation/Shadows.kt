package com.castorama.krakathon.core.designsystem.theme.elevation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class ShadowSpec(val elevation: Dp, val color: Color)

/** Alphas are boosted because the platform multiplies the shadow colour by its own spot alpha. */
@Immutable
object Shadows {
    val card = ShadowSpec(elevation = 10.dp, color = Color.Black.copy(alpha = 0.45f))
    val raised = ShadowSpec(elevation = 2.dp, color = Color.Black.copy(alpha = 0.5f))
    val overlay = ShadowSpec(elevation = 14.dp, color = Color.Black.copy(alpha = 0.6f))
}

fun Modifier.krakShadow(spec: ShadowSpec, shape: Shape): Modifier =
    shadow(elevation = spec.elevation, shape = shape, ambientColor = spec.color, spotColor = spec.color)

/** Room a shadowed surface needs inside any clipping ancestor (lazy lists, scroll containers, clip). */
val ShadowBleed: Dp = 24.dp

val ShadowBleedHorizontalPadding: PaddingValues = PaddingValues(horizontal = ShadowBleed)
