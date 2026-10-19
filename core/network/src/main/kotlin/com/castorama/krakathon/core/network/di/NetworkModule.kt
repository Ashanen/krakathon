package com.castorama.krakathon.core.network.di

import com.castorama.krakathon.core.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.dsl.module

/** Needs a `NetworkConfig` declared in `app`. */
val networkModule = module {
    single<HttpClient> { HttpClientFactory.create(get()) }
}
