package com.castorama.krakathon.feature.catalog.domain

import com.castorama.krakathon.core.usecase.BaseUseCase
import com.castorama.krakathon.core.usecase.flowSingle
import com.castorama.krakathon.feature.catalog.api.GetProductUseCase
import com.castorama.krakathon.feature.catalog.api.Product
import kotlinx.coroutines.flow.Flow

internal class GetProductUseCaseImpl(
    private val repository: ProductRepository,
) : BaseUseCase<String, Product>(), GetProductUseCase {
    override fun onBuild(params: String): Flow<Product> = flowSingle { repository.getProduct(params) }
}
