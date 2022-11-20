package dev.mina.conversion.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.mina.conversion.ui.components.FromCard
import dev.mina.conversion.ui.components.SwitchComponents
import dev.mina.conversion.ui.components.ToCard

@Composable
fun ExchangeScreen(
    uiState: ExchangeScreenUIState.ExchangeUIState,
    onFromSelectionChange: (String) -> Unit,
    onFromValueChange: (Double) -> Unit,
    onToSelectionChange: (String) -> Unit,
    onSwitchClick: (String, String) -> Unit,
) {
    Box(Modifier
        .wrapContentSize(Alignment.Center)
        .padding(16.dp)) {
        Column {
            FromCard(
                rateList = uiState.fromSymbols,
                assignedValue = uiState.from,
                onValueChange = onFromValueChange,
                onSelectionChange = onFromSelectionChange
            )
            ToCard(
                rateList = uiState.toSymbols,
                convertedValue = uiState.to,
                onSelectionChange = onToSelectionChange
            )
        }
        SwitchComponents(
            uiState = uiState,
            onSwitchCLick = onSwitchClick,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

sealed class ExchangeScreenUIState {
    object Loading : ExchangeScreenUIState()
    data class Error(val message: String) : ExchangeScreenUIState()
    data class ExchangeUIState(
        val fromSymbols: List<String>,
        val toSymbols: List<String>,
        val selectedFrom: String,
        val selectedTo: String,
        val rate: Double,
        val from: Double,
        val to: Double,
        val isLoading: Boolean,
    ) : ExchangeScreenUIState()
}
