package com.castorama.krakathon.feature.cart.data

import com.castorama.krakathon.feature.cart.domain.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update

internal class InMemoryCartRepository : CartRepository {
    private val _quantities = MutableStateFlow<Map<String, Int>>(emptyMap())
    override val quantities: StateFlow<Map<String, Int>> = _quantities.asStateFlow()

    override fun add(productId: String) = _quantities.update { it + (productId to (it[productId] ?: 0) + 1) }

    override fun remove(productId: String) = _quantities.update {
        val left = (it[productId] ?: 0) - 1
        if (left > 0) it + (productId to left) else it - productId
    }

    override fun removeAll(productIds: Set<String>) = _quantities.update { it - productIds }

    override fun takeAll(): Map<String, Int> = _quantities.getAndUpdate { emptyMap() }
}
