package me.niccorder.phunware.model

import com.google.gson.annotations.SerializedName

/**
 * The model which represents a forecast for a location. Any other models below are sub-models (if
 * you will) that are used for easy JSON parsing by [com.google.gson.Gson].
 */
data class Forecast(
  @SerializedName("latitude")
  val latitude: Float,

  @SerializedName("longitude")
  val longitude: Float,

  @SerializedName("timezone")
  val timezone: String,

  @SerializedName("currently")
  val current: DataPoint?

)

