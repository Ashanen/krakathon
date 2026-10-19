package com.castorama.krakathon.feature.catalog.domain

import com.castorama.krakathon.feature.catalog.api.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getProduct(id: String): Product
}
