package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.castorama.krakathon.core.designsystem.R
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Flat top bar that owns the status-bar inset; pass it as the Scaffold `topBar`. */
@Composable
fun KrakTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(KrakTheme.colors.background)
            .statusBarsPadding()
            .heightIn(min = KrakTheme.dimensions.topBarHeight)
            .padding(horizontal = KrakTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            KrakIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.ds_back),
                onClick = onBack,
            )
        } else {
            Spacer(Modifier.width(KrakTheme.spacing.small))
        }
        Text(
            text = title,
            style = KrakTheme.typography.titleLarge,
            color = KrakTheme.colors.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f).padding(horizontal = KrakTheme.spacing.small),
        )
        actions()
    }
}
