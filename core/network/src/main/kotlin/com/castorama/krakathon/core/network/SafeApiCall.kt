package com.castorama.krakathon.core.network

import com.castorama.krakathon.core.exception.AppException
import com.castorama.krakathon.core.exception.NetworkException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import java.io.IOException

/**
 * Wrap EVERY remote call in a DataSource. Maps Ktor/IO errors to [NetworkException].
 * DataSources never use try/catch themselves.
 *
 * ```kotlin
 * override suspend fun getProducts() = safeApiCall { client.get("products").body<List<ProductDto>>() }
 * ```
 */
suspend fun <T> safeApiCall(block: suspend () -> T): T = try {
    block()
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    throw e.toNetworkException()
}

internal fun Throwable.toNetworkException(): Throwable = when (this) {
    is AppException -> this
    is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException -> NetworkException.Timeout
    is ResponseException -> when (response.status) {
        HttpStatusCode.Unauthorized -> NetworkException.Unauthorized
        HttpStatusCode.Forbidden -> NetworkException.Forbidden
        HttpStatusCode.NotFound -> NetworkException.NotFound
        else -> NetworkException.ServerError(response.status.value, message.orEmpty())
    }
    is SerializationException -> NetworkException.IncompatibleData(message.orEmpty())
    is IOException -> NetworkException.NoConnection
    else -> NetworkException.Unknown(message.orEmpty(), this)
}
