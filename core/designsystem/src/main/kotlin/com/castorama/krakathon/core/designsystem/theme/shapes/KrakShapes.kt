package com.castorama.krakathon.core.designsystem.theme.shapes

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
object KrakShapes {
    val extraSmall: Shape = RoundedCornerShape(4.dp)
    val small: Shape = RoundedCornerShape(8.dp)
    val medium: Shape = RoundedCornerShape(12.dp)
    val large: Shape = RoundedCornerShape(16.dp)
    val card: Shape = RoundedCornerShape(20.dp)
    val extraLarge: Shape = RoundedCornerShape(28.dp)
    val full: Shape = CircleShape
}
