package com.castorama.krakathon.feature.cart.api

import com.castorama.krakathon.core.events.AppEvent

data class OrderPlaced(val itemCount: Int) : AppEvent
