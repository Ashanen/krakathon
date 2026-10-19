package com.castorama.krakathon.feature.cart.ui

import androidx.lifecycle.viewModelScope
import com.castorama.krakathon.core.presentation.flow.launchWith
import com.castorama.krakathon.core.presentation.mvi.BaseUiState
import com.castorama.krakathon.core.presentation.mvi.ScreenState
import com.castorama.krakathon.core.presentation.viewmodel.BaseViewModel
import com.castorama.krakathon.core.usecase.invoke
import com.castorama.krakathon.feature.cart.api.AddToCartUseCase
import com.castorama.krakathon.feature.cart.domain.CartException
import com.castorama.krakathon.feature.cart.domain.CheckoutUseCase
import com.castorama.krakathon.feature.cart.domain.ObserveCartUseCase
import com.castorama.krakathon.feature.cart.domain.RemoveFromCartUseCase
import com.castorama.krakathon.feature.cart.domain.model.Cart
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

data class CartState(
    val cart: Cart? = null,
    val isCheckingOut: Boolean = false,
    val checkedOut: Boolean = false,
    override val baseUiState: BaseUiState = BaseUiState(),
) : ScreenState<CartState> {
    override fun withBaseUiState(baseUiState: BaseUiState) = copy(baseUiState = baseUiState)
}

sealed interface CartAction {
    data class Increase(val productId: String) : CartAction
    data class Decrease(val productId: String) : CartAction
    data object Checkout : CartAction
}

class CartViewModel(
    private val observeCartUseCase: ObserveCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val checkoutUseCase: CheckoutUseCase,
) : BaseViewModel<CartState, CartAction>(CartState()) {

    init {
        observeCartUseCase()
            .onEach { cart -> updateState { copy(cart = cart) } }
            .launchWith(viewModelScope, onError = ::showError)
    }

    override fun onAction(action: CartAction) {
        when (action) {
            is CartAction.Increase -> addToCartUseCase(action.productId).launchWith(viewModelScope, ::showError)
            is CartAction.Decrease -> removeFromCartUseCase(action.productId).launchWith(viewModelScope, ::showError)
            CartAction.Checkout -> checkout()
        }
    }

    override fun errorMessage(error: Throwable): Int = when (error) {
        CartException.EmptyCart -> R.string.cart_error_empty
        else -> super.errorMessage(error)
    }

    private fun checkout() {
        if (currentState.isCheckingOut) return
        checkoutUseCase()
            .onStart { updateState { copy(isCheckingOut = true) } }
            .onEach { updateState { copy(checkedOut = true) } }
            .onCompletion { updateState { copy(isCheckingOut = false) } }
            .launchWith(viewModelScope, onError = ::showError)
    }
}
