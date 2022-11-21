package dev.mina.conversion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.mina.conversion.ui.components.HistoricItem
import dev.mina.conversion.ui.components.OthersItems

@Composable
fun DetailsScreen(uiState: DetailsScreenUIState.DetailsUIState) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(Modifier
            .fillMaxHeight()
            .weight(1F)) {
            uiState.historicalItems.forEach {
                HistoricItem(date = it.date ?: "",
                    base = it.fromText ?: "",
                    baseValue = it.fromRate ?: "1.0",
                    target = it.toText ?: "",
                    convertedValue = it.toRate ?: "1.0")
            }
            OthersItems(modifier = Modifier
                .fillMaxWidth()
                .weight(1F), uiState = uiState)
        }

    }

}


sealed class DetailsScreenUIState {
    object Loading : DetailsScreenUIState()
    data class Error(val message: String) : DetailsScreenUIState()
    data class DetailsUIState(
        val historicalItems: List<HistoricItemUistate>,
        val others: List<String>,
    ) : DetailsScreenUIState()
}

data class HistoricItemUistate(
    val date: String? = null,
    val fromText: String? = null,
    val fromRate: String? = null,
    val toText: String? = null,
    val toRate: String? = null,
)