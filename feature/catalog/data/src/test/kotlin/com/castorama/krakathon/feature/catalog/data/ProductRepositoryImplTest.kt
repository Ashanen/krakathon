package com.castorama.krakathon.feature.catalog.data

import com.castorama.krakathon.core.exception.NetworkException
import com.castorama.krakathon.feature.catalog.api.CatalogException
import com.castorama.krakathon.feature.catalog.data.source.ProductDataSource
import com.castorama.krakathon.feature.catalog.data.source.ProductDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class ProductRepositoryImplTest : BehaviorSpec({

    Given("a data source") {
        val source = CountingDataSource(listOf(ProductDto("p1", "Drill", "Tools", "", 100)))
        val repository = ProductRepositoryImpl(source)

        When("one product is requested twice") {
            repository.getProduct("p1")
            repository.getProduct("p1")

            Then("it is fetched once by id, never via the full catalog") {
                source.byIdCalls shouldContainExactly listOf("p1")
                source.listCalls shouldBe 0
            }
        }

        When("the catalog was already loaded") {
            val fresh = CountingDataSource(listOf(ProductDto("p1", "Drill", "Tools", "", 100)))
            val repo = ProductRepositoryImpl(fresh)
            repo.getProducts()
            repo.getProduct("p1")

            Then("lookups come from the cache") {
                fresh.byIdCalls.shouldBeEmpty()
            }
        }

        When("an unknown id is requested") {
            Then("the domain exception is thrown") {
                shouldThrow<CatalogException.ProductNotFound> { repository.getProduct("nope") }
            }
        }
    }
})

private class CountingDataSource(private val products: List<ProductDto>) : ProductDataSource {
    var listCalls = 0
    val byIdCalls = mutableListOf<String>()

    override suspend fun getProducts(): List<ProductDto> = products.also { listCalls++ }

    override suspend fun getProduct(id: String): ProductDto {
        byIdCalls += id
        return products.firstOrNull { it.id == id } ?: throw NetworkException.NotFound
    }
}
