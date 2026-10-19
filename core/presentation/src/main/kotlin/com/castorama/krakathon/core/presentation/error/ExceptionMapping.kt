package com.castorama.krakathon.core.presentation.error

import androidx.annotation.StringRes
import com.castorama.krakathon.core.exception.AuthException
import com.castorama.krakathon.core.exception.ConcurrencyException
import com.castorama.krakathon.core.exception.NetworkException
import com.castorama.krakathon.core.exception.ResourceException
import com.castorama.krakathon.core.exception.StorageException
import com.castorama.krakathon.core.exception.ValidationException
import com.castorama.krakathon.core.presentation.R

/** Generic message for any error. Features map their own exceptions first, then fall back here. */
@StringRes
fun Throwable.toErrorMessage(): Int = when (this) {
    is NetworkException.NoConnection -> R.string.error_network_no_connection
    is NetworkException.Timeout -> R.string.error_network_timeout
    is NetworkException.Unauthorized, is AuthException.SessionExpired -> R.string.error_session_expired
    is NetworkException.Forbidden -> R.string.error_forbidden
    is NetworkException.NotFound, is ResourceException.NotFound, is StorageException.DataNotFound -> R.string.error_not_found
    is NetworkException.ServerError -> R.string.error_server
    is AuthException.InvalidCredentials -> R.string.error_invalid_credentials
    is ValidationException -> R.string.error_validation
    is ConcurrencyException -> R.string.error_conflict
    else -> R.string.error_generic
}
