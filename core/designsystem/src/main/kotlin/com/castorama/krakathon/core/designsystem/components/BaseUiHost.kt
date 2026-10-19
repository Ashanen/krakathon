package com.castorama.krakathon.core.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.presentation.mvi.BaseAction
import com.castorama.krakathon.core.presentation.mvi.BaseUiState
import com.castorama.krakathon.core.presentation.mvi.ErrorDialogState
import com.castorama.krakathon.core.presentation.mvi.InfoDialogState
import com.castorama.krakathon.core.presentation.mvi.SnackbarState
import kotlinx.coroutines.withTimeoutOrNull
import com.castorama.krakathon.core.designsystem.R
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/**
 * Renders everything a screen's [BaseUiState] can ask for: error dialog, info dialog, snackbar
 * (shown on [snackbarHostState], restarted by every new `token`) and a blocking loading overlay.
 * Give it the full screen size so the overlay covers the content.
 */
@Composable
fun BaseUiHost(
    baseUiState: BaseUiState,
    onBaseAction: (BaseAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    ErrorDialog(baseUiState.errorDialogState, onBaseAction)
    InfoDialog(baseUiState.infoDialogState, onBaseAction)
    SnackbarEffect(baseUiState.snackbarState, snackbarHostState, onBaseAction)
    LoadingOverlay(baseUiState.isLoading, modifier)
}

@Composable
private fun ErrorDialog(state: ErrorDialogState, onBaseAction: (BaseAction) -> Unit) {
    if (!state.isVisible) return
    KrakConfirmDialog(
        title = stringResource(state.title ?: R.string.ds_error_title),
        message = stringResource(state.message ?: R.string.ds_error_generic),
        confirmText = stringResource(if (state.canRetry) R.string.ds_retry else R.string.ds_ok),
        onConfirm = { onBaseAction(if (state.canRetry) BaseAction.Retry else BaseAction.DismissError) },
        onDismiss = { onBaseAction(BaseAction.DismissError) },
        dismissText = if (state.canRetry) stringResource(R.string.ds_cancel) else null,
        icon = Icons.Outlined.ErrorOutline,
    )
}

@Composable
private fun InfoDialog(state: InfoDialogState, onBaseAction: (BaseAction) -> Unit) {
    if (!state.isVisible) return
    KrakConfirmDialog(
        title = state.title?.let { stringResource(it) },
        message = state.message?.let { stringResource(it) }.orEmpty(),
        confirmText = stringResource(state.primaryButtonText ?: R.string.ds_ok),
        onConfirm = { onBaseAction(BaseAction.DismissInfo) },
        onDismiss = { onBaseAction(BaseAction.DismissInfo) },
        dismissText = null,
    )
}

@Composable
private fun SnackbarEffect(
    state: SnackbarState,
    hostState: SnackbarHostState,
    onBaseAction: (BaseAction) -> Unit,
) {
    val message = state.message?.let { stringResource(it, *state.formatArgs.toTypedArray()) }
    val actionLabel = state.actionLabel?.let { stringResource(it) }
    val currentOnAction by rememberUpdatedState(onBaseAction)
    LaunchedEffect(state.token, state.isVisible) {
        if (!state.isVisible || message == null) return@LaunchedEffect
        val result = withTimeoutOrNull(state.duration.milliseconds) {
            hostState.showSnackbar(KrakSnackbarVisuals(message, actionLabel, state.type))
        }
        currentOnAction(
            if (result == SnackbarResult.ActionPerformed) BaseAction.PerformSnackbarAction else BaseAction.DismissSnackbar,
        )
    }
}

@Composable
private fun LoadingOverlay(isLoading: Boolean, modifier: Modifier) {
    AnimatedVisibility(visible = isLoading, modifier = modifier, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(KrakTheme.colors.scrim)
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = {}),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier.size(72.dp).clip(KrakTheme.shapes.large).background(KrakTheme.colors.surface),
                contentAlignment = Alignment.Center,
            ) {
                LoadingIndicator(size = 32.dp, strokeWidth = 3.dp)
            }
        }
    }
}
