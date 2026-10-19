package com.castorama.krakathon.feature.catalog.data.di

import com.castorama.krakathon.feature.catalog.data.ProductRepositoryImpl
import com.castorama.krakathon.feature.catalog.data.source.FakeProductDataSource
import com.castorama.krakathon.feature.catalog.data.source.ProductDataSource
import com.castorama.krakathon.feature.catalog.domain.ProductRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/** Backend ready? Replace `FakeProductDataSource` with `RemoteProductDataSource`. */
val catalogDataModule = module {
    singleOf(::FakeProductDataSource) bind ProductDataSource::class
    singleOf(::ProductRepositoryImpl) bind ProductRepository::class
}
