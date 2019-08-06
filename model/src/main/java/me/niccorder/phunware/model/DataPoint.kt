package me.niccorder.phunware.model

import com.google.gson.annotations.SerializedName

data class DataPoint(
  @SerializedName("time")
  val time: Long,

  @SerializedName("summary")
  val summary: String?,

  @SerializedName("icon")
  val forecastIcon: ForecastIcon?,

  @SerializedName("temperature")
  val temperature: Double?,

  @SerializedName("humidity")
  val humidity: Double?
)