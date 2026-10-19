package com.castorama.krakathon.feature.catalog.ui.detail

import androidx.lifecycle.viewModelScope
import com.castorama.krakathon.core.presentation.flow.launchWith
import com.castorama.krakathon.core.presentation.mvi.BaseUiState
import com.castorama.krakathon.core.presentation.mvi.ScreenState
import com.castorama.krakathon.core.presentation.mvi.SnackbarState
import com.castorama.krakathon.core.presentation.viewmodel.BaseViewModel
import com.castorama.krakathon.feature.cart.api.AddToCartUseCase
import com.castorama.krakathon.feature.catalog.api.CatalogException
import com.castorama.krakathon.feature.catalog.api.GetProductUseCase
import com.castorama.krakathon.feature.catalog.api.Product
import com.castorama.krakathon.feature.catalog.ui.R
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

data class ProductDetailState(
    val product: Product? = null,
    val isAdding: Boolean = false,
    override val baseUiState: BaseUiState = BaseUiState(),
) : ScreenState<ProductDetailState> {
    override fun withBaseUiState(baseUiState: BaseUiState) = copy(baseUiState = baseUiState)
}

sealed interface ProductDetailAction {
    data object AddToCart : ProductDetailAction
}

class ProductDetailViewModel(
    private val productId: String,
    private val getProductUseCase: GetProductUseCase,
    private val addToCartUseCase: AddToCartUseCase,
) : BaseViewModel<ProductDetailState, ProductDetailAction>(ProductDetailState()) {

    init {
        loadProduct()
    }

    override fun onAction(action: ProductDetailAction) {
        when (action) {
            ProductDetailAction.AddToCart -> addToCart()
        }
    }

    override fun errorMessage(error: Throwable): Int = when (error) {
        is CatalogException.ProductNotFound -> R.string.catalog_error_product_not_found
        else -> super.errorMessage(error)
    }

    private fun loadProduct() {
        getProductUseCase(productId)
            .onEach { product -> updateState { copy(product = product) } }
            .launchWith(viewModelScope, onError = { showError(it, retry = ::loadProduct) })
    }

    private fun addToCart() {
        if (currentState.isAdding) return
        addToCartUseCase(productId)
            .onStart { updateState { copy(isAdding = true) } }
            .onEach { showSnackbar(SnackbarState.success(R.string.catalog_added_to_cart)) }
            .onCompletion { updateState { copy(isAdding = false) } }
            .launchWith(viewModelScope, onError = ::showError)
    }
}
