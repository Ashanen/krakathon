package com.castorama.krakathon.feature.cart.ui

import com.castorama.krakathon.core.exception.NetworkException
import com.castorama.krakathon.core.presentation.mvi.Loadable
import com.castorama.krakathon.core.testing.RecordingEventBus
import com.castorama.krakathon.core.testing.TestDispatchersExtension
import com.castorama.krakathon.core.usecase.BaseUseCase
import com.castorama.krakathon.core.usecase.flowSingle
import com.castorama.krakathon.feature.cart.api.AddToCartUseCase
import com.castorama.krakathon.feature.cart.domain.CartRepository
import com.castorama.krakathon.feature.cart.domain.CheckoutUseCase
import com.castorama.krakathon.feature.cart.domain.ObserveCartUseCase
import com.castorama.krakathon.feature.cart.domain.RemoveFromCartUseCase
import com.castorama.krakathon.feature.cart.domain.RemoveProductsUseCase
import com.castorama.krakathon.feature.catalog.api.GetProductUseCase
import com.castorama.krakathon.feature.catalog.api.Product
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update

class CartViewModelTest : BehaviorSpec({
    extension(TestDispatchersExtension())

    Given("an empty cart") {
        val viewModel = viewModel(FakeCartRepository())

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
        val viewModel = viewModel(FakeCartRepository(mapOf("1" to 1)))

        When("plus is pressed") {
            viewModel.onAction(CartAction.Increase("1"))

            Then("the total doubles") {
                viewModel.uiState.value.cart.valueOrNull?.totalInGrosze shouldBe 2_000
            }
        }

        When("checkout is pressed") {
            viewModel.onAction(CartAction.Checkout)

            Then("the screen is told to close") {
                viewModel.uiState.value.checkedOut shouldBe true
            }
        }
    }

    Given("the network is down when the cart opens") {
        val repository = FakeCartRepository(mapOf("1" to 1))
        val lookup = FakeGetProductUseCase(offline = true)
        val viewModel = viewModel(repository, lookup)

        Then("the cart shows an inline failure, not an endless skeleton") {
            viewModel.uiState.value.cart.shouldBeInstanceOf<Loadable.Failed>()
        }

        When("the network is back and retry is pressed") {
            lookup.offline = false
            viewModel.onAction(CartAction.Retry)

            Then("the cart loads") {
                viewModel.uiState.value.cart.valueOrNull?.itemCount shouldBe 1
            }

            Then("later changes still reach the screen") {
                repository.add("1")
                viewModel.uiState.value.cart.valueOrNull?.itemCount shouldBe 2
            }
        }
    }
})

private fun viewModel(
    repository: CartRepository,
    lookup: GetProductUseCase = FakeGetProductUseCase(),
) = CartViewModel(
    observeCartUseCase = ObserveCartUseCase(repository, lookup),
    addToCartUseCase = FakeAddToCart(repository),
    removeFromCartUseCase = RemoveFromCartUseCase(repository),
    removeProductsUseCase = RemoveProductsUseCase(repository),
    checkoutUseCase = CheckoutUseCase(repository, RecordingEventBus()),
)

private class FakeCartRepository(initial: Map<String, Int> = emptyMap()) : CartRepository {
    override val quantities = MutableStateFlow(initial)
    override fun add(productId: String) = quantities.update { it + (productId to (it[productId] ?: 0) + 1) }
    override fun remove(productId: String) = quantities.update { it - productId }
    override fun removeAll(productIds: Set<String>) = quantities.update { it - productIds }
    override fun takeAll(): Map<String, Int> = quantities.getAndUpdate { emptyMap() }
}

private class FakeGetProductUseCase(var offline: Boolean = false) : BaseUseCase<String, Product>(), GetProductUseCase {
    override fun onBuild(params: String): Flow<Product> = flowSingle {
        if (offline) throw NetworkException.NoConnection
        Product(params, "Drill", "Tools", "", 1_000)
    }
}

private class FakeAddToCart(private val repository: CartRepository) : BaseUseCase<String, Unit>(), AddToCartUseCase {
    override fun onBuild(params: String): Flow<Unit> = flowSingle { repository.add(params) }
}
