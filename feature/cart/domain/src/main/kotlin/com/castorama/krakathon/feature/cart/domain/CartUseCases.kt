package com.castorama.krakathon.feature.cart.domain

import com.castorama.krakathon.core.events.AppEventBus
import com.castorama.krakathon.core.usecase.BaseUseCase
import com.castorama.krakathon.core.usecase.NoParamUseCase
import com.castorama.krakathon.core.usecase.flowSingle
import com.castorama.krakathon.feature.cart.api.AddToCartUseCase
import com.castorama.krakathon.feature.cart.api.ObserveCartCountUseCase
import com.castorama.krakathon.feature.cart.api.OrderPlaced
import com.castorama.krakathon.feature.cart.domain.model.Cart
import com.castorama.krakathon.feature.cart.domain.model.CartItem
import com.castorama.krakathon.feature.catalog.api.GetProductUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class AddToCartUseCaseImpl(
    private val repository: CartRepository,
) : BaseUseCase<String, Unit>(), AddToCartUseCase {
    override fun onBuild(params: String): Flow<Unit> = flowSingle { repository.add(params) }
}

internal class ObserveCartCountUseCaseImpl(
    private val repository: CartRepository,
) : NoParamUseCase<Int>(), ObserveCartCountUseCase {
    override fun onBuild(params: Unit): Flow<Int> = repository.quantities.map { it.values.sum() }
}

class RemoveFromCartUseCase(
    private val repository: CartRepository,
) : BaseUseCase<String, Unit>() {
    override fun onBuild(params: String): Flow<Unit> = flowSingle { repository.remove(params) }
}

/** Cross-feature via api: joins cart quantities with products from catalog:api. */
class ObserveCartUseCase(
    private val repository: CartRepository,
    private val getProductUseCase: GetProductUseCase,
) : NoParamUseCase<Cart>() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onBuild(params: Unit): Flow<Cart> = repository.quantities.flatMapLatest { quantities ->
        if (quantities.isEmpty()) {
            flowOf(Cart(emptyList()))
        } else {
            combine(quantities.map { (id, quantity) -> getProductUseCase(id).map { CartItem(it, quantity) } }) {
                Cart(it.toList())
            }
        }
    }
}

/** Clears the cart and publishes [OrderPlaced] on the event bus. */
class CheckoutUseCase(
    private val repository: CartRepository,
    private val eventBus: AppEventBus,
) : NoParamUseCase<Unit>() {
    override fun onBuild(params: Unit): Flow<Unit> = flowSingle {
        val itemCount = repository.quantities.value.values.sum()
        if (itemCount == 0) throw CartException.EmptyCart
        repository.clear()
        eventBus.emit(OrderPlaced(itemCount))
    }
}
