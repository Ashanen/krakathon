package com.castorama.krakathon.feature.catalog.ui.list

import androidx.lifecycle.viewModelScope
import com.castorama.krakathon.core.presentation.flow.launchWith
import com.castorama.krakathon.core.presentation.mvi.BaseUiState
import com.castorama.krakathon.core.presentation.mvi.Loadable
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

data class CatalogState(
    val products: Loadable<List<Product>> = Loadable.Loading,
    val cartCount: Int = 0,
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
        getProductsUseCase().launchLoadable { copy(products = it) }
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
