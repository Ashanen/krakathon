package com.castorama.krakathon.feature.catalog.api

import com.castorama.krakathon.core.usecase.UseCase

/** Public: other features may read a product by id. Param = product id. */
interface GetProductUseCase : UseCase<String, Product>
