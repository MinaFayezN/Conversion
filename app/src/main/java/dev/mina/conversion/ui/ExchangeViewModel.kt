package dev.mina.conversion.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mina.conversion.data.ExchangeRepo
import dev.mina.conversion.data.HistoricalRates
import dev.mina.conversion.ui.screens.ExchangeScreenUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(private val exchangeRepo: ExchangeRepo) : ViewModel() {

    private val exchangeViewModelState = MutableStateFlow(ExchangeViewModelState())
    val uiState = exchangeViewModelState
        .map(ExchangeViewModelState::toUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = exchangeViewModelState.value.toUiState()
        )

    init {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val symbols = exchangeRepo.getSymbols()
                val symbolsKeys = symbols.symbols?.keys?.toList() ?: return@launch
                exchangeViewModelState.update { it.copy(symbols = symbolsKeys) }
                val baseRate = symbolsKeys[0]
                loadHistoricalRates(baseRate)
            } catch (e: Exception) {
                Log.d("ExchangeViewModel::init", e.message ?: e.toString())
                exchangeViewModelState.update { it.copy(error = "Something went wrong. Please try again later") }
            }
        }
    }

    private suspend fun loadHistoricalRates(baseRate: String) {
        val historicalRates = exchangeRepo.getHistoricalRates(source = baseRate)
        exchangeViewModelState.update { it.copy(historicalRates = historicalRates) }
    }

    fun changeSelection(from: String? = null, to: String? = null) {
        exchangeViewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            from?.let {
                loadHistoricalRates(baseRate = from)
            }
            exchangeViewModelState.update {
                it.copy(
                    selectedFromSymbol = from ?: it.selectedFromSymbol,
                    selectedToSymbol = to ?: it.selectedToSymbol,
                    isLoading = false)
            }
        }
    }

    fun changeValue(newValue: Double) {
        exchangeViewModelState.update {
            it.copy(valueToConvert = newValue)
        }
    }
}


data class ExchangeViewModelState(
    val error: String? = null,
    val symbols: List<String>? = null,
    val selectedFromSymbol: String? = null,
    val selectedToSymbol: String? = null,
    val historicalRates: HistoricalRates? = null,
    val valueToConvert: Double = 1.0,
    val isLoading: Boolean = false,
) {

    fun toUiState(): ExchangeScreenUIState {
        return symbols?.let { symbolsList ->
            runCatching {
                val selectedFrom = selectedFromSymbol ?: symbolsList[0]
                val fromSymbols = symbolsList.toMutableList().moveItemToTop(selectedFrom)
                val selectedTo = selectedToSymbol ?: fromSymbols[1]
                val toSymbols = symbolsList.toMutableList().moveItemToTop(selectedTo)
                fromSymbols.remove(selectedTo) //To prevent selecting same value in From as in To
                toSymbols.remove(selectedFrom) //To prevent selecting same value in To as in From
                val rate = historicalRates?.quotes?.get("$selectedFrom$selectedTo") ?: 1.0
                val convertedValue = (valueToConvert * rate)
                ExchangeScreenUIState.ExchangeUIState(
                    fromSymbols = fromSymbols,
                    selectedFrom = selectedFrom,
                    toSymbols = toSymbols,
                    selectedTo = selectedTo,
                    rate = rate,
                    from = valueToConvert,
                    to = convertedValue,
                    isLoading = isLoading)
            }.getOrElse {
                Log.d("ExchangeViewModelState::toUiState", it.message ?: it.toString())
                ExchangeScreenUIState.Error("Something went wrong. Please try again later")
            }
        } ?: error?.let {
            ExchangeScreenUIState.Error(it)
        } ?: ExchangeScreenUIState.Loading
    }

}