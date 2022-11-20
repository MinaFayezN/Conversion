package dev.mina.conversion.data

import javax.inject.Inject

interface ExchangeRepo {
    suspend fun getSymbols(): Symbols
    suspend fun getLatestRates(
        base: String? = null,
        symbols: List<String>? = null,
    ): LatestRates
}

class ExchangeRepoMockImpl @Inject constructor() : ExchangeRepo {
    override suspend fun getSymbols(): Symbols =
        Symbols(success = true,
            symbols = mapOf(
                "AED" to "United Arab Emirates Dirham",
                "AFN" to "Afghan Afghani",
                "ALL" to "Albanian Lek",
                "AMD" to "Armenian Dram"
            ),
            error = null)


    override suspend fun getLatestRates(base: String?, symbols: List<String>?): LatestRates =
        LatestRates(success = true,
            timestamp = 1519296206,
            base = "USD",
            date = "2022-02-24",
            rates = mapOf(
                "GBP" to 0.72007,
                "JPY" to 107.346001,
                "ALL" to 17.346001,
                "AFN" to 0.813399,
                "AED" to 0.8145499
            ),
            error = null)
}