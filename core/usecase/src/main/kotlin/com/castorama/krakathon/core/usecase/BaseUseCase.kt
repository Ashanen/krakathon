package com.castorama.krakathon.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Base for every use case. Implement [onBuild]; callers get a cold [Flow]
 * that runs on [UseCaseDispatchers.background].
 *
 * ```kotlin
 * internal class GetProductUseCaseImpl(
 *     private val repository: ProductRepository,
 * ) : BaseUseCase<String, Product>(), GetProductUseCase {
 *     override fun onBuild(params: String) = flowSingle { repository.getProduct(params) }
 * }
 * ```
 */
abstract class BaseUseCase<In, Out> : UseCase<In, Out> {
    protected abstract fun onBuild(params: In): Flow<Out>

    final override operator fun invoke(params: In): Flow<Out> =
        onBuild(params).flowOn(UseCaseDispatchers.background)
}

/** Use case without input. Call it as `useCase()`. */
abstract class NoParamUseCase<Out> : BaseUseCase<Unit, Out>()
