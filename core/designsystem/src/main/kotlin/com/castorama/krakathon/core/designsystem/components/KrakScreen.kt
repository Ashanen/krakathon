package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.castorama.krakathon.core.presentation.mvi.BaseAction
import com.castorama.krakathon.core.presentation.mvi.BaseUiState
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Standard screen shell: top bar, snackbar host and the dialogs/loading overlay driven by [baseUiState]. */
@Composable
fun KrakScreen(
    title: String,
    baseUiState: BaseUiState,
    onBaseAction: (BaseAction) -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { KrakTopBar(title = title, onBack = onBack, actions = actions) },
            snackbarHost = { SnackbarHost(snackbarHostState) { KrakSnackbar(it) } },
            containerColor = KrakTheme.colors.background,
            content = content,
        )
        BaseUiHost(
            baseUiState = baseUiState,
            onBaseAction = onBaseAction,
            snackbarHostState = snackbarHostState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
