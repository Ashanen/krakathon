package com.castorama.krakathon.feature.catalog.ui

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.castorama.krakathon.core.navigation.FeatureEntries
import com.castorama.krakathon.core.navigation.Navigator
import com.castorama.krakathon.feature.cart.api.CartRoute
import com.castorama.krakathon.feature.catalog.api.CatalogRoute
import com.castorama.krakathon.feature.catalog.api.ProductDetailRoute
import com.castorama.krakathon.feature.catalog.ui.detail.ProductDetailEntry
import com.castorama.krakathon.feature.catalog.ui.list.CatalogEntry

internal class CatalogEntries : FeatureEntries {
    override fun EntryProviderScope<NavKey>.register(navigator: Navigator) {
        entry<CatalogRoute> {
            CatalogEntry(
                onProductClick = { navigator.navigate(ProductDetailRoute(it)) },
                onCartClick = { navigator.navigate(CartRoute) },
            )
        }
        entry<ProductDetailRoute> { route ->
            ProductDetailEntry(productId = route.productId, onBack = navigator::back)
        }
    }
}
