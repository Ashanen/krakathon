package com.castorama.krakathon.core.events

/**
 * A one-off fact that already happened ("order placed"). Declare events in the
 * feature `api` that owns them: `data class OrderPlaced(val orderId: String) : AppEvent`.
 */
interface AppEvent
