package com.castorama.krakathon.feature.catalog.domain.di

import com.castorama.krakathon.feature.catalog.api.GetProductUseCase
import com.castorama.krakathon.feature.catalog.domain.GetProductUseCaseImpl
import com.castorama.krakathon.feature.catalog.domain.GetProductsUseCase
import com.castorama.krakathon.feature.catalog.domain.ObserveOrderPlacedUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val catalogDomainModule = module {
    factoryOf(::GetProductsUseCase)
    factoryOf(::GetProductUseCaseImpl) bind GetProductUseCase::class
    factoryOf(::ObserveOrderPlacedUseCase)
}
