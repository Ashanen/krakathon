package com.castorama.krakathon.core.presentation.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn

/**
 * The only way a ViewModel collects a flow. Errors go to [onError], never crash the app.
 *
 * ```kotlin
 * getProductsUseCase()
 *     .onEach { products -> updateState { copy(products = products) } }
 *     .launchWith(viewModelScope, onError = ::showError)
 * ```
 */
fun <T> Flow<T>.launchWith(
    scope: CoroutineScope,
    onError: (Throwable) -> Unit,
): Job = catch { onError(it) }.launchIn(scope)
