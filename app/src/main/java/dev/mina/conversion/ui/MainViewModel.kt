package dev.mina.conversion.ui

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
class MainViewModel @Inject constructor(private val exchangeRepo: ExchangeRepo) : ViewModel() {

    private val viewModelState = MutableStateFlow(ViewModelState())

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val symbols = exchangeRepo.getSymbols()
            symbols.error?.let {
                // handle error
                return@launch
            }
            val symbolsKeys = symbols.symbols?.keys?.toList() ?: return@launch
            val baseRate = symbolsKeys[0]
            viewModelState.update {
                it.copy(
                    symbols = symbolsKeys,
                )
            }
            val latestRates =
                exchangeRepo.getLatestRates(base = baseRate, symbols = symbolsKeys)
            latestRates.error?.let {
                // handle error
                return@launch
            }
            viewModelState.update {
                it.copy(
                    latestRates = latestRates,
                )
            }
        }
    }
}


data class ViewModelState(
    val symbols: List<String>? = null,
    val selectedFromSymbol: String? = null,
    val selectedToSymbol: String? = null,
    val latestRates: LatestRates? = null,
    val valueToConvert: Double = 1.0,
) {

    fun toUiState(): ExchangeScreenUIState {
        return symbols?.let { symbolsList ->
            val selectedFrom = selectedFromSymbol ?: symbolsList[0]
            val fromSymbols =
                symbolsList.toMutableList().moveItemToTop(selectedFrom) // Add selected on on top of the list
            val selectedTo = selectedToSymbol ?: fromSymbols[1]
            val toSymbols = symbolsList.toMutableList().apply {
                remove(selectedFrom)
                moveItemToTop(selectedTo)
            }
            val rate = latestRates?.rates?.get(selectedTo) ?: 1.0
            ExchangeScreenUIState.ExchangeUIState(
                fromSymbols = fromSymbols,
                selectedFrom = selectedFrom,
                toSymbols = toSymbols,
                selectedTo = selectedTo,
                rate = rate,
                from = valueToConvert,
                isLoading = false)
        } ?: ExchangeScreenUIState.Loading
    }

}