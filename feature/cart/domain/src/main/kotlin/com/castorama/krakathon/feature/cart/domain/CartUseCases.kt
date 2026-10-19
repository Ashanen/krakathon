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
import com.castorama.krakathon.feature.catalog.api.CatalogException
import com.castorama.krakathon.feature.catalog.api.GetProductUseCase
import com.castorama.krakathon.feature.catalog.api.Product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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

class RemoveProductsUseCase(
    private val repository: CartRepository,
) : BaseUseCase<Set<String>, Unit>() {
    override fun onBuild(params: Set<String>): Flow<Unit> = flowSingle { repository.removeAll(params) }
}

/**
 * Cross-feature via api: joins cart quantities with products from catalog:api.
 * Products are looked up only when the SET of ids changes, not on every quantity change.
 * A product the catalog no longer has is reported in [Cart.unavailableIds] instead of failing the stream.
 */
class ObserveCartUseCase(
    private val repository: CartRepository,
    private val getProductUseCase: GetProductUseCase,
) : NoParamUseCase<Cart>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onBuild(params: Unit): Flow<Cart> {
        val products = repository.quantities
            .map { it.keys }
            .distinctUntilChanged()
            .flatMapLatest(::lookUp)
        return combine(repository.quantities, products) { quantities, lookup ->
            val known = quantities.filterKeys { it in lookup }
            Cart(
                items = known.mapNotNull { (id, quantity) -> lookup[id]?.let { CartItem(it, quantity) } },
                unavailableIds = known.filterKeys { lookup[it] == null }.keys,
            )
        }
    }

    /** id -> product, or id -> null when the catalog no longer has it. */
    private fun lookUp(ids: Set<String>): Flow<Map<String, Product?>> =
        if (ids.isEmpty()) {
            flowOf(emptyMap())
        } else {
            combine(ids.map(::lookUpOne)) { pairs -> pairs.toMap() }
        }

    private fun lookUpOne(id: String): Flow<Pair<String, Product?>> =
        getProductUseCase(id)
            .map<Product, Pair<String, Product?>> { id to it }
            .catch { error -> if (error is CatalogException.ProductNotFound) emit(id to null) else throw error }
}

/** Empties the cart atomically and publishes [OrderPlaced] on the event bus. */
class CheckoutUseCase(
    private val repository: CartRepository,
    private val eventBus: AppEventBus,
) : NoParamUseCase<Unit>() {
    override fun onBuild(params: Unit): Flow<Unit> = flowSingle {
        val taken = repository.takeAll()
        val itemCount = taken.values.sum()
        if (itemCount == 0) throw CartException.EmptyCart
        eventBus.emit(OrderPlaced(itemCount))
    }
}
