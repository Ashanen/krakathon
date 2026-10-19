package com.castorama.krakathon.feature.catalog.data.source

interface ProductDataSource {
    suspend fun getProducts(): List<ProductDto>

    /** Throws `NetworkException.NotFound` for an unknown id. */
    suspend fun getProduct(id: String): ProductDto
}
