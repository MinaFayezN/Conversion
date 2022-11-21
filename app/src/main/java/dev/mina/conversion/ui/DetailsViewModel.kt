package dev.mina.conversion.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mina.conversion.data.DetailsRepo
import dev.mina.conversion.ui.screens.DetailsScreenUIState
import dev.mina.conversion.ui.screens.DetailsScreenUIState.DetailsUIState
import dev.mina.conversion.ui.screens.HistoricItemUistate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val detailsRepo: DetailsRepo) : ViewModel() {

    private val detailsViewModelState = MutableStateFlow(DetailsViewModelState())
    val uiState = detailsViewModelState
        .map(DetailsViewModelState::toUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = detailsViewModelState.value.toUiState()
        )

    fun loadHistoricalRates(
        from: String,
        to: String,
        value: Double,
    ) {
        detailsViewModelState.update { it.copy(rates = null) }
        viewModelScope.launch(Dispatchers.IO) {
            val timeFrameRates = detailsRepo.getTimeFrameRates(source = from)
            detailsViewModelState.update {
                it.copy(
                    rates = timeFrameRates.quotes,
                    selectedFromSymbol = from,
                    selectedToSymbol = to,
                    valueToConvert = value)
            }
        }
    }

}


data class DetailsViewModelState(
    val error: String? = null,
    val rates: Map<String, Map<String, Double>>? = null,
    val selectedFromSymbol: String? = null,
    val selectedToSymbol: String? = null,
    val valueToConvert: Double = 1.0,
) {

    fun toUiState(): DetailsScreenUIState {
        return rates?.let { symbolsList ->
            runCatching {
                val historicItemUistates = symbolsList.keys.map {
                    HistoricItemUistate(date = it,
                        fromText = selectedFromSymbol,
                        fromRate = valueToConvert.toString(),
                        toText = selectedToSymbol,
                        toRate = (symbolsList[it]?.get("$selectedFromSymbol$selectedToSymbol") ?: 1.0).toDouble()
                            .times(valueToConvert).toString()
                    )
                }
                val others = rates?.flatMap {
                    listOf(listOf("Date: ${it.key}"),
                        it.value.toList().map { rate -> "${rate.first}: ${rate.second}" }).flatten()
                } ?: emptyList()
                DetailsUIState(historicalItems = historicItemUistates, others = others)
            }.getOrElse {
                Log.d("ExchangeViewModelState::toUiState", it.message ?: it.toString())
                DetailsScreenUIState.Error("Something went wrong. Please try again later")
            }
        } ?: error?.let {
            DetailsScreenUIState.Error(it)
        } ?: DetailsScreenUIState.Loading
    }

}