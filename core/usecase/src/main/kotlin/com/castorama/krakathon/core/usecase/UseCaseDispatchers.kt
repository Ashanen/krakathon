package com.castorama.krakathon.core.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/** Dispatcher for all use cases. Tests swap it via [setBackground] and restore with [resetBackground]. */
object UseCaseDispatchers {
    var background: CoroutineDispatcher = Dispatchers.Default
        private set

    fun setBackground(dispatcher: CoroutineDispatcher) {
        background = dispatcher
    }

    fun resetBackground() {
        background = Dispatchers.Default
    }
}
