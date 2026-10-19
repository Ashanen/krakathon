package com.castorama.krakathon.feature.cart.domain.di

import com.castorama.krakathon.feature.cart.api.AddToCartUseCase
import com.castorama.krakathon.feature.cart.api.ObserveCartCountUseCase
import com.castorama.krakathon.feature.cart.domain.AddToCartUseCaseImpl
import com.castorama.krakathon.feature.cart.domain.CheckoutUseCase
import com.castorama.krakathon.feature.cart.domain.ObserveCartCountUseCaseImpl
import com.castorama.krakathon.feature.cart.domain.ObserveCartUseCase
import com.castorama.krakathon.feature.cart.domain.RemoveFromCartUseCase
import com.castorama.krakathon.feature.cart.domain.RemoveProductsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val cartDomainModule = module {
    factoryOf(::AddToCartUseCaseImpl) bind AddToCartUseCase::class
    factoryOf(::ObserveCartCountUseCaseImpl) bind ObserveCartCountUseCase::class
    factoryOf(::RemoveFromCartUseCase)
    factoryOf(::RemoveProductsUseCase)
    factoryOf(::ObserveCartUseCase)
    factoryOf(::CheckoutUseCase)
}
