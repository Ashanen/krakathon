package com.castorama.krakathon.feature.cart.data.di

import com.castorama.krakathon.feature.cart.data.InMemoryCartRepository
import com.castorama.krakathon.feature.cart.domain.CartRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val cartDataModule = module {
    singleOf(::InMemoryCartRepository) bind CartRepository::class
}
