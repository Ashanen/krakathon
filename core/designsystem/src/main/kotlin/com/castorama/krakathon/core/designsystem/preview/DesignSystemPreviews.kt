package com.castorama.krakathon.core.designsystem.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.castorama.krakathon.core.presentation.mvi.BaseUiState
import com.castorama.krakathon.core.designsystem.components.KrakBadge
import com.castorama.krakathon.core.designsystem.components.KrakButton
import com.castorama.krakathon.core.designsystem.components.KrakCard
import com.castorama.krakathon.core.designsystem.components.KrakChip
import com.castorama.krakathon.core.designsystem.components.KrakElevatedCard
import com.castorama.krakathon.core.designsystem.components.KrakEmptyState
import com.castorama.krakathon.core.designsystem.components.KrakErrorState
import com.castorama.krakathon.core.designsystem.components.KrakFilterChip
import com.castorama.krakathon.core.designsystem.components.KrakIconButton
import com.castorama.krakathon.core.designsystem.components.KrakInfoBox
import com.castorama.krakathon.core.designsystem.components.KrakKeyValueRow
import com.castorama.krakathon.core.designsystem.components.KrakOutlinedButton
import com.castorama.krakathon.core.designsystem.components.KrakOutlinedCard
import com.castorama.krakathon.core.designsystem.components.KrakPasswordField
import com.castorama.krakathon.core.designsystem.components.KrakScreen
import com.castorama.krakathon.core.designsystem.components.KrakSearchField
import com.castorama.krakathon.core.designsystem.components.KrakSectionHeader
import com.castorama.krakathon.core.designsystem.components.KrakSegment
import com.castorama.krakathon.core.designsystem.components.KrakSegmentedControl
import com.castorama.krakathon.core.designsystem.components.KrakTextArea
import com.castorama.krakathon.core.designsystem.components.KrakTextButton
import com.castorama.krakathon.core.designsystem.components.KrakTextField
import com.castorama.krakathon.core.designsystem.components.KrakTonalButton
import com.castorama.krakathon.core.designsystem.components.SkeletonBlock
import com.castorama.krakathon.core.designsystem.components.SkeletonLine
import com.castorama.krakathon.core.designsystem.theme.KrakTheme
import com.castorama.krakathon.core.designsystem.theme.colors.StatusTone

@Composable
private fun PreviewSurface(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    KrakTheme(darkTheme = darkTheme) {
        Column(
            modifier = Modifier
                .background(KrakTheme.colors.background)
                .padding(KrakTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.medium),
        ) { content() }
    }
}

@Preview(showBackground = true, name = "Buttons")
@Composable
private fun ButtonsPreview() = PreviewSurface {
    KrakButton(text = "Primary", onClick = {}, leadingIcon = Icons.Filled.Add)
    KrakButton(text = "Loading", onClick = {}, loading = true)
    KrakButton(text = "Delete", onClick = {}, destructive = true)
    KrakTonalButton(text = "Tonal", onClick = {})
    KrakOutlinedButton(text = "Outlined", onClick = {})
    KrakButton(text = "Disabled", onClick = {}, enabled = false)
    Row(horizontalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small)) {
        KrakTextButton(text = "Text button", onClick = {})
        KrakIconButton(icon = Icons.Filled.MoreVert, contentDescription = "More", onClick = {})
    }
}

@Preview(showBackground = true, name = "Fields")
@Composable
private fun FieldsPreview() = PreviewSurface {
    var text by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("secret") }
    var search by remember { mutableStateOf("drill") }
    var notes by remember { mutableStateOf("") }
    KrakTextField(value = text, onValueChange = { text = it }, label = "Name", placeholder = "Your name", maxLength = 40)
    KrakTextField(value = "oops", onValueChange = {}, label = "Email", errorText = "Enter a valid email")
    KrakPasswordField(value = password, onValueChange = { password = it }, label = "Password")
    KrakSearchField(value = search, onValueChange = { search = it })
    KrakTextArea(value = notes, onValueChange = { notes = it }, label = "Notes", placeholder = "Anything else?")
}

@Preview(showBackground = true, name = "Cards")
@Composable
private fun CardsPreview() = PreviewSurface {
    KrakSectionHeader(title = "Details", trailingActionText = "See all", onTrailingActionClick = {})
    KrakCard(modifier = Modifier.padding(horizontal = KrakTheme.spacing.extraSmall)) {
        Column(Modifier.padding(KrakTheme.spacing.medium), verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small)) {
            KrakKeyValueRow(label = "Model", value = "XR-200")
            KrakKeyValueRow(label = "Warranty", value = "24 months")
        }
    }
    KrakElevatedCard(modifier = Modifier.padding(horizontal = KrakTheme.spacing.extraSmall), onClick = {}) {
        Text("Elevated, clickable", Modifier.padding(KrakTheme.spacing.medium), style = KrakTheme.typography.bodyMedium)
    }
    KrakOutlinedCard {
        Text("Outlined", Modifier.padding(KrakTheme.spacing.medium), style = KrakTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true, name = "Selection and status")
@Composable
private fun SelectionPreview() = PreviewSurface {
    var selected by remember { mutableStateOf("list") }
    KrakSegmentedControl(
        options = listOf(KrakSegment("list", "List"), KrakSegment("grid", "Grid")),
        selectedValue = selected,
        onSelected = { selected = it },
    )
    Row(horizontalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small)) {
        KrakFilterChip(text = "In stock", selected = true, onClick = {})
        KrakFilterChip(text = "On sale", selected = false, onClick = {})
        KrakChip(text = "Add", onClick = {}, leadingIcon = Icons.Filled.Add)
    }
    Row(horizontalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small)) {
        StatusTone.entries.forEach { KrakBadge(text = it.name, tone = it, showDot = true) }
    }
    KrakInfoBox(
        title = "Add more photos",
        text = "Listings with five or more photos get noticed faster.",
        actionText = "Add photos",
        onAction = {},
    )
    KrakInfoBox(text = "Connection is unstable.", tone = StatusTone.Warning)
}

@Preview(showBackground = true, name = "States")
@Composable
private fun StatesPreview() = PreviewSurface {
    KrakEmptyState(
        title = "Nothing here yet",
        description = "Items you add will show up here.",
        actionText = "Add item",
        onAction = {},
    )
    KrakErrorState(message = "We could not load your items.", onRetry = {})
    Column(verticalArrangement = Arrangement.spacedBy(KrakTheme.spacing.small)) {
        SkeletonBlock(Modifier.fillMaxWidth().height(96.dp))
        SkeletonLine()
        SkeletonLine(Modifier.padding(end = 80.dp))
    }
}

@Preview(showBackground = true, name = "Screen with loading")
@Composable
private fun ScreenPreview() {
    KrakTheme {
        KrakScreen(
            title = "Catalog",
            baseUiState = BaseUiState(isLoading = true),
            onBaseAction = {},
            onBack = {},
            actions = { KrakIconButton(icon = Icons.Filled.MoreVert, contentDescription = "More", onClick = {}) },
        ) { padding ->
            Column(Modifier.padding(padding).padding(KrakTheme.spacing.medium)) {
                KrakSectionHeader(title = "Popular")
            }
        }
    }
}

@Preview(showBackground = true, name = "Dark")
@Composable
private fun DarkPreview() = PreviewSurface(darkTheme = true) {
    KrakButton(text = "Primary", onClick = {})
    KrakTonalButton(text = "Tonal", onClick = {})
    KrakTextField(value = "", onValueChange = {}, label = "Name", placeholder = "Your name")
    KrakCard {
        Text("Card on dark", Modifier.padding(KrakTheme.spacing.medium), color = KrakTheme.colors.onSurface)
    }
    KrakInfoBox(text = "Saved.", tone = StatusTone.Success)
}
