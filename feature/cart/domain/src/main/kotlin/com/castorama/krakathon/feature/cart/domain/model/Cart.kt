package com.castorama.krakathon.feature.cart.domain.model

import com.castorama.krakathon.feature.catalog.api.Product

data class CartItem(val product: Product, val quantity: Int) {
    val totalInGrosze: Long get() = product.priceInGrosze * quantity
}

/** [unavailableIds]: products still in the cart that the catalog no longer has. */
data class Cart(
    val items: List<CartItem>,
    val unavailableIds: Set<String> = emptySet(),
) {
    val totalInGrosze: Long get() = items.sumOf { it.totalInGrosze }
    val itemCount: Int get() = items.sumOf { it.quantity }
    val isEmpty: Boolean get() = items.isEmpty() && unavailableIds.isEmpty()
}
