package com.castorama.krakathon.feature.cart.domain.model

import com.castorama.krakathon.feature.catalog.api.Product

data class CartItem(val product: Product, val quantity: Int) {
    val totalInGrosze: Long get() = product.priceInGrosze * quantity
}

data class Cart(val items: List<CartItem>) {
    val totalInGrosze: Long get() = items.sumOf { it.totalInGrosze }
    val itemCount: Int get() = items.sumOf { it.quantity }
}
