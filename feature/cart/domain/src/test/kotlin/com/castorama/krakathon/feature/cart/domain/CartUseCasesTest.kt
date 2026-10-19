package com.castorama.krakathon.feature.cart.domain

import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import com.castorama.krakathon.core.testing.RecordingEventBus
import com.castorama.krakathon.core.testing.TestDispatchersExtension
import com.castorama.krakathon.core.usecase.invoke
import com.castorama.krakathon.feature.cart.api.OrderPlaced
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.single

class CartUseCasesTest : BehaviorSpec({
    extension(TestDispatchersExtension())

    val products = listOf(product("a", 1_000), product("b", 250))

    Given("a cart with 2x a and 1x b") {
        val repository = FakeCartRepository(mapOf("a" to 2, "b" to 1))

        When("the cart is observed") {
            val cart = ObserveCartUseCase(repository, FakeGetProductUseCase(products))().first()

            Then("items join catalog products and the total is summed") {
                cart.itemCount shouldBe 3
                cart.totalInGrosze shouldBe 2_250
            }
        }

        When("the cart is checked out") {
            val bus = RecordingEventBus()
            CheckoutUseCase(repository, bus)().single()

            Then("the cart is cleared and OrderPlaced is published") {
                repository.quantities.value shouldBe emptyMap()
                bus.emitted shouldContainExactly listOf(OrderPlaced(itemCount = 3))
            }
        }
    }

    Given("an empty cart") {
        val repository = FakeCartRepository()
        val bus = RecordingEventBus()

        When("checkout is attempted") {
            Then("EmptyCart is thrown and nothing is published") {
                shouldThrow<CartException.EmptyCart> { CheckoutUseCase(repository, bus)().single() }
                bus.emitted.shouldBeEmpty()
            }
        }

        When("a product is added twice and removed once") {
            AddToCartUseCaseImpl(repository)("a").single()
            AddToCartUseCaseImpl(repository)("a").single()
            RemoveFromCartUseCase(repository)("a").single()

            Then("the count is 1") {
                ObserveCartCountUseCaseImpl(repository)().test { awaitItem() shouldBe 1 }
            }
        }
    }
})
