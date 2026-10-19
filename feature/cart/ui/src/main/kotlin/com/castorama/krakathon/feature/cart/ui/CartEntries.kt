package com.castorama.krakathon.feature.cart.ui

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.castorama.krakathon.core.navigation.FeatureEntries
import com.castorama.krakathon.core.navigation.Navigator
import com.castorama.krakathon.feature.cart.api.CartRoute

internal class CartEntries : FeatureEntries {
    override fun EntryProviderScope<NavKey>.register(navigator: Navigator) {
        entry<CartRoute> {
            CartEntry(onBack = navigator::back, onCheckedOut = navigator::back)
        }
    }
}
