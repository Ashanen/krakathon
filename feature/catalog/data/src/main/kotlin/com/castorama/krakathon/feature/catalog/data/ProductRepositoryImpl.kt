package com.castorama.krakathon.feature.catalog.data

import com.castorama.krakathon.core.exception.NetworkException
import com.castorama.krakathon.feature.catalog.api.CatalogException
import com.castorama.krakathon.feature.catalog.api.Product
import com.castorama.krakathon.feature.catalog.data.source.ProductDataSource
import com.castorama.krakathon.feature.catalog.data.source.toDomain
import com.castorama.krakathon.feature.catalog.domain.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/** Singleton. Caches products by id so the cart can look them up without refetching the catalog. */
internal class ProductRepositoryImpl(
    private val dataSource: ProductDataSource,
) : ProductRepository {

    private val cache = MutableStateFlow<Map<String, Product>>(emptyMap())

    override suspend fun getProducts(): List<Product> =
        dataSource.getProducts().map { it.toDomain() }.also { products -> cache.value = products.associateBy(Product::id) }

    override suspend fun getProduct(id: String): Product =
        cache.value[id] ?: fetchProduct(id).also { product -> cache.update { it + (id to product) } }

    private suspend fun fetchProduct(id: String): Product = try {
        dataSource.getProduct(id).toDomain()
    } catch (_: NetworkException.NotFound) {
        throw CatalogException.ProductNotFound(id)
    }
}
