package com.castorama.krakathon.core.designsystem.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.castorama.krakathon.core.designsystem.R
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Full-area failure state; shows a retry button when [onRetry] is set. */
@Composable
fun KrakErrorState(
    message: String,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.ds_error_title),
    onRetry: (() -> Unit)? = null,
) {
    StateLayout(
        icon = Icons.Outlined.ErrorOutline,
        iconTint = KrakTheme.colors.onErrorContainer,
        iconBackground = KrakTheme.colors.errorContainer,
        title = title,
        description = message,
        actionText = if (onRetry != null) stringResource(R.string.ds_retry) else null,
        onAction = onRetry,
        modifier = modifier,
    )
}
