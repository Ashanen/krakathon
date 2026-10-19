package com.castorama.krakathon.core.presentation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castorama.krakathon.core.exception.toErrorType
import com.castorama.krakathon.core.presentation.error.toErrorMessage
import com.castorama.krakathon.core.presentation.flow.launchWith
import com.castorama.krakathon.core.presentation.mvi.BaseAction
import com.castorama.krakathon.core.presentation.mvi.BaseUiState
import com.castorama.krakathon.core.presentation.mvi.ErrorDialogState
import com.castorama.krakathon.core.presentation.mvi.InfoDialogState
import com.castorama.krakathon.core.presentation.mvi.Loadable
import com.castorama.krakathon.core.presentation.mvi.ScreenState
import com.castorama.krakathon.core.presentation.mvi.SnackbarState
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job

/**
 * MVI base: one [uiState], one [onAction] entry point, shared dialogs/snackbar via [onBaseAction].
 * Collect flows only with `launchWith(viewModelScope, onError = ::showError)`.
 */
abstract class BaseViewModel<S : ScreenState<S>, A>(initialState: S) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private var pendingRetry: (() -> Unit)? = null

    abstract fun onAction(action: A)

    fun onBaseAction(action: BaseAction) {
        when (action) {
            BaseAction.DismissError -> hideError()
            BaseAction.Retry -> pendingRetry.also { hideError() }?.invoke()
            BaseAction.DismissInfo -> updateBase { copy(infoDialogState = InfoDialogState.Hidden) }
            BaseAction.DismissSnackbar -> updateBase { copy(snackbarState = SnackbarState.Hidden) }
            BaseAction.PerformSnackbarAction -> onSnackbarAction()
        }
    }

    protected fun updateState(reducer: S.() -> S) = _uiState.update(reducer)

    protected val currentState: S get() = _uiState.value

    protected fun setLoading(isLoading: Boolean) = updateBase { copy(isLoading = isLoading) }

    /** Shows an error dialog. Pass [retry] only for idempotent work (loads), never for submits. */
    protected fun showError(error: Throwable, retry: (() -> Unit)? = null) {
        pendingRetry = retry
        updateBase {
            copy(
                isLoading = false,
                errorDialogState = ErrorDialogState(
                    isVisible = true,
                    errorType = error.toErrorType(),
                    message = errorMessage(error),
                    canRetry = retry != null,
                ),
            )
        }
    }

    /**
     * Collects screen data into a [Loadable] slot of the state: Loading -> Ready, or Failed on error.
     * Retry = call the same load function again; it re-subscribes from scratch.
     *
     * ```kotlin
     * getProductsUseCase().launchLoadable { copy(products = it) }
     * ```
     */
    protected fun <T> Flow<T>.launchLoadable(into: S.(Loadable<T>) -> S): Job =
        onStart { updateState { into(Loadable.Loading) } }
            .onEach { value -> updateState { into(Loadable.Ready(value)) } }
            .launchWith(viewModelScope) { error -> updateState { into(Loadable.Failed(errorMessage(error))) } }

    protected fun showInfo(infoDialogState: InfoDialogState) = updateBase { copy(infoDialogState = infoDialogState) }

    protected fun showSnackbar(snackbarState: SnackbarState) = updateBase {
        copy(snackbarState = snackbarState.copy(token = this.snackbarState.token + 1))
    }

    /** Override to map this feature's own exceptions first, then fall back to `super`. */
    @StringRes
    protected open fun errorMessage(error: Throwable): Int = error.toErrorMessage()

    /** Override to react to the snackbar action button. Default: dismiss. */
    protected open fun onSnackbarAction() = updateBase { copy(snackbarState = SnackbarState.Hidden) }

    private fun hideError() {
        pendingRetry = null
        updateBase { copy(errorDialogState = ErrorDialogState.Hidden) }
    }

    private fun updateBase(reducer: BaseUiState.() -> BaseUiState) =
        _uiState.update { it.withBaseUiState(it.baseUiState.reducer()) }
}
