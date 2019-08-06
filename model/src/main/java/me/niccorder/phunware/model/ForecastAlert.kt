package me.niccorder.phunware.model

import com.google.gson.annotations.SerializedName

@Suppress("ArrayInDataClass")
data class ForecastAlert(
  @SerializedName("description")
  val description: String,

  @SerializedName("title")
  val title: String,

  @SerializedName("uri")
  val referenceUri: String,

  @SerializedName("expires")
  val expires: Long,
  @SerializedName("regions")
  val regions: Array<String>?,

  @SerializedName("severity")
  val severity: AlertSeverity,

  @SerializedName("time")
  val time: Long
)