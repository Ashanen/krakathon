package com.castorama.krakathon.feature.catalog.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object CatalogRoute : NavKey

@Serializable
data class ProductDetailRoute(val productId: String) : NavKey
