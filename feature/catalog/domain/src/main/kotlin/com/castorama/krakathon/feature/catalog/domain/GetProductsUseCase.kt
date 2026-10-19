package com.castorama.krakathon.feature.catalog.domain

import com.castorama.krakathon.core.usecase.NoParamUseCase
import com.castorama.krakathon.core.usecase.flowSingle
import com.castorama.krakathon.feature.catalog.api.Product
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val repository: ProductRepository,
) : NoParamUseCase<List<Product>>() {
    override fun onBuild(params: Unit): Flow<List<Product>> = flowSingle {
        repository.getProducts().sortedBy { it.name }
    }
}
