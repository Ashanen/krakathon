package com.castorama.krakathon.core.presentation.mvi

import androidx.annotation.StringRes
import com.castorama.krakathon.core.exception.ErrorType

data class ErrorDialogState(
    val isVisible: Boolean = false,
    val errorType: ErrorType = ErrorType.Generic,
    @StringRes val title: Int? = null,
    @StringRes val message: Int? = null,
    val canRetry: Boolean = false,
) {
    companion object {
        val Hidden = ErrorDialogState()
    }
}

data class InfoDialogState(
    val isVisible: Boolean = false,
    @StringRes val title: Int? = null,
    @StringRes val message: Int? = null,
    @StringRes val primaryButtonText: Int? = null,
) {
    companion object {
        val Hidden = InfoDialogState()

        fun show(@StringRes title: Int, @StringRes message: Int, @StringRes primaryButtonText: Int) =
            InfoDialogState(true, title, message, primaryButtonText)
    }
}

enum class SnackbarType { Success, Error, Info }

enum class SnackbarDuration(val milliseconds: Long) {
    Short(4_000L),
    Action(6_000L),
    Long(10_000L),
}

data class SnackbarState(
    val isVisible: Boolean = false,
    @StringRes val message: Int? = null,
    val formatArgs: List<Any> = emptyList(),
    val type: SnackbarType = SnackbarType.Info,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    @StringRes val actionLabel: Int? = null,
    /** Bumped on every show, so the same message twice restarts the timer. */
    val token: Long = 0L,
) {
    companion object {
        val Hidden = SnackbarState()

        fun success(@StringRes message: Int, vararg formatArgs: Any, @StringRes actionLabel: Int? = null) =
            create(SnackbarType.Success, message, formatArgs.toList(), actionLabel)

        fun error(@StringRes message: Int, vararg formatArgs: Any, @StringRes actionLabel: Int? = null) =
            create(SnackbarType.Error, message, formatArgs.toList(), actionLabel).copy(duration = SnackbarDuration.Long)

        fun info(@StringRes message: Int, vararg formatArgs: Any, @StringRes actionLabel: Int? = null) =
            create(SnackbarType.Info, message, formatArgs.toList(), actionLabel)

        private fun create(type: SnackbarType, message: Int, args: List<Any>, actionLabel: Int?) = SnackbarState(
            isVisible = true,
            message = message,
            formatArgs = args,
            type = type,
            duration = if (actionLabel != null) SnackbarDuration.Action else SnackbarDuration.Short,
            actionLabel = actionLabel,
        )
    }
}
