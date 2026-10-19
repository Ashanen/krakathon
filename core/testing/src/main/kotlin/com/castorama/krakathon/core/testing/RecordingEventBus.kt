package com.castorama.krakathon.core.testing

import com.castorama.krakathon.core.events.AppEvent
import com.castorama.krakathon.core.events.AppEventBus
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Fake bus with the SAME delivery as production (no replay: a late subscriber misses earlier events).
 * Extra: every emitted event is kept in [emitted] for assertions.
 */
class RecordingEventBus : AppEventBus {
    val emitted = mutableListOf<AppEvent>()
    private val flow = MutableSharedFlow<AppEvent>(extraBufferCapacity = 64, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val events: SharedFlow<AppEvent> = flow

    override fun emit(event: AppEvent) {
        emitted += event
        flow.tryEmit(event)
    }
}
