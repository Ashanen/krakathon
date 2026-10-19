package com.castorama.krakathon.feature.catalog.data.source

import com.castorama.krakathon.core.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/** Real API example. Bind it in [com.castorama.krakathon.feature.catalog.data.di.catalogDataModule] when the backend exists. */
class RemoteProductDataSource(
    private val client: HttpClient,
) : ProductDataSource {
    override suspend fun getProducts(): List<ProductDto> = safeApiCall {
        client.get("products").body()
    }

    override suspend fun getProduct(id: String): ProductDto = safeApiCall {
        client.get("products/$id").body()
    }
}
