package com.castorama.krakathon.core.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.castorama.krakathon.core.presentation.mvi.Loadable

/**
 * Renders a [Loadable]: [loading] skeleton, inline [KrakErrorState] with retry, or [content].
 * Use it for every screen's main data so no state is ever left unrendered.
 */
@Composable
fun <T> LoadableContent(
    loadable: Loadable<T>,
    onRetry: () -> Unit,
    loading: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    when (loadable) {
        Loadable.Loading -> loading()
        is Loadable.Failed -> KrakErrorState(
            message = stringResource(loadable.message),
            onRetry = onRetry,
            modifier = modifier,
        )
        is Loadable.Ready -> content(loadable.value)
    }
}
