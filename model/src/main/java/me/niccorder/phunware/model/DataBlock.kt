package me.niccorder.phunware.model

import com.google.gson.annotations.SerializedName

data class DataBlock(

  @SerializedName("data")
  val data: List<DataPoint>,

  @SerializedName("summary")
  val summary: String?,

  @SerializedName("icon")
  val forecastIcon: ForecastIcon?
)