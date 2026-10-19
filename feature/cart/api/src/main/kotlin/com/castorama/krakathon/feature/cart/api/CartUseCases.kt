package com.castorama.krakathon.feature.cart.api

import com.castorama.krakathon.core.usecase.UseCase

/** Param = product id. */
interface AddToCartUseCase : UseCase<String, Unit>

/** Emits the total number of items, live. */
interface ObserveCartCountUseCase : UseCase<Unit, Int>
