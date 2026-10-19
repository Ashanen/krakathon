package com.castorama.krakathon.feature.catalog.ui.list

import com.castorama.krakathon.core.exception.NetworkException
import com.castorama.krakathon.core.presentation.mvi.Loadable
import com.castorama.krakathon.core.testing.RecordingEventBus
import com.castorama.krakathon.core.testing.TestDispatchersExtension
import com.castorama.krakathon.core.usecase.NoParamUseCase
import com.castorama.krakathon.feature.cart.api.ObserveCartCountUseCase
import com.castorama.krakathon.feature.catalog.api.Product
import com.castorama.krakathon.feature.catalog.domain.GetProductsUseCase
import com.castorama.krakathon.feature.catalog.domain.ObserveOrderPlacedUseCase
import com.castorama.krakathon.feature.catalog.domain.ProductRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CatalogViewModelTest : BehaviorSpec({
    extension(TestDispatchersExtension())

    Given("the first catalog load fails") {
        val repository = FlakyProductRepository(failuresLeft = 1)
        val viewModel = CatalogViewModel(
            GetProductsUseCase(repository),
            ZeroCartCount,
            ObserveOrderPlacedUseCase(RecordingEventBus()),
        )

        Then("the screen shows an inline failure with retry, not a skeleton forever") {
            viewModel.uiState.value.products.shouldBeInstanceOf<Loadable.Failed>()
            viewModel.uiState.value.baseUiState.isLoading shouldBe false
        }

        When("reload is pressed") {
            viewModel.onAction(CatalogAction.Reload)

            Then("products are shown") {
                viewModel.uiState.value.products.valueOrNull?.map { it.id } shouldBe listOf("p1")
            }
        }
    }
})

private class FlakyProductRepository(var failuresLeft: Int) : ProductRepository {
    private val product = Product("p1", "Drill", "Tools", "", 100)
    override suspend fun getProducts(): List<Product> {
        if (failuresLeft-- > 0) throw NetworkException.Timeout
        return listOf(product)
    }
    override suspend fun getProduct(id: String) = product
}

private object ZeroCartCount : NoParamUseCase<Int>(), ObserveCartCountUseCase {
    override fun onBuild(params: Unit): Flow<Int> = flowOf(0)
}
