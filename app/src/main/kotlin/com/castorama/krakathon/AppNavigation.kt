package com.castorama.krakathon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.castorama.krakathon.core.navigation.BackStackNavigator
import com.castorama.krakathon.core.navigation.FeatureEntries
import com.castorama.krakathon.feature.catalog.api.CatalogRoute
import org.koin.compose.getKoin

/** Start screen = [CatalogRoute]. Every feature adds its screens through [FeatureEntries]. */
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(CatalogRoute)
    val navigator = remember(backStack) { BackStackNavigator(backStack) }
    val koin = getKoin()
    val features = remember { koin.getAll<FeatureEntries>() }

    NavDisplay(
        backStack = backStack,
        onBack = { navigator.back() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            features.forEach { feature -> with(feature) { register(navigator) } }
        },
    )
}
