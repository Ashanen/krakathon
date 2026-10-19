package com.castorama.krakathon.core.designsystem.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.designsystem.R
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = KrakTheme.colors.primary,
    strokeWidth: Dp = 4.dp,
) {
    val description = stringResource(R.string.ds_loading)
    CircularProgressIndicator(
        modifier = modifier.size(size).semantics { contentDescription = description },
        color = color,
        strokeWidth = strokeWidth,
    )
}
