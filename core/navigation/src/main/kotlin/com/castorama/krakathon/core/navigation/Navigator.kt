package com.castorama.krakathon.core.navigation

import androidx.navigation3.runtime.NavKey

/** Screens navigate through this. Routes (`NavKey`) live in each feature `api`. */
interface Navigator {
    fun navigate(route: NavKey)
    fun replaceAll(route: NavKey)
    fun back()
}

class BackStackNavigator(private val backStack: MutableList<NavKey>) : Navigator {
    override fun navigate(route: NavKey) {
        backStack.add(route)
    }

    override fun replaceAll(route: NavKey) {
        backStack.clear()
        backStack.add(route)
    }

    override fun back() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }
}
