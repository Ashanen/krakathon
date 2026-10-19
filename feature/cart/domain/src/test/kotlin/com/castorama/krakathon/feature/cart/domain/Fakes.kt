package com.castorama.krakathon.feature.cart.domain

import com.castorama.krakathon.core.usecase.BaseUseCase
import com.castorama.krakathon.core.usecase.flowSingle
import com.castorama.krakathon.feature.catalog.api.CatalogException
import com.castorama.krakathon.feature.catalog.api.GetProductUseCase
import com.castorama.krakathon.feature.catalog.api.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeCartRepository(initial: Map<String, Int> = emptyMap()) : CartRepository {
    override val quantities = MutableStateFlow(initial)
    override fun add(productId: String) = quantities.update { it + (productId to (it[productId] ?: 0) + 1) }
    override fun remove(productId: String) = quantities.update {
        val left = (it[productId] ?: 0) - 1
        if (left > 0) it + (productId to left) else it - productId
    }
    override fun clear() = quantities.update { emptyMap() }
}

class FakeGetProductUseCase(private val products: List<Product>) : BaseUseCase<String, Product>(), GetProductUseCase {
    override fun onBuild(params: String): Flow<Product> = flowSingle {
        products.firstOrNull { it.id == params } ?: throw CatalogException.ProductNotFound(params)
    }
}

fun product(id: String, price: Long) = Product(id, "Product $id", "Tools", "", price)
