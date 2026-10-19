package com.castorama.krakathon.feature.catalog.api

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val priceInGrosze: Long,
)
