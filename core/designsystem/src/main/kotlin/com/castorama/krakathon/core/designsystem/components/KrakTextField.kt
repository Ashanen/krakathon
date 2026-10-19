package com.castorama.krakathon.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.graphics.vector.ImageVector
import com.castorama.krakathon.core.designsystem.R
import com.castorama.krakathon.core.designsystem.theme.KrakTheme

/** Label sits above the field; [errorText] turns the field red and replaces [supportingText]. */
@Composable
fun KrakTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLength: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val colors = KrakTheme.colors
    val isError = errorText != null
    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = KrakTheme.typography.labelLarge,
                color = if (isError) colors.error else colors.onSurfaceVariant,
                modifier = Modifier.padding(bottom = KrakTheme.spacing.extraSmall),
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = { if (maxLength == null || it.length <= maxLength) onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = if (singleLine) KrakTheme.dimensions.textFieldHeight else KrakTheme.dimensions.multilineTextFieldHeight),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = KrakTheme.typography.bodyLarge,
            placeholder = placeholder?.let { { Text(it, style = KrakTheme.typography.bodyLarge) } },
            leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null) } },
            trailingIcon = trailingContent,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            minLines = if (singleLine) 1 else minLines,
            shape = KrakTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.surface,
                unfocusedContainerColor = colors.surface,
                disabledContainerColor = colors.surfaceVariant,
                errorContainerColor = colors.surface,
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.outline,
                disabledBorderColor = colors.outlineVariant,
                errorBorderColor = colors.error,
                focusedTextColor = colors.onSurface,
                unfocusedTextColor = colors.onSurface,
                disabledTextColor = colors.onSurfaceVariant,
                errorTextColor = colors.onSurface,
                cursorColor = colors.primary,
                errorCursorColor = colors.error,
                focusedPlaceholderColor = colors.onSurfaceVariant,
                unfocusedPlaceholderColor = colors.onSurfaceVariant,
                focusedLeadingIconColor = colors.primary,
                unfocusedLeadingIconColor = colors.onSurfaceVariant,
                errorLeadingIconColor = colors.error,
            ),
        )
        if (errorText != null || supportingText != null || maxLength != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = KrakTheme.spacing.extraSmall, start = KrakTheme.spacing.medium, end = KrakTheme.spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small),
            ) {
                Text(
                    text = errorText ?: supportingText.orEmpty(),
                    style = KrakTheme.typography.bodySmall,
                    color = if (isError) colors.error else colors.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                if (maxLength != null) {
                    Text(
                        text = stringResource(R.string.ds_character_counter, value.length, maxLength),
                        style = KrakTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
fun KrakTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    maxLength: Int? = null,
    minLines: Int = 4,
) {
    KrakTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        supportingText = supportingText,
        errorText = errorText,
        enabled = enabled,
        singleLine = false,
        minLines = minLines,
        maxLength = maxLength,
    )
}
