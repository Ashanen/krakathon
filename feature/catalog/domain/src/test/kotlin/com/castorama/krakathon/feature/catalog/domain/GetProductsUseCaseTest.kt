package com.castorama.krakathon.feature.catalog.domain

import com.castorama.krakathon.core.testing.TestDispatchersExtension
import com.castorama.krakathon.core.usecase.invoke
import com.castorama.krakathon.feature.catalog.api.CatalogException
import com.castorama.krakathon.feature.catalog.api.Product
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.single

class GetProductsUseCaseTest : BehaviorSpec({
    extension(TestDispatchersExtension())

    Given("a repository with unsorted products") {
        val repository = FakeProductRepository(listOf(product("2", "Saw"), product("1", "Drill")))

        When("products are loaded") {
            val result = GetProductsUseCase(repository)().single()

            Then("they are sorted by name") {
                result.map { it.name } shouldContainExactly listOf("Drill", "Saw")
            }
        }

        When("one product is loaded by id") {
            val result = GetProductUseCaseImpl(repository)("2").single()

            Then("that product is returned") {
                result.name shouldBe "Saw"
            }
        }

        When("an unknown id is loaded") {
            Then("ProductNotFound is thrown") {
                shouldThrow<CatalogException.ProductNotFound> {
                    GetProductUseCaseImpl(repository)("404").single()
                }
            }
        }
    }
})

private fun product(id: String, name: String) = Product(id, name, "Tools", "", 100)

private class FakeProductRepository(private val products: List<Product>) : ProductRepository {
    override suspend fun getProducts() = products
    override suspend fun getProduct(id: String) =
        products.firstOrNull { it.id == id } ?: throw CatalogException.ProductNotFound(id)
}
