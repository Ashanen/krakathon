package com.castorama.krakathon.core.exception

/**
 * Marker for every typed app error. Data layer maps raw errors to these;
 * domain throws these; UI maps them to messages via [toErrorMessage].
 * Features add their own: `sealed class CartException : Exception(), AppException`.
 */
interface AppException

sealed class NetworkException : Exception(), AppException {
    data object NoConnection : NetworkException()
    data object Timeout : NetworkException()
    data object Unauthorized : NetworkException()
    data object Forbidden : NetworkException()
    data object NotFound : NetworkException()
    data class ServerError(val code: Int, override val message: String) : NetworkException()
    data class IncompatibleData(override val message: String) : NetworkException()
    data class Unknown(override val message: String, override val cause: Throwable? = null) : NetworkException()
}

sealed class AuthException : Exception(), AppException {
    data object InvalidCredentials : AuthException()
    data object SessionExpired : AuthException()
    data object UserNotFound : AuthException()
}

sealed class StorageException : Exception(), AppException {
    data class ReadError(override val message: String) : StorageException()
    data class WriteError(override val message: String) : StorageException()
    data object DataNotFound : StorageException()
}

sealed class StateException : Exception(), AppException {
    data class InvalidTransition(
        val from: String,
        val to: String,
        override val message: String = "Cannot transition from $from to $to",
    ) : StateException()

    data class InvalidState(override val message: String) : StateException()
}

sealed class ValidationException : Exception(), AppException {
    data class FieldRequired(val field: String) : ValidationException()
    data class FieldInvalid(val field: String, override val message: String) : ValidationException()
}

sealed class ResourceException : Exception(), AppException {
    data class NotFound(
        val resourceType: String,
        val resourceId: String,
        override val message: String = "$resourceType not found: $resourceId",
    ) : ResourceException()
}

sealed class ConcurrencyException : Exception(), AppException {
    data class VersionConflict(override val message: String) : ConcurrencyException()
}
