package com.castorama.krakathon.core.events

import app.cash.turbine.test
import com.castorama.krakathon.core.testing.RecordingEventBus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

private data class Ping(val n: Int) : AppEvent
private data class Pong(val n: Int) : AppEvent

/** Production bus and the test fake must deliver events the same way. */
class AppEventBusContractTest : FunSpec({

    val buses: List<Pair<String, () -> AppEventBus>> = listOf(
        "AppEventBusImpl" to { AppEventBusImpl() },
        "RecordingEventBus" to { RecordingEventBus() },
    )

    buses.forEach { (name, newBus) ->
        context(name) {
            test("subscriber gets events emitted after it subscribed") {
                val bus = newBus()
                bus.observe<Ping>().test {
                    bus.emit(Ping(1))
                    awaitItem() shouldBe Ping(1)
                }
            }

            test("late subscriber does not get earlier events") {
                val bus = newBus()
                bus.emit(Ping(1))
                bus.observe<Ping>().test { expectNoEvents() }
            }

            test("observe filters by type") {
                val bus = newBus()
                bus.observe<Pong>().test {
                    bus.emit(Ping(1))
                    bus.emit(Pong(2))
                    awaitItem() shouldBe Pong(2)
                }
            }
        }
    }
})
