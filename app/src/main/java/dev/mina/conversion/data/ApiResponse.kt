package dev.mina.conversion.data

import com.google.gson.annotations.SerializedName

data class Symbols(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("currencies")
    val symbols: Map<String, String>? = null,
)

data class HistoricalRates(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("timestamp")
    val timestamp: Int? = null,
    @SerializedName("source")
    val source: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("quotes")
    val quotes: Map<String, Double>? = null,
)

data class TimeFrameRates(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("timeframe")
    val timeFrame: Boolean? = null,
    @SerializedName("start_date")
    val startDate: String? = null,
    @SerializedName("end_date")
    val endDate: String? = null,
    @SerializedName("source")
    val source: String? = null,
    @SerializedName("quotes")
    val quotes: Map<String, Map<String, Double>>? = null,
)