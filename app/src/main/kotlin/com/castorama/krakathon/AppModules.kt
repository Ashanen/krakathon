package com.castorama.krakathon

import com.castorama.krakathon.core.events.di.eventsModule
import com.castorama.krakathon.core.network.NetworkConfig
import com.castorama.krakathon.core.network.di.networkModule
import com.castorama.krakathon.feature.cart.data.di.cartDataModule
import com.castorama.krakathon.feature.cart.domain.di.cartDomainModule
import com.castorama.krakathon.feature.cart.ui.di.cartUiModule
import com.castorama.krakathon.feature.catalog.data.di.catalogDataModule
import com.castorama.krakathon.feature.catalog.domain.di.catalogDomainModule
import com.castorama.krakathon.feature.catalog.ui.di.catalogUiModule
import org.koin.dsl.module

private val configModule = module {
    single { NetworkConfig(baseUrl = BuildConfig.API_BASE_URL, enableLogging = BuildConfig.DEBUG) }
}

val appModules = listOf(
    configModule,
    eventsModule,
    networkModule,
    catalogDomainModule,
    catalogDataModule,
    catalogUiModule,
    cartDomainModule,
    cartDataModule,
    cartUiModule,
    // newFeature:modules
)
