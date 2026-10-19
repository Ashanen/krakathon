package com.castorama.krakathon.feature.cart.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.castorama.krakathon.core.designsystem.components.KrakButton
import com.castorama.krakathon.core.designsystem.components.KrakCard
import com.castorama.krakathon.core.designsystem.components.KrakEmptyState
import com.castorama.krakathon.core.designsystem.components.KrakIconButton
import com.castorama.krakathon.core.designsystem.components.KrakInfoBox
import com.castorama.krakathon.core.designsystem.components.KrakKeyValueRow
import com.castorama.krakathon.core.designsystem.components.LoadableContent
import com.castorama.krakathon.core.designsystem.components.KrakScreen
import com.castorama.krakathon.core.designsystem.components.SkeletonBlock
import com.castorama.krakathon.core.designsystem.theme.KrakTheme
import com.castorama.krakathon.core.designsystem.theme.colors.StatusTone
import com.castorama.krakathon.core.presentation.format.formatPrice
import com.castorama.krakathon.core.presentation.mvi.BaseAction
import com.castorama.krakathon.feature.cart.domain.model.Cart
import com.castorama.krakathon.feature.cart.domain.model.CartItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CartEntry(
    onBack: () -> Unit,
    onCheckedOut: () -> Unit,
    viewModel: CartViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.checkedOut) { if (state.checkedOut) onCheckedOut() }
    CartScreen(state, viewModel::onAction, viewModel::onBaseAction, onBack)
}

@Composable
fun CartScreen(
    state: CartState,
    onAction: (CartAction) -> Unit,
    onBaseAction: (BaseAction) -> Unit,
    onBack: () -> Unit,
) {
    KrakScreen(
        title = stringResource(R.string.cart_title),
        baseUiState = state.baseUiState,
        onBaseAction = onBaseAction,
        onBack = onBack,
    ) { padding ->
        LoadableContent(
            loadable = state.cart,
            onRetry = { onAction(CartAction.Retry) },
            loading = { CartSkeleton(padding) },
            modifier = Modifier.padding(padding),
        ) { cart ->
            if (cart.isEmpty) {
                KrakEmptyState(
                    title = stringResource(R.string.cart_empty_title),
                    description = stringResource(R.string.cart_empty_description),
                    icon = Icons.Outlined.ShoppingCart,
                    actionText = stringResource(R.string.cart_browse),
                    onAction = onBack,
                    modifier = Modifier.padding(padding),
                )
            } else {
                CartContent(cart, state.isCheckingOut, onAction, padding)
            }
        }
    }
}

/** 1:1 ghost of one [CartItemCard] row. */
@Composable
private fun CartSkeleton(padding: PaddingValues) {
    SkeletonBlock(
        Modifier.padding(padding).padding(KrakTheme.spacing.screenPadding).fillMaxWidth().height(88.dp),
        shape = KrakTheme.shapes.card,
    )
}

@Composable
private fun CartContent(cart: Cart, isCheckingOut: Boolean, onAction: (CartAction) -> Unit, padding: PaddingValues) {
    Column(Modifier.fillMaxSize().padding(padding)) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(KrakTheme.spacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.semiMedium),
        ) {
            if (cart.unavailableIds.isNotEmpty()) {
                item(key = "unavailable") {
                    KrakInfoBox(
                        text = pluralStringResource(R.plurals.cart_unavailable, cart.unavailableIds.size, cart.unavailableIds.size),
                        tone = StatusTone.Warning,
                        actionText = stringResource(R.string.cart_remove_unavailable),
                        onAction = { onAction(CartAction.RemoveUnavailable) },
                    )
                }
            }
            items(cart.items, key = { it.product.id }) { item -> CartItemCard(item, enabled = !isCheckingOut, onAction) }
        }
        Column(
            modifier = Modifier.navigationBarsPadding().padding(KrakTheme.spacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.semiMedium),
        ) {
            KrakKeyValueRow(label = stringResource(R.string.cart_total), value = formatPrice(cart.totalInGrosze))
            KrakButton(
                text = stringResource(R.string.cart_checkout),
                onClick = { onAction(CartAction.Checkout) },
                enabled = cart.unavailableIds.isEmpty(),
                loading = isCheckingOut,
            )
        }
    }
}

@Composable
private fun CartItemCard(item: CartItem, enabled: Boolean, onAction: (CartAction) -> Unit) {
    KrakCard(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(KrakTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small),
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.extraSmall)) {
                Text(item.product.name, style = KrakTheme.typography.titleMedium, maxLines = 1)
                Text(formatPrice(item.totalInGrosze), style = KrakTheme.typography.bodyMedium, color = KrakTheme.colors.primary)
            }
            KrakIconButton(
                icon = Icons.Outlined.Remove,
                contentDescription = stringResource(R.string.cart_decrease),
                onClick = { onAction(CartAction.Decrease(item.product.id)) },
                enabled = enabled,
            )
            Text(stringResource(R.string.cart_quantity, item.quantity), style = KrakTheme.typography.titleSmall)
            KrakIconButton(
                icon = Icons.Outlined.Add,
                contentDescription = stringResource(R.string.cart_increase),
                onClick = { onAction(CartAction.Increase(item.product.id)) },
                enabled = enabled,
            )
        }
    }
}
