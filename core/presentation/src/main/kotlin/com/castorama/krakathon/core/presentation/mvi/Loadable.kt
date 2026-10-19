package com.castorama.krakathon.core.presentation.mvi

import androidx.annotation.StringRes

/**
 * Screen data that loads. Exactly one of: still loading, ready, or failed with a message.
 * A failed first load is a state the screen renders (inline error + retry) — never an endless skeleton.
 */
sealed interface Loadable<out T> {
    data object Loading : Loadable<Nothing>
    data class Ready<out T>(val value: T) : Loadable<T>
    data class Failed(@StringRes val message: Int) : Loadable<Nothing>

    val valueOrNull: T? get() = (this as? Ready)?.value
}
