package com.castorama.krakathon.feature.catalog.ui.list

import androidx.lifecycle.viewModelScope
import com.castorama.krakathon.core.presentation.flow.launchWith
import com.castorama.krakathon.core.presentation.mvi.BaseUiState
import com.castorama.krakathon.core.presentation.mvi.ScreenState
import com.castorama.krakathon.core.presentation.mvi.SnackbarState
import com.castorama.krakathon.core.presentation.viewmodel.BaseViewModel
import com.castorama.krakathon.core.usecase.invoke
import com.castorama.krakathon.feature.cart.api.ObserveCartCountUseCase
import com.castorama.krakathon.feature.catalog.api.Product
import com.castorama.krakathon.feature.catalog.domain.GetProductsUseCase
import com.castorama.krakathon.feature.catalog.domain.ObserveOrderPlacedUseCase
import com.castorama.krakathon.feature.catalog.ui.R
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

data class CatalogState(
    val products: List<Product> = emptyList(),
    val cartCount: Int = 0,
    val hasLoaded: Boolean = false,
    override val baseUiState: BaseUiState = BaseUiState(),
) : ScreenState<CatalogState> {
    override fun withBaseUiState(baseUiState: BaseUiState) = copy(baseUiState = baseUiState)
}

sealed interface CatalogAction {
    data object Reload : CatalogAction
}

class CatalogViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val observeCartCountUseCase: ObserveCartCountUseCase,
    private val observeOrderPlacedUseCase: ObserveOrderPlacedUseCase,
) : BaseViewModel<CatalogState, CatalogAction>(CatalogState()) {

    init {
        loadProducts()
        observeCartCount()
        observeOrders()
    }

    override fun onAction(action: CatalogAction) {
        when (action) {
            CatalogAction.Reload -> loadProducts()
        }
    }

    private fun loadProducts() {
        getProductsUseCase()
            .onStart { setLoading(true) }
            .onEach { products ->
                setLoading(false)
                updateState { copy(products = products, hasLoaded = true) }
            }
            .launchWith(viewModelScope, onError = { showError(it, retry = ::loadProducts) })
    }

    private fun observeCartCount() {
        observeCartCountUseCase()
            .onEach { count -> updateState { copy(cartCount = count) } }
            .launchWith(viewModelScope, onError = ::showError)
    }

    private fun observeOrders() {
        observeOrderPlacedUseCase()
            .onEach { order -> showSnackbar(SnackbarState.success(R.string.catalog_order_placed, order.itemCount)) }
            .launchWith(viewModelScope, onError = ::showError)
    }
}
