package com.castorama.krakathon.core.events

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance

/**
 * App-wide bus for one-off facts. For STATE other screens must see, use a
 * `StateFlow` on a singleton repository instead. Only domain/data touch the bus;
 * ViewModels observe it through a use case.
 */
interface AppEventBus {
    val events: SharedFlow<AppEvent>
    fun emit(event: AppEvent)
}

inline fun <reified T : AppEvent> AppEventBus.observe(): Flow<T> = events.filterIsInstance<T>()

/** DROP_OLDEST: `emit` never fails and the newest event always gets through. */
internal class AppEventBusImpl : AppEventBus {
    private val _events = MutableSharedFlow<AppEvent>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val events: SharedFlow<AppEvent> = _events.asSharedFlow()

    override fun emit(event: AppEvent) {
        _events.tryEmit(event)
    }
}
