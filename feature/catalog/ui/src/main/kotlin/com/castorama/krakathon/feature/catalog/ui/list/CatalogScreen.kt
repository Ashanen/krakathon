package com.castorama.krakathon.feature.catalog.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.castorama.krakathon.core.designsystem.components.KrakBadge
import com.castorama.krakathon.core.designsystem.components.KrakCard
import com.castorama.krakathon.core.designsystem.components.KrakEmptyState
import com.castorama.krakathon.core.designsystem.components.KrakIconButton
import com.castorama.krakathon.core.designsystem.components.KrakScreen
import com.castorama.krakathon.core.designsystem.components.SkeletonBlock
import com.castorama.krakathon.core.designsystem.theme.KrakTheme
import com.castorama.krakathon.core.designsystem.theme.colors.StatusTone
import com.castorama.krakathon.core.presentation.format.formatPrice
import com.castorama.krakathon.core.presentation.mvi.BaseAction
import com.castorama.krakathon.feature.catalog.api.Product
import com.castorama.krakathon.feature.catalog.ui.R
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CatalogEntry(
    onProductClick: (String) -> Unit,
    onCartClick: () -> Unit,
    viewModel: CatalogViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CatalogScreen(
        state = state,
        onAction = viewModel::onAction,
        onBaseAction = viewModel::onBaseAction,
        onProductClick = onProductClick,
        onCartClick = onCartClick,
    )
}

@Composable
fun CatalogScreen(
    state: CatalogState,
    onAction: (CatalogAction) -> Unit,
    onBaseAction: (BaseAction) -> Unit,
    onProductClick: (String) -> Unit,
    onCartClick: () -> Unit,
) {
    KrakScreen(
        title = stringResource(R.string.catalog_title),
        baseUiState = state.baseUiState.copy(isLoading = false),
        onBaseAction = onBaseAction,
        actions = { CartAction(state.cartCount, onCartClick) },
    ) { padding ->
        when {
            !state.hasLoaded -> CatalogSkeleton(padding)
            state.products.isEmpty() -> KrakEmptyState(
                title = stringResource(R.string.catalog_empty_title),
                description = stringResource(R.string.catalog_empty_description),
                actionText = stringResource(R.string.catalog_reload),
                onAction = { onAction(CatalogAction.Reload) },
                modifier = Modifier.padding(padding),
            )
            else -> LazyColumn(
                contentPadding = listPadding(padding),
                verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.semiMedium),
            ) {
                items(state.products, key = { it.id }) { product ->
                    ProductCard(product, onClick = { onProductClick(product.id) })
                }
            }
        }
    }
}

@Composable
private fun CartAction(cartCount: Int, onCartClick: () -> Unit) {
    Box(contentAlignment = Alignment.TopEnd) {
        KrakIconButton(
            icon = Icons.Outlined.ShoppingCart,
            contentDescription = stringResource(R.string.catalog_open_cart),
            onClick = onCartClick,
        )
        if (cartCount > 0) KrakBadge(text = cartCount.toString(), tone = StatusTone.Info)
    }
}

@Composable
private fun ProductCard(product: Product, onClick: () -> Unit) {
    KrakCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(
            modifier = Modifier.padding(KrakTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.extraSmall),
        ) {
            Text(product.category, style = KrakTheme.typography.labelMedium, color = KrakTheme.colors.onSurfaceVariant)
            Text(product.name, style = KrakTheme.typography.titleMedium, maxLines = 1)
            Text(formatPrice(product.priceInGrosze), style = KrakTheme.typography.titleSmall, color = KrakTheme.colors.primary)
        }
    }
}

/** 1:1 ghost of [ProductCard] rows. */
@Composable
private fun CatalogSkeleton(padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = listPadding(padding),
        verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.semiMedium),
        userScrollEnabled = false,
    ) {
        items(SKELETON_ROWS, key = { it }) {
            SkeletonBlock(Modifier.fillMaxWidth().height(96.dp), shape = KrakTheme.shapes.card)
        }
    }
}

@Composable
private fun listPadding(padding: PaddingValues) = PaddingValues(
    start = KrakTheme.spacing.screenPadding,
    end = KrakTheme.spacing.screenPadding,
    top = padding.calculateTopPadding() + KrakTheme.spacing.small,
    bottom = padding.calculateBottomPadding() + KrakTheme.spacing.large,
)

private val SKELETON_ROWS = List(6) { it }
