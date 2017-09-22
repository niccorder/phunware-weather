package me.niccorder.phunware.data.model

import com.google.gson.annotations.SerializedName

/**
 * The model which represents a forecast for a location. Any other models below are sub-models (if
 * you will) that are used for easy JSON parsing by [com.google.gson.Gson].
 */
data class Forecast(
        @SerializedName("latitude") val latitude: Float,
        @SerializedName("longitude") val longitude: Float,
        @SerializedName("timezone") val timezone: String,
        @SerializedName("currently") val current: DataPoint?
)

data class DataPoint(
        @SerializedName("time") val time: Long,
        @SerializedName("summary") val summary: String?,
        @SerializedName("icon") val forecastIcon: ForecastIcon?,
        @SerializedName("temperature") val temperature: Double?,
        @SerializedName("humidity") val humidity: Double?
)

@Suppress("ArrayInDataClass")
data class DataBlock(
        @SerializedName("data") val data: Array<DataPoint>?,
        @SerializedName("summary") val summary: String?,
        @SerializedName("icon") val forecastIcon: ForecastIcon?
)

@Suppress("ArrayInDataClass")
data class ForecastAlert(
        @SerializedName("description") val description: String,
        @SerializedName("title") val title: String,
        @SerializedName("uri") val referenceUri: String,
        @SerializedName("expires") val expires: Long,
        @SerializedName("regions") val regions: Array<String>?,
        @SerializedName("severity") val severity: AlertSeverity,
        @SerializedName("time") val time: Long
)

enum class ForecastIcon(val type: String) {
    CLEAR_DAY("clear-day"),
    CLEAR_NIGHT("clear-night"),
    RAIN("rain"),
    SNOW("snow"),
    SLEET("sleet"),
    WIND("wind"),
    FOG("fog"),
    CLOUDY("cloudy"),
    PARTLY_CLOUDY_DAY("partly-cloudy-day"),
    PARTLY_CLOUDY_NIGHT("partly-cloudy-night"),
    UNKNOWN("unknown")
}

enum class AlertSeverity(val type: String) {
    ADVISORY("advisory"),
    WATCH("watch"),
    WARNING("warning"),
    UNKNOWN("unknown")
}