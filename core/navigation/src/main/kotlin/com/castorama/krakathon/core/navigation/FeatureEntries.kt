package com.castorama.krakathon.core.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

/**
 * Each feature `ui` registers its screens by binding one of these in Koin:
 * `singleOf(::CatalogEntries) bind FeatureEntries::class`. `app` collects all of them.
 */
interface FeatureEntries {
    fun EntryProviderScope<NavKey>.register(navigator: Navigator)
}
