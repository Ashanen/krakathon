package com.castorama.krakathon.core.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** One-shot flow from a suspend call. Use in use cases: `flowSingle { repository.load() }`. */
fun <T> flowSingle(block: suspend () -> T): Flow<T> = flow { emit(block()) }
