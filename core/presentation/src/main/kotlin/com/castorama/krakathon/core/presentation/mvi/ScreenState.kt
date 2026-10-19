package com.castorama.krakathon.core.presentation.mvi

/** Every screen state carries [BaseUiState] (loading, error dialog, info dialog, snackbar). */
interface ScreenState<S : ScreenState<S>> {
    val baseUiState: BaseUiState
    fun withBaseUiState(baseUiState: BaseUiState): S
}

data class BaseUiState(
    val isLoading: Boolean = false,
    val errorDialogState: ErrorDialogState = ErrorDialogState.Hidden,
    val infoDialogState: InfoDialogState = InfoDialogState.Hidden,
    val snackbarState: SnackbarState = SnackbarState.Hidden,
)

sealed interface BaseAction {
    data object DismissError : BaseAction
    data object Retry : BaseAction
    data object DismissInfo : BaseAction
    data object DismissSnackbar : BaseAction
    data object PerformSnackbarAction : BaseAction
}
