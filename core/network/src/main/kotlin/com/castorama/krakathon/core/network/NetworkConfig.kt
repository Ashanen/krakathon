package com.castorama.krakathon.core.network

/** Set the real API base URL in `app` when the backend exists. */
data class NetworkConfig(
    val baseUrl: String,
    val enableLogging: Boolean = false,
)
