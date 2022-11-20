package dev.mina.conversion.data


import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("info")
    val info: String? = null,
)

data class Symbols(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("symbols")
    val symbols: Map<String, String>? = null,
    @SerializedName("error")
    val error: Error? = null,
)

data class LatestRates(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("timestamp")
    val timestamp: Int? = null,
    @SerializedName("base")
    val base: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("rates")
    val rates: Map<String, Double>? = null,
    @SerializedName("error")
    val error: Error? = null,
)