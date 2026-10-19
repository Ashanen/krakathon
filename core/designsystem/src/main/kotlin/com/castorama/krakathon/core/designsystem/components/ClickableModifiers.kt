package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

/** [clickable] that also exposes `Role.Button` to accessibility services. */
fun Modifier.clickableAsButton(
    onClickLabel: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = clickable(enabled = enabled, onClickLabel = onClickLabel, role = Role.Button, onClick = onClick)

/** Click without ripple, for surfaces that draw their own pressed state. */
@Composable
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    enabled = enabled,
    onClick = onClick,
)
