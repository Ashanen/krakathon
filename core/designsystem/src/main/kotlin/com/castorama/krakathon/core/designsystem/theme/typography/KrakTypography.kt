package com.castorama.krakathon.core.designsystem.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/** Material 3 type scale: 15 roles in five families. */
@Immutable
data class KrakTypography(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSmall: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
)

private fun style(
    weight: FontWeight,
    size: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit = 0.sp,
) = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = weight,
    fontSize = size,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
)

fun createTypography() = KrakTypography(
    displayLarge = style(FontWeight.Normal, 57.sp, 64.sp, (-0.25).sp),
    displayMedium = style(FontWeight.Normal, 45.sp, 52.sp),
    displaySmall = style(FontWeight.Bold, 32.sp, 40.sp),
    headlineLarge = style(FontWeight.Bold, 32.sp, 40.sp, (-0.32).sp),
    headlineMedium = style(FontWeight.Bold, 28.sp, 36.sp, (-0.28).sp),
    headlineSmall = style(FontWeight.Bold, 24.sp, 32.sp, (-0.24).sp),
    titleLarge = style(FontWeight.SemiBold, 20.sp, 28.sp),
    titleMedium = style(FontWeight.SemiBold, 16.sp, 24.sp),
    titleSmall = style(FontWeight.SemiBold, 14.sp, 20.sp),
    bodyLarge = style(FontWeight.Normal, 16.sp, 24.sp),
    bodyMedium = style(FontWeight.Normal, 14.sp, 21.sp),
    bodySmall = style(FontWeight.Normal, 12.sp, 16.sp),
    labelLarge = style(FontWeight.Medium, 14.sp, 20.sp, 0.1.sp),
    labelMedium = style(FontWeight.Medium, 12.sp, 16.sp, 0.5.sp),
    labelSmall = style(FontWeight.Medium, 11.sp, 16.sp, 0.5.sp),
)

internal fun KrakTypography.toMaterial() = Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall,
)
