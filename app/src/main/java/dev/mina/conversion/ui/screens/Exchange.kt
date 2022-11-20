package dev.mina.conversion.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mina.conversion.ui.components.Card
import dev.mina.conversion.ui.components.CardType
import dev.mina.conversion.ui.components.SwitchComponents
import dev.mina.conversion.ui.roundDecimalPlaces

@Composable
fun ExchangeScreen(
    uiState: ExchangeScreenUIState.ExchangeUIState,
    onFromSelectionChange: (String) -> Unit,
    onFromValueChange: (String) -> Unit,
    onToSelectionChange: (String) -> Unit,
    onSwitchClick: () -> Unit,
) {
    var convertedValue by remember { mutableStateOf(uiState.rate) }
    LaunchedEffect(uiState.from, uiState.rate) {
        convertedValue = (uiState.from * uiState.rate).roundDecimalPlaces(places = 4)
    }
    Box(Modifier
        .wrapContentSize(Alignment.Center)
        .padding(16.dp)) {
        Column {
            Card(type = CardType.From,
                rateList = uiState.fromSymbols,
                assignedValue = uiState.from,
                onValueChange = onFromValueChange,
                onSelectionChange = onFromSelectionChange
            )
            Card(type = CardType.To,
                rateList = uiState.toSymbols,
                assignedValue = convertedValue,
                onValueChange = {},
                onSelectionChange = onToSelectionChange)
        }
        SwitchComponents(
            isLoading = false,
            rate = uiState.rate,
            onSwitchCLick = onSwitchClick,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

sealed class ExchangeScreenUIState {
    object Loading : ExchangeScreenUIState()
    data class ExchangeUIState(
        val fromSymbols: List<String>,
        val toSymbols: List<String>,
        val selectedFrom: String,
        val selectedTo: String,
        val rate: Double,
        val from: Double,
        val isLoading: Boolean,
    ) : ExchangeScreenUIState()
}
