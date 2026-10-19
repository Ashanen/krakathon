package com.castorama.krakathon.feature.catalog.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.castorama.krakathon.core.designsystem.components.KrakButton
import com.castorama.krakathon.core.designsystem.components.KrakScreen
import com.castorama.krakathon.core.designsystem.components.LoadableContent
import com.castorama.krakathon.core.designsystem.components.SkeletonBlock
import com.castorama.krakathon.core.designsystem.components.SkeletonLine
import com.castorama.krakathon.core.designsystem.theme.KrakTheme
import com.castorama.krakathon.core.presentation.format.formatPrice
import com.castorama.krakathon.core.presentation.mvi.BaseAction
import com.castorama.krakathon.feature.catalog.api.Product
import com.castorama.krakathon.feature.catalog.ui.R
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailEntry(
    productId: String,
    onBack: () -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel { parametersOf(productId) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ProductDetailScreen(state, viewModel::onAction, viewModel::onBaseAction, onBack)
}

@Composable
fun ProductDetailScreen(
    state: ProductDetailState,
    onAction: (ProductDetailAction) -> Unit,
    onBaseAction: (BaseAction) -> Unit,
    onBack: () -> Unit,
) {
    KrakScreen(
        title = stringResource(R.string.catalog_detail_title),
        baseUiState = state.baseUiState,
        onBaseAction = onBaseAction,
        onBack = onBack,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(KrakTheme.spacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.medium),
        ) {
            LoadableContent(
                loadable = state.product,
                onRetry = { onAction(ProductDetailAction.Retry) },
                loading = {
                    DetailSkeleton()
                    SkeletonBlock(Modifier.fillMaxWidth().height(KrakTheme.dimensions.buttonHeight), shape = KrakTheme.shapes.large)
                },
            ) { product ->
                ProductDetails(product)
                KrakButton(
                    text = stringResource(R.string.catalog_add_to_cart),
                    onClick = { onAction(ProductDetailAction.AddToCart) },
                    loading = state.isAdding,
                )
            }
        }
    }
}

@Composable
private fun ProductDetails(product: Product) {
    Column(verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small)) {
        Text(product.category, style = KrakTheme.typography.labelLarge, color = KrakTheme.colors.onSurfaceVariant)
        Text(product.name, style = KrakTheme.typography.headlineSmall)
        Text(formatPrice(product.priceInGrosze), style = KrakTheme.typography.titleLarge, color = KrakTheme.colors.primary)
        Text(product.description, style = KrakTheme.typography.bodyLarge)
    }
}

@Composable
private fun DetailSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small)) {
        SkeletonLine(Modifier.fillMaxWidth(0.3f))
        SkeletonBlock(Modifier.fillMaxWidth(0.8f).height(32.dp))
        SkeletonLine(Modifier.fillMaxWidth(0.4f), height = 24.dp)
        SkeletonLine()
        SkeletonLine(Modifier.fillMaxWidth(0.7f))
    }
}
