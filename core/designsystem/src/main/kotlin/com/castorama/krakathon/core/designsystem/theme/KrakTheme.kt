package com.castorama.krakathon.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.castorama.krakathon.core.designsystem.theme.colors.DarkDesignColors
import com.castorama.krakathon.core.designsystem.theme.colors.DesignColors
import com.castorama.krakathon.core.designsystem.theme.colors.LightDesignColors
import com.castorama.krakathon.core.designsystem.theme.dimensions.Dimensions
import com.castorama.krakathon.core.designsystem.theme.elevation.Elevation
import com.castorama.krakathon.core.designsystem.theme.icons.IconSize
import com.castorama.krakathon.core.designsystem.theme.motion.Motion
import com.castorama.krakathon.core.designsystem.theme.shapes.KrakShapes
import com.castorama.krakathon.core.designsystem.theme.spacing.Spacing
import com.castorama.krakathon.core.designsystem.theme.typography.KrakTypography
import com.castorama.krakathon.core.designsystem.theme.typography.createTypography
import com.castorama.krakathon.core.designsystem.theme.typography.toMaterial

private val LocalDesignColors = staticCompositionLocalOf<DesignColors> { error("KrakTheme not provided") }
private val LocalKrakTypography = staticCompositionLocalOf<KrakTypography> { error("KrakTheme not provided") }

/** Accessor for every design token: `KrakTheme.colors.primary`, `KrakTheme.spacing.medium`. */
object KrakTheme {
    val colors: DesignColors
        @Composable @ReadOnlyComposable get() = LocalDesignColors.current
    val typography: KrakTypography
        @Composable @ReadOnlyComposable get() = LocalKrakTypography.current
    val spacing: Spacing get() = Spacing
    val shapes: KrakShapes get() = KrakShapes
    val elevation: Elevation get() = Elevation
    val dimensions: Dimensions get() = Dimensions
    val iconSize: IconSize get() = IconSize
    val motion: Motion get() = Motion
}

@Composable
fun KrakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkDesignColors else LightDesignColors
    val typography = remember { createTypography() }
    val scheme = if (darkTheme) colors.toDarkScheme() else colors.toLightScheme()

    MaterialTheme(colorScheme = scheme, typography = typography.toMaterial()) {
        CompositionLocalProvider(
            LocalDesignColors provides colors,
            LocalKrakTypography provides typography,
            content = content,
        )
    }
}

private fun DesignColors.toLightScheme() = lightColorScheme(
    primary = primary, onPrimary = onPrimary,
    primaryContainer = primaryContainer, onPrimaryContainer = onPrimaryContainer,
    secondary = secondary, onSecondary = onSecondary,
    secondaryContainer = secondaryContainer, onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiary, onTertiary = onTertiary,
    tertiaryContainer = tertiaryContainer, onTertiaryContainer = onTertiaryContainer,
    error = error, onError = onError, errorContainer = errorContainer, onErrorContainer = onErrorContainer,
    surface = surface, onSurface = onSurface, surfaceVariant = surfaceVariant, onSurfaceVariant = onSurfaceVariant,
    background = background, onBackground = onBackground,
    outline = outline, outlineVariant = outlineVariant,
    inverseSurface = inverseSurface, inverseOnSurface = inverseOnSurface, inversePrimary = inversePrimary,
    scrim = scrim, surfaceTint = surfaceTint,
    surfaceContainerLowest = surfaceContainerLowest, surfaceContainerLow = surfaceContainerLow,
    surfaceContainer = surfaceContainer, surfaceContainerHigh = surfaceContainerHigh,
    surfaceContainerHighest = surfaceContainerHighest, surfaceBright = surfaceBright, surfaceDim = surfaceDim,
)

private fun DesignColors.toDarkScheme() = darkColorScheme(
    primary = primary, onPrimary = onPrimary,
    primaryContainer = primaryContainer, onPrimaryContainer = onPrimaryContainer,
    secondary = secondary, onSecondary = onSecondary,
    secondaryContainer = secondaryContainer, onSecondaryContainer = onSecondaryContainer,
    tertiary = tertiary, onTertiary = onTertiary,
    tertiaryContainer = tertiaryContainer, onTertiaryContainer = onTertiaryContainer,
    error = error, onError = onError, errorContainer = errorContainer, onErrorContainer = onErrorContainer,
    surface = surface, onSurface = onSurface, surfaceVariant = surfaceVariant, onSurfaceVariant = onSurfaceVariant,
    background = background, onBackground = onBackground,
    outline = outline, outlineVariant = outlineVariant,
    inverseSurface = inverseSurface, inverseOnSurface = inverseOnSurface, inversePrimary = inversePrimary,
    scrim = scrim, surfaceTint = surfaceTint,
    surfaceContainerLowest = surfaceContainerLowest, surfaceContainerLow = surfaceContainerLow,
    surfaceContainer = surfaceContainer, surfaceContainerHigh = surfaceContainerHigh,
    surfaceContainerHighest = surfaceContainerHighest, surfaceBright = surfaceBright, surfaceDim = surfaceDim,
)
