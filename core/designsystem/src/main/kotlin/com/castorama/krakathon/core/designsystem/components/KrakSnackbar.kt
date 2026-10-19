package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.castorama.krakathon.core.presentation.mvi.SnackbarType
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Carries the [SnackbarType] to [KrakSnackbar]; display time is owned by [BaseUiHost], hence Indefinite. */
internal class KrakSnackbarVisuals(
    override val message: String,
    override val actionLabel: String?,
    val type: SnackbarType,
) : SnackbarVisuals {
    override val withDismissAction: Boolean = false
    override val duration = androidx.compose.material3.SnackbarDuration.Indefinite
}

@Composable
internal fun KrakSnackbar(data: SnackbarData, modifier: Modifier = Modifier) {
    val colors = KrakTheme.colors
    val type = (data.visuals as? KrakSnackbarVisuals)?.type ?: SnackbarType.Info
    val container = when (type) {
        SnackbarType.Success -> colors.success
        SnackbarType.Error -> colors.error
        SnackbarType.Info -> colors.inverseSurface
    }
    val content = when (type) {
        SnackbarType.Success -> colors.onSuccess
        SnackbarType.Error -> colors.onError
        SnackbarType.Info -> colors.inverseOnSurface
    }
    Snackbar(
        snackbarData = data,
        modifier = modifier.padding(KrakTheme.spacing.medium),
        shape = KrakTheme.shapes.medium,
        containerColor = container,
        contentColor = content,
        actionColor = content,
    )
}
