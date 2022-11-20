package dev.mina.conversion.ui

import android.util.Log
import dev.mina.conversion.ui.screens.ExchangeScreenUIState
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test

class ExchangeViewModelStateTest {

    @Before
    fun setUp() {
        mockkConstructor(ExchangeViewModelState::class)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun checkUiState_IsLoading_WhenSymbolsIsNull() {
        val viewModelState = ExchangeViewModelState(symbols = null)
        val result = viewModelState.toUiState()
        assert(result is ExchangeScreenUIState.Loading)
    }

    @Test
    fun checkUiState_IsError_WhenSymbolsIsEmpty() {
        val viewModelState = ExchangeViewModelState(symbols = listOf())
        val result = viewModelState.toUiState()
        assert(result is ExchangeScreenUIState.Error)
    }

    @Test
    fun checkUiState_IsExchangeUiState_WhenSymbolsIsNotnullNorEmpty() {
        val viewModelState = ExchangeViewModelState(symbols = listOf("EGP", "AED", "USD", "EUR"))
        val result = viewModelState.toUiState()
        assert(result is ExchangeScreenUIState.ExchangeUIState)
    }

    @Test
    fun checkUiState_HasValidData_WhenSymbolsIsNotnullNorEmptyAndNoSymbolsSent() {
        val viewModelState = ExchangeViewModelState(symbols = listOf("EGP", "AED", "USD", "EUR"))
        val result = viewModelState.toUiState()
        assert(result is ExchangeScreenUIState.ExchangeUIState)
        val castedResult = result as ExchangeScreenUIState.ExchangeUIState
        val fromSymbolsSize = castedResult.fromSymbols.size
        val toSymbolsSize = castedResult.toSymbols.size
        assert(fromSymbolsSize > 1)
        assert(toSymbolsSize > 1)
        assert(fromSymbolsSize == toSymbolsSize)
        assert(castedResult.isLoading.not())
        assert(castedResult.rate == 1.0)
    }

    @Test
    fun checkUiState_HasValidData_WhenSymbolsIsNotnullNorEmptyAndFromSymbolSent() {
        val viewModelState = ExchangeViewModelState(symbols = listOf("EGP", "AED", "USD", "EUR"),
            selectedFromSymbol = "AED")
        val result = viewModelState.toUiState()
        assert(result is ExchangeScreenUIState.ExchangeUIState)
        val castedResult = result as ExchangeScreenUIState.ExchangeUIState
        val fromSymbolsSize = castedResult.fromSymbols.size
        val toSymbolsSize = castedResult.toSymbols.size
        assert(fromSymbolsSize > 1)
        assert(toSymbolsSize > 1)
        assert(fromSymbolsSize == toSymbolsSize)
        assert(castedResult.isLoading.not())
        assert(castedResult.rate == 1.0)
        assert(castedResult.fromSymbols[0] == "AED")
        assert(castedResult.selectedFrom == "AED")
    }

    @Test
    fun checkUiState_HasValidData_WhenSymbolsIsNotnullNorEmptyAndToSymbolSent() {
        val viewModelState = ExchangeViewModelState(
            symbols = listOf("EGP", "AED", "USD", "EUR"),
            selectedFromSymbol = "AED",
            selectedToSymbol = "USD"
        )
        val result = viewModelState.toUiState()
        assert(result is ExchangeScreenUIState.ExchangeUIState)
        val castedResult = result as ExchangeScreenUIState.ExchangeUIState
        val fromSymbolsSize = castedResult.fromSymbols.size
        val toSymbolsSize = castedResult.toSymbols.size
        assert(fromSymbolsSize > 1)
        assert(toSymbolsSize > 1)
        assert(fromSymbolsSize == toSymbolsSize)
        assert(castedResult.isLoading.not())
        assert(castedResult.rate == 1.0)
        assert(castedResult.selectedFrom == "AED")
        assert(castedResult.fromSymbols[0] == "AED")
        assert(castedResult.selectedTo == "USD")
        assert(castedResult.toSymbols[0] == "USD")
    }

}