package dev.mina.conversion.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onDetailsClick: (String, String, Double) -> Unit,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {

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

        OutlinedButton(
            modifier = Modifier.align(CenterHorizontally),
            onClick = {
                onDetailsClick.invoke(uiState.selectedFrom,
                    uiState.selectedTo,
                    uiState.from)
            },
            border = BorderStroke(1.dp, Color.Cyan),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text(text = "Details")
        }
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
