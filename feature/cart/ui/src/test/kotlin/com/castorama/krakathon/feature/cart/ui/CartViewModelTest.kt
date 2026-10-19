package com.castorama.krakathon.feature.cart.ui

import com.castorama.krakathon.core.testing.RecordingEventBus
import com.castorama.krakathon.core.testing.TestDispatchersExtension
import com.castorama.krakathon.core.usecase.BaseUseCase
import com.castorama.krakathon.core.usecase.flowSingle
import com.castorama.krakathon.feature.cart.api.AddToCartUseCase
import com.castorama.krakathon.feature.cart.domain.CartRepository
import com.castorama.krakathon.feature.cart.domain.CheckoutUseCase
import com.castorama.krakathon.feature.cart.domain.ObserveCartUseCase
import com.castorama.krakathon.feature.cart.domain.RemoveFromCartUseCase
import com.castorama.krakathon.feature.catalog.api.GetProductUseCase
import com.castorama.krakathon.feature.catalog.api.Product
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class CartViewModelTest : BehaviorSpec({
    extension(TestDispatchersExtension())

    Given("an empty cart") {
        val repository = FakeCartRepository()
        val viewModel = viewModel(repository)

        When("checkout is pressed") {
            viewModel.onAction(CartAction.Checkout)

            Then("an error dialog explains the cart is empty") {
                val error = viewModel.uiState.value.baseUiState.errorDialogState
                error.isVisible shouldBe true
                error.message shouldBe R.string.cart_error_empty
                viewModel.uiState.value.checkedOut shouldBe false
            }
        }
    }

    Given("a cart with one drill") {
        val repository = FakeCartRepository(mapOf("1" to 1))
        val viewModel = viewModel(repository)

        When("plus is pressed") {
            viewModel.onAction(CartAction.Increase("1"))

            Then("the total doubles") {
                viewModel.uiState.value.cart?.totalInGrosze shouldBe 2_000
            }
        }

        When("checkout is pressed") {
            viewModel.onAction(CartAction.Checkout)

            Then("the screen is told to close") {
                viewModel.uiState.value.checkedOut shouldBe true
            }
        }
    }
})

private fun viewModel(repository: CartRepository) = CartViewModel(
    observeCartUseCase = ObserveCartUseCase(repository, FakeGetProductUseCase),
    addToCartUseCase = FakeAddToCart(repository),
    removeFromCartUseCase = RemoveFromCartUseCase(repository),
    checkoutUseCase = CheckoutUseCase(repository, RecordingEventBus()),
)

private class FakeCartRepository(initial: Map<String, Int> = emptyMap()) : CartRepository {
    override val quantities = MutableStateFlow(initial)
    override fun add(productId: String) = quantities.update { it + (productId to (it[productId] ?: 0) + 1) }
    override fun remove(productId: String) = quantities.update { it - productId }
    override fun clear() = quantities.update { emptyMap() }
}

private object FakeGetProductUseCase : BaseUseCase<String, Product>(), GetProductUseCase {
    override fun onBuild(params: String): Flow<Product> = flowOf(Product(params, "Drill", "Tools", "", 1_000))
}

private class FakeAddToCart(private val repository: CartRepository) : BaseUseCase<String, Unit>(), AddToCartUseCase {
    override fun onBuild(params: String): Flow<Unit> = flowSingle { repository.add(params) }
}
