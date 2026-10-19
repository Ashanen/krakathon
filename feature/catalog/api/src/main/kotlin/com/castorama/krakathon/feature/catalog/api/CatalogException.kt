package com.castorama.krakathon.feature.catalog.api

import com.castorama.krakathon.core.exception.AppException

sealed class CatalogException : Exception(), AppException {
    data class ProductNotFound(val productId: String) : CatalogException()
}
