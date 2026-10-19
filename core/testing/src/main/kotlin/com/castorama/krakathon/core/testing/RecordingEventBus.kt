package com.castorama.krakathon.core.testing

import com.castorama.krakathon.core.events.AppEvent
import com.castorama.krakathon.core.events.AppEventBus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/** Fake bus: records every emitted event in [emitted] and replays them to subscribers. */
class RecordingEventBus : AppEventBus {
    val emitted = mutableListOf<AppEvent>()
    private val flow = MutableSharedFlow<AppEvent>(replay = 64)
    override val events: SharedFlow<AppEvent> = flow

    override fun emit(event: AppEvent) {
        emitted += event
        flow.tryEmit(event)
    }
}
