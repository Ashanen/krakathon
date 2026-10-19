package com.castorama.krakathon.core.designsystem.theme.colors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Closed status set: a status keeps one tone on every screen, so chips, badges and banners never disagree. */
enum class StatusTone { Info, Success, Warning, Error, Neutral }

@Immutable
data class StatusToneColors(
    val background: Color,
    val content: Color,
    val surface: Color,
    val border: Color,
)

val StatusTone.colors: StatusToneColors
    @Composable @ReadOnlyComposable
    get() = if (KrakTheme.colors.isDark) DarkTones.getValue(this) else LightTones.getValue(this)

private val LightTones = mapOf(
    StatusTone.Info to StatusToneColors(ColorPalette.Blue100, ColorPalette.Blue900, ColorPalette.Blue50, ColorPalette.Blue100),
    StatusTone.Success to StatusToneColors(ColorPalette.Green100, ColorPalette.Green900, ColorPalette.Green50, ColorPalette.Green100),
    StatusTone.Warning to StatusToneColors(ColorPalette.Amber100, ColorPalette.Amber900, ColorPalette.Amber50, ColorPalette.Amber100),
    StatusTone.Error to StatusToneColors(ColorPalette.Red100, ColorPalette.Red900, ColorPalette.Red50, ColorPalette.Red100),
    StatusTone.Neutral to StatusToneColors(ColorPalette.Neutral20, ColorPalette.Neutral70, ColorPalette.Neutral10, ColorPalette.Neutral20),
)

private val DarkTones = mapOf(
    StatusTone.Info to StatusToneColors(ColorPalette.Blue900, ColorPalette.Blue100, ColorPalette.Blue900.copy(alpha = 0.4f), ColorPalette.Blue900),
    StatusTone.Success to StatusToneColors(ColorPalette.Green900, ColorPalette.Green100, ColorPalette.Green900.copy(alpha = 0.4f), ColorPalette.Green900),
    StatusTone.Warning to StatusToneColors(ColorPalette.Amber900, ColorPalette.Amber100, ColorPalette.Amber900.copy(alpha = 0.4f), ColorPalette.Amber900),
    StatusTone.Error to StatusToneColors(ColorPalette.Red900, ColorPalette.Red100, ColorPalette.Red900.copy(alpha = 0.4f), ColorPalette.Red900),
    StatusTone.Neutral to StatusToneColors(ColorPalette.Neutral70, ColorPalette.Neutral10, ColorPalette.Neutral80, ColorPalette.Neutral70),
)
