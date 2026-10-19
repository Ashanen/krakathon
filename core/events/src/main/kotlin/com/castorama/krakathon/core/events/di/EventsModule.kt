package com.castorama.krakathon.core.events.di

import com.castorama.krakathon.core.events.AppEventBus
import com.castorama.krakathon.core.events.AppEventBusImpl
import org.koin.dsl.module

val eventsModule = module {
    single<AppEventBus> { AppEventBusImpl() }
}
