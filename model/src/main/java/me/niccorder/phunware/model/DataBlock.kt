package me.niccorder.phunware.model

import com.google.gson.annotations.SerializedName

@Suppress("ArrayInDataClass")
data class DataBlock(
  @SerializedName("data")
  val data: Array<DataPoint>?,

  @SerializedName("summary")
  val summary: String?,

  @SerializedName("icon")
  val forecastIcon: ForecastIcon?
)