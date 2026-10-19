package com.castorama.krakathon.feature.catalog.ui.di

import com.castorama.krakathon.core.navigation.FeatureEntries
import com.castorama.krakathon.feature.catalog.ui.CatalogEntries
import com.castorama.krakathon.feature.catalog.ui.detail.ProductDetailViewModel
import com.castorama.krakathon.feature.catalog.ui.list.CatalogViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val catalogUiModule = module {
    viewModelOf(::CatalogViewModel)
    viewModel { params -> ProductDetailViewModel(params.get(), get(), get()) }
    singleOf(::CatalogEntries) bind FeatureEntries::class
}
