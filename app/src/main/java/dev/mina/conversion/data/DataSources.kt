package dev.mina.conversion.data

import retrofit2.http.GET
import retrofit2.http.Query


interface FixerAPI {

    @GET("list")
    suspend fun getSymbols(): Symbols

    @GET("historical")
    suspend fun getHistoricalRates(
        @Query("date")
        currentDayDate: String? = null,
        @Query("source")
        source: String? = null,
    ): HistoricalRates


    @GET("timeframe")
    suspend fun getTimeFrameRates(
        @Query("start_date")
        prev3DaysDate: String? = null,
        @Query("end_date")
        currentDayDate: String? = null,
        @Query("source")
        base: String? = null,
    ): TimeFrameRates


}
