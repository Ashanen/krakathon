package com.castorama.krakathon.feature.catalog.domain

import com.castorama.krakathon.core.events.AppEventBus
import com.castorama.krakathon.core.events.observe
import com.castorama.krakathon.core.usecase.NoParamUseCase
import com.castorama.krakathon.feature.cart.api.OrderPlaced
import kotlinx.coroutines.flow.Flow

/** Event bus example: catalog reacts to a one-off fact published by cart (via cart:api). */
class ObserveOrderPlacedUseCase(
    private val eventBus: AppEventBus,
) : NoParamUseCase<OrderPlaced>() {
    override fun onBuild(params: Unit): Flow<OrderPlaced> = eventBus.observe<OrderPlaced>()
}
