package com.castorama.krakathon.feature.catalog.data.source

import com.castorama.krakathon.feature.catalog.api.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    @SerialName("price_grosze") val priceInGrosze: Long,
)

fun ProductDto.toDomain() = Product(
    id = id,
    name = name,
    category = category,
    description = description,
    priceInGrosze = priceInGrosze,
)

fun Product.toDto() = ProductDto(
    id = id,
    name = name,
    category = category,
    description = description,
    priceInGrosze = priceInGrosze,
)
