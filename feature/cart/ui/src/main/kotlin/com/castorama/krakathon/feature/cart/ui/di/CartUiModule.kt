package com.castorama.krakathon.feature.cart.ui.di

import com.castorama.krakathon.core.navigation.FeatureEntries
import com.castorama.krakathon.feature.cart.ui.CartEntries
import com.castorama.krakathon.feature.cart.ui.CartViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val cartUiModule = module {
    viewModelOf(::CartViewModel)
    singleOf(::CartEntries) bind FeatureEntries::class
}
