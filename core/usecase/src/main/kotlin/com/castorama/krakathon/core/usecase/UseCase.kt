package com.castorama.krakathon.core.usecase

import kotlinx.coroutines.flow.Flow

/**
 * Public use case contract. Put `interface XUseCase : UseCase<In, Out>` in a feature `api`
 * so other features can call it. The implementation extends [BaseUseCase] in `domain`.
 */
interface UseCase<in In, out Out> {
    operator fun invoke(params: In): Flow<Out>
}

operator fun <Out> UseCase<Unit, Out>.invoke(): Flow<Out> = invoke(Unit)
