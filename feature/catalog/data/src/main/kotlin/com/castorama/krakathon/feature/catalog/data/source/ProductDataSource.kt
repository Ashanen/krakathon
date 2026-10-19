package com.castorama.krakathon.feature.catalog.data.source

interface ProductDataSource {
    suspend fun getProducts(): List<ProductDto>
}
