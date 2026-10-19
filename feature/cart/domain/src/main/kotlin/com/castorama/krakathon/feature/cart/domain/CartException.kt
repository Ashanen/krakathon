package com.castorama.krakathon.feature.cart.domain

import com.castorama.krakathon.core.exception.AppException

sealed class CartException : Exception(), AppException {
    data object EmptyCart : CartException()
}
