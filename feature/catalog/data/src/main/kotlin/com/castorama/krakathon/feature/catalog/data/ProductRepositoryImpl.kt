package com.castorama.krakathon.feature.catalog.data

import com.castorama.krakathon.feature.catalog.api.CatalogException
import com.castorama.krakathon.feature.catalog.api.Product
import com.castorama.krakathon.feature.catalog.data.source.ProductDataSource
import com.castorama.krakathon.feature.catalog.data.source.toDomain
import com.castorama.krakathon.feature.catalog.domain.ProductRepository

internal class ProductRepositoryImpl(
    private val dataSource: ProductDataSource,
) : ProductRepository {
    override suspend fun getProducts(): List<Product> = dataSource.getProducts().map { it.toDomain() }

    override suspend fun getProduct(id: String): Product =
        getProducts().firstOrNull { it.id == id } ?: throw CatalogException.ProductNotFound(id)
}
