package com.castorama.krakathon.core.exception

enum class ErrorType { Network, Server, NotFound, Permission, Validation, Generic }

fun Throwable.toErrorType(): ErrorType = when (this) {
    is NetworkException.NoConnection, is NetworkException.Timeout -> ErrorType.Network
    is NetworkException.ServerError -> ErrorType.Server
    is NetworkException.NotFound, is ResourceException.NotFound, is StorageException.DataNotFound -> ErrorType.NotFound
    is NetworkException.Unauthorized, is NetworkException.Forbidden, is AuthException -> ErrorType.Permission
    is ValidationException -> ErrorType.Validation
    else -> ErrorType.Generic
}
