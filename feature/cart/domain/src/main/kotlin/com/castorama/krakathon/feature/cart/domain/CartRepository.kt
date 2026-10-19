package com.castorama.krakathon.feature.cart.domain

import kotlinx.coroutines.flow.StateFlow

/** Reactive state on a singleton repo: every screen observing [quantities] updates at once. */
interface CartRepository {
    /** productId -> quantity */
    val quantities: StateFlow<Map<String, Int>>
    fun add(productId: String)
    fun remove(productId: String)
    fun clear()
}
