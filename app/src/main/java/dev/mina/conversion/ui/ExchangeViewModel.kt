package dev.mina.conversion.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mina.conversion.data.ExchangeRepo
import dev.mina.conversion.data.LatestRates
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
            val symbols = exchangeRepo.getSymbols()
            symbols.error?.let { error ->
                exchangeViewModelState.update { it.copy(error = error.info) }
                return@launch
            }
            val symbolsKeys = symbols.symbols?.keys?.toList() ?: return@launch
            exchangeViewModelState.update { it.copy(symbols = symbolsKeys) }
            val baseRate = symbolsKeys[0]
            loadLatestRates(baseRate, symbolsKeys)
        }
    }

    private suspend fun loadLatestRates(
        baseRate: String,
        symbolsKeys: List<String>?,
    ) {
        val latestRates = exchangeRepo.getLatestRates(base = baseRate, symbols = symbolsKeys)
        latestRates.error?.let { error ->
            exchangeViewModelState.update { it.copy(error = error.info) }
        } ?: exchangeViewModelState.update { it.copy(latestRates = latestRates) }
    }

    fun changeSelection(from: String? = null, to: String? = null) {
        exchangeViewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            from?.let {
                loadLatestRates(baseRate = from, symbolsKeys = exchangeViewModelState.value.symbols)
            }
            exchangeViewModelState.update {
                it.copy(
                    selectedFromSymbol = from ?: it.selectedFromSymbol,
                    selectedToSymbol = to ?: it.selectedToSymbol)
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
    val latestRates: LatestRates? = null,
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
                val rate = latestRates?.rates?.get(selectedTo) ?: 1.0
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